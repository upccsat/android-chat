package com.ntu.treatment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ntu.treatment.adapter.Adapter_ChatMessage;
import com.ntu.treatment.im.JWebSocketClient;
import com.ntu.treatment.im.JWebSocketClientService;
import com.ntu.treatment.modle.ChatMessage;
import com.ntu.treatment.util.GetUrl;
import com.ntu.treatment.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class ChatFriendActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private ListView listView;
    private Button btn_send;
    private EditText et_content;
    private JWebSocketClientService.JWebSocketClientBinder binder;
    private String url;

    private String userName;
    private String toUserName;
    private List<ChatMessage> chatMessageList = new ArrayList<>();//消息列表
    private Adapter_ChatMessage adapter_chatMessage;
    private JWebSocketClientService jWebSClientService;
    private JWebSocketClient client;
    private ChatFriendActivity.ChatMessageReceiver chatMessageReceiver;
    private TextView toUserNameText;
    private class ChatMessageReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String message=intent.getStringExtra("message");
            Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(message);
            mContext=ChatFriendActivity.this;
            String fromUserName=jsonObject.getString("fromUserName");
            String content=jsonObject.getString("content");
            String toUserName1=jsonObject.getString("toUserName");
            Log.e("ChatFriendActivity","toUserName:"+toUserName);
            Log.e("ChatFriendActivity","toUserName1:"+toUserName1);
            Log.e("ChatFriendActivity","fromUserName:"+fromUserName);
            Log.e("ChatFriendActivity","isEqual"+(fromUserName.equals(toUserName)));
            if(fromUserName.equals(toUserName)){//这里的tousername是当前的联系人，tousername1是当前用户
                Log.e("ChatFriendActivity",toUserName);
                ChatMessage chatMessage=new ChatMessage();
                chatMessage.setContent(content);
                chatMessage.setFromUserName(fromUserName);
                chatMessage.setIsMeSend(0);
                chatMessage.setTime(System.currentTimeMillis()+"");
                chatMessageList.add(chatMessage);
            }
            initChatMsgListView();
        }
    }
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e("MainActivity", "服务与活动成功绑定");
            binder = (JWebSocketClientService.JWebSocketClientBinder) iBinder;
            jWebSClientService = binder.getService();
            client = jWebSClientService.client;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("ChatActivity", "服务与活动成功断开");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_friend);

        Intent intent=getIntent();
        userName=intent.getStringExtra("userName");
        toUserName=intent.getStringExtra("toUserName");

        toUserNameText=findViewById(R.id.tv_groupOrContactName);
        toUserNameText.setText(toUserName);
        Util util = new Util();
        util.setUsernameNameText(userName);

        mContext=ChatFriendActivity.this;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userNameNow",userName );
        params.put("userNameToShow",toUserName);
        url= GetUrl.url+"/user/getHistorySingle";
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String response=new String(responseBody);
                        System.out.println(response);
                        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response);
                        JSONArray friendsList=jsonObject.getJSONArray("history_single_list");
                        for(int i=0;i<friendsList.size();i++){
                            com.alibaba.fastjson.JSONObject friendObject=friendsList.getJSONObject(i);
                            String fromUserName=friendObject.getString("fromUserName");
                            String content=friendObject.getString("content");
                            String sendTime=friendObject.getString("sendTime");
                            ChatMessage chatMessage=new ChatMessage();
                            chatMessage.setTime(sendTime);
                            chatMessage.setContent(content);
                            chatMessage.setFromUserName(fromUserName);
                            if(fromUserName.equals(userName)){
                                chatMessage.setIsMeSend(1);
                            }else{
                                chatMessage.setIsMeSend(0);
                            }
                            chatMessageList.add(chatMessage);
                        }
                        initChatMsgListView();
                    }
                });
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(ChatFriendActivity.this, "Post请求失败！", Toast.LENGTH_SHORT).show();
            }
        });
        bindService();
        doRegisterReceiver();

        findViewById();
        initView();
    }
    private void doRegisterReceiver() {
        chatMessageReceiver = new ChatFriendActivity.ChatMessageReceiver();
        IntentFilter filter = new IntentFilter("com.xch.servicecallback.content");
        registerReceiver(chatMessageReceiver, filter);
    }
    private void findViewById() {
        listView = findViewById(R.id.chatmsg_listView);
        btn_send = findViewById(R.id.btn_send);
        et_content = findViewById(R.id.et_content);
        //iv_return_doctor = findViewById(R.id.iv_return_doctor);
        btn_send.setOnClickListener(this);
    }
    private void initView() {
        //监听输入框的变化
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et_content.getText().toString().length() > 0) {
                    btn_send.setVisibility(View.VISIBLE);
                } else {
                    btn_send.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    @Override
    public void onClick(View view) {
        LocalDateTime currentTime = null;
        // 定义日期时间格式
        DateTimeFormatter formatter = null;
        String formattedTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentTime = LocalDateTime.now();
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            formattedTime = currentTime.format(formatter);
        }
        // 将LocalDateTime转换为字符串
        switch (view.getId()) {
            case R.id.btn_send:
                String content = et_content.getText().toString();
                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("fromUserName",userName);
                    jsonObject.put("toUserName",toUserName);
                    jsonObject.put("content", content);
                    jsonObject.put("sendTime",formattedTime);
                    jsonObject.put("groupId",0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (content.length() <= 0) {
                    Util.showToast(mContext, "消息不能为空哟");
                    return;
                }

                if (client != null && client.isOpen()) {
                    jWebSClientService.sendMsg(jsonObject);

                    //暂时将发送的消息加入消息列表，实际以发送成功为准（也就是服务器返回你发的消息时）
                    ChatMessage chatMessage=new ChatMessage();
                    chatMessage.setContent(content);
                    chatMessage.setIsMeSend(1);

                    chatMessage.setTime(System.currentTimeMillis()+"");
                    chatMessageList.add(chatMessage);
                    initChatMsgListView();
                    et_content.setText("");
                } else if(client==null){
                    Util.showToast(mContext, "连接已断开，请稍等或重启App哟");
                }else if(!client.isOpen()){
                    Util.showToast(mContext, "连接已断开，请稍等或重启App哟1");
                }
                break;
            default:
                break;
        }
    }
    private void initChatMsgListView(){
        adapter_chatMessage = new Adapter_ChatMessage(mContext, chatMessageList);
        listView.setAdapter(adapter_chatMessage);
        listView.setSelection(chatMessageList.size());
    }
    private void bindService() {
        Intent bindIntent = new Intent(mContext, JWebSocketClientService.class);
        bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
    }
}