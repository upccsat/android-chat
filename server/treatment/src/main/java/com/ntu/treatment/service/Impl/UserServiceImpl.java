package com.ntu.treatment.service.Impl;


import com.ntu.treatment.dao.UserDao;
import com.ntu.treatment.pojo.Doctor;
import com.ntu.treatment.pojo.Patient;
import com.ntu.treatment.pojo.PatientHistory;
import com.ntu.treatment.pojo.User;
import com.ntu.treatment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
