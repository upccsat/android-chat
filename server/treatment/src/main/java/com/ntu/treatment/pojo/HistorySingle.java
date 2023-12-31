package com.ntu.treatment.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistorySingle {
    private String fromUserName;
    private String toUserName;
    private String content;
    private String sendTime;
}
