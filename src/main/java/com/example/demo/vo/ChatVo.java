package com.example.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatVo {
    private int id;
    private int userId;
    private int anotherUserId;
    private int productId;
}
