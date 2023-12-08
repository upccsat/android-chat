package com.ntu.treatment.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group implements Serializable {
    private String groupName;
    private String owner;
    private String createTime;
}
