package com.ntu.treatment.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ntu.treatment.pojo.HistroySingle;
import com.ntu.treatment.service.Impl.UserServiceImpl;
import com.ntu.treatment.utils.SpringUtil;
import javax.websocket.Session;
import java.io.IOException;
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
        HistroySingle histroySingle=new HistroySingle(fromUserName,toUserName,content,sendTime);

        UserServiceImpl userService = (UserServiceImpl) SpringUtil.getBean(UserServiceImpl.class);
        Session session = sessions.get(toUserName);
        if (session != null){
            session.getAsyncRemote().sendText(content);
            userService.addHistorySingle(histroySingle);
        }else{
            userService.addHistorySingle(histroySingle);
        }
    }
}
