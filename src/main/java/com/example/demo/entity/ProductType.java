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

    public ProductType(String name, int status, Timestamp createTime, int createMan, Timestamp updateTime, int updateMan) {
        this.name = name;
        this.status = status;
        this.createTime = createTime;
        this.createMan = createMan;
        this.updateTime = updateTime;
        this.updateMan = updateMan;
    }
}
