package com.ntu.treatment.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ntu.treatment.pojo.*;
import com.ntu.treatment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
        List<Integer> groupIds=userService.getAllGroupsId(username);
        List<Group> list = userService.getGroups(groupIds);
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
    public String addGroupMember(@RequestParam("usernames") ArrayList<String> usernames){//usernames的最后一位是groupId
        System.out.println("123");
        for(int i=0;i< usernames.size();i++){
            usernames.set(i, usernames.get(i).replace("\"","").replace("[","").replace("]",""));
            System.out.println("当前："+usernames.get(i));
            System.out.println(usernames.get(i) instanceof String);
        }
        System.out.println(usernames);
        Boolean flag=userService.addGroupMember(usernames);
        if(flag){
            return "true";
        }else{
            return "false";
        }
    }
    @RequestMapping("/getGroupId")
    public Integer getGroupId(String groupName,String owner){
        System.out.println("111");
        return userService.getGroupId(groupName,owner);
    }
    @RequestMapping("/getHistoryGroup")
    public JSONObject findHistoryGroup(String groupIdStr){
        Integer groupId=Integer.parseInt(groupIdStr);
        List<HistoryGroup> list=userService.getHistoryGroup(groupId);
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(list));
        jsonObject.put("history_group_list", jsonArray.toString());
        System.out.println(jsonArray.toString());
        return jsonObject;
    }
    @RequestMapping("/getFriends")
    public JSONObject findAllFriends(String username){
        List<Friend> list = userService.getAllFriends(username);//返回friends的名字和头像的地址
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(list));
        jsonObject.put("friends_list", jsonArray.toString());
        System.out.println(jsonObject.toString());
        return jsonObject;

    }
    @RequestMapping("/getHistorySingle")
    public JSONObject findHistorySingle(String userNameNow,String userNameToShow){
        List<HistorySingle> list=userService.getHistorySingle(userNameNow,userNameToShow);
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(list));
        jsonObject.put("history_single_list", jsonArray.toString());
        System.out.println(jsonArray.toString());
        return jsonObject;
    }
    @RequestMapping("/addFriend")
    public String addFriend(String fromUserName,String toUserName){
        System.out.println("addFriend");
        Boolean flag=userService.addFriend(fromUserName,toUserName);
        if(flag){
            return "true";
        }else{
            return "false";
        }
    }
    @RequestMapping("/addFriendInvitation")
    public String addFriendInvitation(String fromUserName,String toUserName){
        System.out.println("addFriendInvitation");
        FriendInvitation friendInvitation=new FriendInvitation(fromUserName,toUserName,0);
        Boolean flag=userService.addFriendInvitation(friendInvitation);
        if(flag){
            return "true";
        }else{
            return "false";
        }
    }
    @RequestMapping("/changeFriendInvitationStatus")
    public String changeFriendInvitationStatus(String fromUserName,String toUserName){
        System.out.println("changeFriendInvitationStatus");
        Boolean flag=userService.changeFriendInvitationStatus(fromUserName,toUserName);
        if(flag){
            Boolean flag1=userService.addFriend(fromUserName,toUserName);
            if(flag1){
                return "true";
            }
            return "false";
        }else{
            return "false";
        }
    }
    @RequestMapping("/getAllFriendInvitation")
    public JSONObject getAllFriendInvitation(String userName){
        List<FriendInvitation> list=userService.getAllFriendInvitation(userName);
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(list));
        jsonObject.put("friend_invitation_list", jsonArray.toString());
        System.out.println(jsonObject.toString());
        return jsonObject;
    }
}
