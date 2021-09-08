package com.example.demo.ao;

import lombok.Data;

@Data
public class LikeAo {
    private String token;
    private int productId;
    private boolean like;
}
