package com.example.demo.entity;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class Product {
    private int id;
    private String productDesc;
    private String productImage;
    private Double productPrice;
    private int productTypeId;
    private int brandId;
    private int publishUserId;
    private String productAddress;
    private Timestamp createTime;
    private Timestamp updateTime;
    private int numbers;
    private int productRuleId;
    private String productRule;
}
