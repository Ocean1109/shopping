package com.example.demo.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ChatDetail {
    private int id;
    private int chatId;
    private int userId;
    private String userName;
    private String content;
    private Timestamp createTime;
}
