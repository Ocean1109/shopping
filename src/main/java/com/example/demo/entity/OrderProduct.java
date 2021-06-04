package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderProduct {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    private int orderId;
    private int productId;

    public OrderProduct(int orderId, int productId) {
        this.orderId = orderId;
        this.productId = productId;
    }
}
