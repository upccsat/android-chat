package com.ntu.treatment.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.ntu.treatment.pojo.Doctor;
import com.ntu.treatment.pojo.Patient;
import com.ntu.treatment.pojo.PatientHistory;
import com.ntu.treatment.pojo.User;
import com.ntu.treatment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
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
}
