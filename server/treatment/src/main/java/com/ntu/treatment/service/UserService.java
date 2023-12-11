package com.ntu.treatment.service;

import com.alibaba.fastjson.JSONObject;
import com.ntu.treatment.pojo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 吼吼权
 * @version 1.0
 * @date 2021/3/9 21:20
 */
public interface UserService {

    Boolean checkLogin(String username,String password);

    Boolean register(User user);

    //群聊模块

    List<Group> getAllGroups(String username);

    Boolean createGroup(Group group);
    Boolean addGroupMember(String username,String currentGroupId);

    List<String> getUserNameFromGroup(Integer groupId);
    List<HistoryGroup> getHistoryGroup(String userNameNow,Integer groupId);
    Boolean addHistoryGroup(HistoryGroup historyGroup);

    //一对一模块
    List<Friend> getAllFriends(String userName);

    List<HistroySingle> getHistorySingle(String userNameNow,String userNameToShow);

    Boolean addHistorySingle(HistroySingle histroySingle);

    Boolean addFriend(String userNameNow,String userNameToAdd);
}
