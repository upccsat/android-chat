package com.ntu.treatment.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryGroup {
    Integer groupId;
    String fromUserName;
    String content;
    String sendTime;
}
