package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    private int userId;
    private int productId;
    private int productNumber;

    public ShoppingCart(int userId, int productId, int productNumber) {
        this.userId = userId;
        this.productId = productId;
        this.productNumber = productNumber;
    }
}
