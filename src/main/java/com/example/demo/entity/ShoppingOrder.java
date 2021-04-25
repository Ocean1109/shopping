package com.example.demo.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ShoppingOrder {
    private int id;
    private int buyingUserId;
    private int tradeStatus;
    private int payStatus;
    private Double orderAmount;
    private Double payAmount;
    private Timestamp payTime;
    private Timestamp completionTime;
    private Timestamp createTime;
}
