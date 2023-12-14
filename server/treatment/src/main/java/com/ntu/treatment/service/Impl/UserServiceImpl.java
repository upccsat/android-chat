package com.ntu.treatment.service.Impl;


import com.ntu.treatment.dao.UserDao;
import com.ntu.treatment.pojo.*;
import com.ntu.treatment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 吼吼权
 * @version 1.0
 * @date 2021/3/9 21:20
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    public Boolean checkLogin(String username,String password){
        User user = userDao.findByName(username);
        if (user.getPassword().equals(password)){
            System.out.println("登陆成功");
            return true;
        }else {
            return false;
        }
    }

    public Boolean register(User user){
        int flag = userDao.register(user);
        if (flag == 1){
            return true;
        }
        else{
            return false;
        }
    }

    //一对一模块
    //找到某用户所有的朋友
    @Override
    public List<Friend> getAllFriends(String username){
        List<String> l1=userDao.getAllFriendsFrom1(username);
        List<String> l2=userDao.getAllFriendsFrom2(username);
        l1.addAll(l2);
        List<Friend> result=new ArrayList<>();
        for(String s:l1){
            String image=userDao.findImageByUserName(s);
            Friend friend=new Friend(s,image);
            result.add(friend);
        }
        return result;
    }
    @Override
    public List<HistorySingle> getHistorySingle(String userNameNow, String userNameToShow){
        return userDao.getHistorySingle(userNameNow,userNameToShow);
    }
    @Override
    public Boolean addHistorySingle(HistorySingle historySingle){
        Integer flag=userDao.addHistorySingle(historySingle);
        if(flag==1){
            return true;
        }else{
            return false;
        }
    }
    @Override
    public Boolean addFriend(String userNameNow,String userNameToAdd){
        Integer flag=userDao.addFriend(userNameNow,userNameToAdd);
        if(flag==1){
            return true;
        }else{
            return false;
        }
    }
    public Boolean addFriendInvitation(FriendInvitation friendInvitation){
        Integer flag=userDao.addFriendInvitation(friendInvitation);
        if(flag==1){
            return true;
        }else{
            return false;
        }
    }
    public Boolean changeFriendInvitationStatus(String fromUserName,String toUserName){
        Integer flag=userDao.changeFriendInvitationStatus(fromUserName,toUserName);
        if(flag>0){
            return true;
        }else{
            return false;
        }
    }
    public List<FriendInvitation> getAllFriendInvitation(String userName){
        List<FriendInvitation> result=userDao.getAllFriendInvitation(userName);
        return result;
    }

    //群聊模块
    @Override
    public List<Integer> getAllGroupsId(String username){
        return userDao.getAllGroupsId(username);
    }
    @Override
    public List<Group> getGroups(List<Integer> groupIds){
        List<Group> result=new ArrayList<>();
        for(int i=0;i<groupIds.size();i++){
            Group group=userDao.getGroup(groupIds.get(i));
            result.add(group);
        }
        return result;
    }
    @Override
    public Boolean createGroup(Group group){
        Boolean flag=(userDao.createGroup(group)==1?true:false);
        return flag;
    }
    public Integer getGroupId(String groupName,String owner){
        Integer result=userDao.getGroupId(groupName,owner);
        return result;
    }
    public Boolean addGroupMember(ArrayList<String> usernames){
        Integer currentGroupId=Integer.parseInt(usernames.get(usernames.size()-1));
        Integer sum=0;
        for(int i=0;i<usernames.size()-1;i++){
            Integer flag=userDao.addGroupMember(usernames.get(i),currentGroupId);
            sum+=flag;
        }
        if(sum==usernames.size()-1){
            return true;
        }else{
            return false;
        }
    }
    public  List<String> getUserNameFromGroup(Integer groupId){
        List<String> result=userDao.getUserNameFromGroup(groupId);
        return result;
    }
    public List<HistoryGroup> getHistoryGroup(String userNameNow,Integer groupId){
        List<HistoryGroup> result=userDao.getHistoryGroup(userNameNow,groupId);
        return result;
    }
    public Boolean addHistoryGroup(HistoryGroup historyGroup){
        Boolean flag=(userDao.addHistoryGroup(historyGroup)==1?true:false);
        return flag;
    }


}
