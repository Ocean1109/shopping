package com.example.demo.entity;

import lombok.Data;

@Data
public class OrderProduct {
    private int id;
    private int orderId;
    private int productId;
}
