package com.ntu.treatment.service.Impl;


import com.alibaba.fastjson.JSONObject;
import com.ntu.treatment.dao.UserDao;
import com.ntu.treatment.pojo.*;
import com.ntu.treatment.service.UserService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        System.out.println(username+"***service**"+password);
        System.out.println("返回的"+user.getPassword());
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
    public List<HistroySingle> getHistorySingle(String userNameNow,String userNameToShow){
        return userDao.getHistorySingle(userNameNow,userNameToShow);
    }
    @Override
    public Boolean addHistorySingle(HistroySingle histroySingle){
        Integer flag=userDao.addHistorySingle(histroySingle);
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

    //群聊模块
    @Override
    public List<Group> getAllGroups(String username){
        return userDao.getAllGroups(username);
    }

    @Override
    public Boolean createGroup(Group group){
        Boolean flag=(userDao.createGroup(group)!=null&&userDao.createGroup(group)==1?true:false);
        return flag;
    }


}
