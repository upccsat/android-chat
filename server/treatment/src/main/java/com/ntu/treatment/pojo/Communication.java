package com.ntu.treatment.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.net.Inet4Address;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Communication implements Serializable {
    private Integer isMeSend;
    private String fromUserName;
    private String content;
    private String sendTime;
}
