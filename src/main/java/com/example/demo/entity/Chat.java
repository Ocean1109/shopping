package com.example.demo.entity;

import lombok.Data;

@Data
public class Chat {
    private int id;
    private int userId;
    private int anotherUserId;
    private int productId;
}
