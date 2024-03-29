package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Chat {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    private int userId;
    private int anotherUserId;
    private int productId;

    public Chat(int userId, int anotherUserId, int productId) {
        this.userId = userId;
        this.anotherUserId = anotherUserId;
        this.productId = productId;
    }
}
