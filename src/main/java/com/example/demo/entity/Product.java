package com.example.demo.entity;


import lombok.Data;

import java.sql.Date;

@Data
public class Product {
    private int id;
    private String productDesc;
    private String productImage;
    private String productPrice;
    private int productTypeId;
    private int productUserId;
    private String productAddress;
    private Date createTime;
    private Date updateTime;
}
