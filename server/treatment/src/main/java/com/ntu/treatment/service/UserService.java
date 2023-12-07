package com.ntu.treatment.service;

import com.ntu.treatment.pojo.Doctor;
import com.ntu.treatment.pojo.Patient;
import com.ntu.treatment.pojo.PatientHistory;
import com.ntu.treatment.pojo.User;
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

}
