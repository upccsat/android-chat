package com.ntu.treatment.im;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.alibaba.fastjson.JSON;
import com.ntu.treatment.ChatFriendActivity;
import com.ntu.treatment.ChatGroupActivity;
import com.ntu.treatment.R;
import com.ntu.treatment.util.Util;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;


public class JWebSocketClientService extends Service {
    public static JWebSocketClient client;
    private JWebSocketClientBinder mBinder = new JWebSocketClientBinder();
    private final static int GRAY_SERVICE_ID = 1001;
    private String username;

    //灰色保活
    public static class GrayInnerService extends Service {

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(GRAY_SERVICE_ID, new Notification());
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
    PowerManager.WakeLock wakeLock;//锁屏唤醒
    //获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行
    @SuppressLint("InvalidWakeLockTag")
    private void acquireWakeLock()
    {
        if (null == wakeLock)
        {
            PowerManager pm = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK| PowerManager.ON_AFTER_RELEASE, "PostLocationService");
            if (null != wakeLock)
            {
                wakeLock.acquire();
            }
        }
    }

    //用于Activity和service通讯
    public class JWebSocketClientBinder extends Binder {
        public JWebSocketClientService getService() {
            return JWebSocketClientService.this;
        }
    }
    public void setUsername(String username){
        this.username=username;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public IBinder onBind(Intent intent) {
        initSocketClient();
        username="";
//        mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//开启心跳检测

        //设置service为前台服务，提高优先级
        if (Build.VERSION.SDK_INT < 18) {
            //Android4.3以下 ，隐藏Notification上的图标
            startForeground(GRAY_SERVICE_ID, new Notification());
        } else if(Build.VERSION.SDK_INT>18 && Build.VERSION.SDK_INT<25){
            //Android4.3 - Android7.0，隐藏Notification上的图标
            Intent innerIntent = new Intent(this, GrayInnerService.class);
            startService(innerIntent);
            startForeground(GRAY_SERVICE_ID, new Notification());
        }else{
            //Android7.0以上app启动后通知栏会出现一条"正在运行"的通知
            startForegroundService(new Intent(this,GrayInnerService.class));
        }

        acquireWakeLock();
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        closeConnect();
        super.onDestroy();
    }


    /**
     * 初始化websocket连接
     */
    private void initSocketClient() {
        URI uri = URI.create(Util.ws);
        client = new JWebSocketClient(uri) {
            @Override
            public void onMessage(String message) {
                Log.e("JWebSocketClientService", "收到的消息：" + message);
                com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(message);
                String fromUserName= jsonObject.getString("fromUserName");
                String toUserName=jsonObject.getString("toUserName");
                String content=jsonObject.getString("content");
                String groupName= jsonObject.getString("groupName");
                Integer groupId=Integer.parseInt(jsonObject.getString("groupId"));

                Intent intent = new Intent();
                if(groupId==0){
                    intent.setAction("com.xch.servicecallback.content");
                    intent.putExtra("message", message);
                    if(username.equals(fromUserName)){
                        checkLockAndShowNotification("来自好友："+fromUserName,content,R.drawable.contact_head_icon,username,toUserName,"",0);
                    }else if(username.equals(toUserName)){
                        checkLockAndShowNotification("来自好友："+fromUserName,content,R.drawable.contact_head_icon,username,fromUserName,"",0);
                    }

                }else{
                    intent.setAction("com.xch.servicecallback.content.group");
                    intent.putExtra("message", message);
                    checkLockAndShowNotification("来自群聊："+groupName,fromUserName+":"+content,R.drawable.group,username,"none",groupName,groupId);
                }
                sendBroadcast(intent);
            }

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                super.onOpen(handshakedata);
                System.out.println(Util.ws+"已连接");
                Log.e("JWebSocketClientService", "websocket连接成功");
            }
        };
        client.connect();
    }



    /**
     * 连接websocket
     */
    private void connect() {
        new Thread() {
            @Override
            public void run() {
                try {
                    //connectBlocking多出一个等待操作，会先连接再发送，否则未连接发送会报错
                    client.connectBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    /**
     * 发送消息
     *
     * @param msg
     */
    public void sendMsg(JSONObject msg) {
        if (null != client) {
            Log.e("JWebSocketClientService", "发送的消息：" + msg);
            client.send(msg.toString());
        }
    }

    /**
     * 断开连接
     */
    private void closeConnect() {
        try {
            if (null != client) {
                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client = null;
        }
    }


//    -----------------------------------消息通知--------------------------------------------------------

    /**
     * 检查锁屏状态，如果锁屏先点亮屏幕
     *
     * @param content
     */
    private void checkLockAndShowNotification(String title,String content,int icon,String userName,String toUserName,String groupName,Integer groupId) {
        //管理锁屏的一个服务
        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if (km.inKeyguardRestrictedInputMode()) {//锁屏
            //获取电源管理器对象
            PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            if (!pm.isScreenOn()) {
                @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
                wl.acquire();  //点亮屏幕
                wl.release();  //任务结束后释放
            }
            sendNotification(title,content,icon,userName,toUserName,groupName,groupId);
        } else {
            sendNotification(title,content,icon,userName,toUserName,groupName,groupId);
        }
    }

    /**
     * 发送通知
     *
     * @param content
     */
    private void sendNotification(String title,String content,int icon,String userName,String toUserName,String groupName,Integer groupId) {
//        Intent intent = new Intent();
//        intent.setClass(this, ChatActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notification = new NotificationCompat.Builder(this)
//                .setAutoCancel(true)
//                // 设置该通知优先级
//                .setPriority(Notification.PRIORITY_MAX)
//                .setSmallIcon(R.drawable.icon)
//                .setContentTitle("服务器")
//                .setContentText(content)
//                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//                .setWhen(System.currentTimeMillis())
//                // 向通知添加声音、闪灯和振动效果
//                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_ALL | Notification.DEFAULT_SOUND)
//                .setContentIntent(pendingIntent)
//                .build();
//        notifyManager.notify(1, notification);//id要保证唯一
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MyChannel";
            String description = "Channel description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channelId", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // 创建通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channelId")
                .setSmallIcon(R.drawable.contact_head_icon)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(icon);

        // 设置点击通知时的操作
        if(groupId==0){
            Intent intent = new Intent(this, ChatFriendActivity.class);
            intent.putExtra("userName",userName);
            intent.putExtra("toUserName",toUserName);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            builder.setContentIntent(pendingIntent);
        }else{
            Intent intent = new Intent(this, ChatGroupActivity.class);
            intent.putExtra("userName",userName);
            intent.putExtra("groupName",groupName);
            intent.putExtra("groupId",groupId.toString());
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            builder.setContentIntent(pendingIntent);
        }
        // 触发通知
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        int notificationId = 1; // 每个通知需要唯一的ID
        notificationManager.notify(notificationId, builder.build());
    }





}
