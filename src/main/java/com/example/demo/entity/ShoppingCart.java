package com.example.demo.entity;

import lombok.Data;

@Data
public class ShoppingCart {
    private int id;
    private int userId;
    private int productId;
    private int productNumber;
}
