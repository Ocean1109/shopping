package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order4Shopkeeper {
    private int id;
    private int productId;
    private String image;
    private String desc;
    private double price;
    private int buyingUserId;
    private String address;
    private String name;
    private String tel;
    private int tradeStatus;
}
