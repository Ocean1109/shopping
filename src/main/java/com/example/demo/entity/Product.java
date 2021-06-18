package com.example.demo.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @TableId(value = "id", type = IdType.AUTO)
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
