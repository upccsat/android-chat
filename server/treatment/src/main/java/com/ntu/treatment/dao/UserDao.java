package com.ntu.treatment.dao;

import com.ntu.treatment.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 吼吼权
 * @version 1.0
 * @date 2021/3/9 21:12
 */
@Mapper
@Repository
public interface UserDao {

    User findByName(String username);



    Integer register(User user);

    //群聊模块
    List<Integer> getAllGroupsId(String username);
    Group getGroup(Integer groupId);
    Integer createGroup(Group group);
    Integer addGroupMember(String username,Integer currentGroupId);
    Integer getGroupId(String groupName,String owner);
    List<String> getUserNameFromGroup(Integer groupId);
    List<HistoryGroup> getHistoryGroup(Integer groupIdTo);
    Integer addHistoryGroup(HistoryGroup historyGroup);
    //一对一模块
    List<String> getAllFriendsFrom1(String username);
    List<String> getAllFriendsFrom2(String username);
    String findImageByUserName(String username);
    List<HistorySingle> getHistorySingle(String userNameNow, String userNameToShow);
    Integer addHistorySingle(HistorySingle historySingle);
    Integer addFriend(String userNameNow,String userNameToAdd);
    Integer addFriendInvitation(FriendInvitation friendInvitation);
    Integer changeFriendInvitationStatus(String fromUserName,String toUserName);
    List<FriendInvitation> getAllFriendInvitation(String userName);



    User getUserInfo(String userName);
    Integer updateUserInfo(User user);

}
