package com.example.demo.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ProductType {
    private int id;
    private String name;
    private int status;
    private Timestamp createTime;
    private int createMan;
    private Timestamp updateTime;
    private int updateMan;
}
