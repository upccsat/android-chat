package com.ntu.treatment.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.ntu.treatment.pojo.*;
import com.ntu.treatment.service.UserService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * @author 吼吼权
 * @version 1.0
 * @date 2021/3/9 21:11
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public String checkLogin(User user){
        System.out.println(user.getUsername());
        System.out.println("密码："+user.getPassword());
        Boolean flag = userService.checkLogin(user.getUsername(),user.getPassword());
        if (flag){
            return "true";
        }else {
            return "false";
        }
    }
    @RequestMapping("/register")
    public String registerDoctor(User user){
        Boolean flag = userService.register(user);
        if (flag){
            return "true";
        }else {
            return "false";
        }
    }
    @RequestMapping("/getGroups")
    public JSONObject findAllGroups(String username){
        List<Group> list = userService.getAllGroups(username);
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(list));
        jsonObject.put("groups_list", jsonArray.toString());
        System.out.println(jsonArray.toString());
        return jsonObject;
    }
    @RequestMapping("/createGroup")
    public String createGroup(Group group){
        LocalDateTime currentTime = LocalDateTime.now();
        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 将LocalDateTime转换为字符串
        String formattedTime = currentTime.format(formatter);
        group.setCreateTime(formattedTime);
        Boolean flag=userService.createGroup(group);
        if(flag){
            return "true";
        }else{
            return "false";
        }
    }
    @RequestMapping("/addGroupMember")
    public String addGroupMember(String username,String groupId){
        Boolean flag=userService.addGroupMember(username,groupId);
        if(flag){
            return "true";
        }else{
            return "false";
        }
    }
    @RequestMapping("/getFriends")
    public JSONObject findAllFriends(String username){
        List<Friend> list = userService.getAllFriends(username);//返回friends的名字和头像的地址
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(list));
        jsonObject.put("friends_list", jsonArray.toString());
        System.out.println(jsonArray.toString());
        return jsonObject;
    }
    @RequestMapping("/getHistorySingle")
    public JSONObject findHistorySingle(String userNameNow,String userNameToShow){
        List<HistroySingle> list=userService.getHistorySingle(userNameNow,userNameToShow);
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(list));
        jsonObject.put("history_single_list", jsonArray.toString());
        System.out.println(jsonArray.toString());
        return jsonObject;
    }
    @RequestMapping("/addFriend")
    public String addFriend(String userNameNow,String userNameToAdd){
        Boolean flag=userService.addFriend(userNameNow,userNameToAdd);
        if(flag){
            return "true";
        }else{
            return "false";
        }
    }
}
