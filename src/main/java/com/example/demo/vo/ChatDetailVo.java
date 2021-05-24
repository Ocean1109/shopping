package com.example.demo.vo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ChatDetailVo {
    private int userId;
    private String userName;
    private String content;
    private Timestamp createTime;
}
