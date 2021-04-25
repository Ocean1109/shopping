package com.example.demo.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Praise {
    private int id;
    private int productId;
    private int userId;
    private String userName;
    private int status;
    private Timestamp praiseTime;
}
