package com.ntu.treatment.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ntu.treatment.pojo.HistoryGroup;
import com.ntu.treatment.pojo.HistorySingle;
import com.ntu.treatment.service.Impl.UserServiceImpl;
import com.ntu.treatment.utils.SpringUtil;
import javax.websocket.Session;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 吼吼权
 * @version 1.0
 * @date 2021/3/11 15:57
 */
public class SessionPool {

    public static Map<String, Session> sessions = new ConcurrentHashMap<>();

    public static void close(String sessionId) throws IOException {
        for (String userId : SessionPool.sessions.keySet()) {
            Session session = SessionPool.sessions.get(userId);
            if (session.getId().equals(sessionId)){
                sessions.remove(userId);
                break;
            }
        }

    }

    public static void sendMessage(String sessionId, String message) {
        sessions.get(sessionId).getAsyncRemote().sendText(message);
    }

    public static void sendMessage(String message) {
        JSONObject jsonObject = JSON.parseObject(message);

        String fromUserName = jsonObject.getString("fromUserName");
        String toUserName=jsonObject.getString("toUserName");
        String content = jsonObject.getString("content");
        String sendTime = jsonObject.getString("sendTime");
        String groupName=jsonObject.getString("groupName");
        Integer groupId=Integer.parseInt(jsonObject.getString("groupId"));

        JSONObject jsonObject1=new JSONObject();
        jsonObject1.put("fromUserName",fromUserName);
        jsonObject1.put("toUserName",toUserName);
        jsonObject1.put("content",content);
        jsonObject1.put("sendTime",sendTime);
        jsonObject1.put("groupName",groupName);
        jsonObject1.put("groupId",groupId.toString());

        UserServiceImpl userService = (UserServiceImpl) SpringUtil.getBean(UserServiceImpl.class);
        if(groupId==0&&!toUserName.equals("none")){
            HistorySingle historySingle =new HistorySingle(fromUserName,toUserName,content,sendTime);
            Session session = sessions.get(toUserName);
            if (session != null){
                session.getAsyncRemote().sendText(jsonObject1.toString());
                userService.addHistorySingle(historySingle);
            }else{
                userService.addHistorySingle(historySingle);
            }
        }else if(groupId!=0&&toUserName.equals("none")){
            HistoryGroup historyGroup=new HistoryGroup(groupId,fromUserName,content,sendTime);
            List<String> usernames=userService.getUserNameFromGroup(groupId);
            for(String username:usernames){
                Session session=sessions.get(username);
                if(session!=null&&!username.equals(fromUserName)){
                    session.getAsyncRemote().sendText(jsonObject1.toString());
                }
            }
            System.out.println(123);
            userService.addHistoryGroup(historyGroup);
        }
    }
}
