package com.example.demo.ao;

import lombok.Data;

@Data
public class LikeAo {
    private int userId;
    private int productId;
    private boolean like;
}
