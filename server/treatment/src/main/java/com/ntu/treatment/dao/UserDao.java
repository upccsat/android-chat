package com.ntu.treatment.dao;

import com.ntu.treatment.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
    List<Group> getAllGroups(String username);
    Integer createGroup(Group group);
    Integer addGroupMember(String username,String currentGroupId);
    Integer getGroupId(String groupName,String createTime);
    List<String> getUserNameFromGroup(Integer groupId);
    List<HistoryGroup> getHistoryGroup(String userNameNow,Integer groupIdToShow);
    Integer addHistoryGroup(HistoryGroup historyGroup);
    //一对一模块
    List<String> getAllFriendsFrom1(String username);
    List<String> getAllFriendsFrom2(String username);
    String findImageByUserName(String username);
    List<HistroySingle> getHistorySingle(String userNameNow,String userNameToShow);
    Integer addHistorySingle(HistroySingle histroySingle);
    Integer addFriend(String userNameNow,String userNameToAdd);

}
