package com.ntu.treatment.service;

import com.ntu.treatment.pojo.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 吼吼权
 * @version 1.0
 * @date 2021/3/9 21:20
 */
public interface UserService {

    Boolean checkLogin(String username,String password);

    Boolean register(User user);

    User getUserInfo(String userName);

    Boolean updateUserInfo(User user);

    //群聊模块
    List<Integer> getAllGroupsId(String username);
    List<Group> getGroups(List<Integer> groupIds);
    Boolean createGroup(Group group);
    Boolean addGroupMember(ArrayList<String> usernames);

    Integer getGroupId(String groupName,String owner);

    List<String> getUserNameFromGroup(Integer groupId);
    List<HistoryGroup> getHistoryGroup(Integer groupId);
    Boolean addHistoryGroup(HistoryGroup historyGroup);


    //一对一模块
    List<Friend> getAllFriends(String userName);

    List<HistorySingle> getHistorySingle(String userNameNow, String userNameToShow);

    Boolean addHistorySingle(HistorySingle historySingle);

    Boolean addFriend(String userNameNow,String userNameToAdd);

    Boolean addFriendInvitation(FriendInvitation friendInvitation);
    Boolean changeFriendInvitationStatus(String fromUserName,String toUserName);
    List<FriendInvitation> getAllFriendInvitation(String userName);
}
