package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderProduct {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    private int orderId;
    private int productId;
    private boolean sentProduct;
    private int shopkeeperId;
    private boolean isFinished;
    private int num;

    public OrderProduct(int orderId, int productId, boolean sentProduct, int shopkeeperId, boolean isFinished, int num) {
        this.orderId = orderId;
        this.productId = productId;
        this.sentProduct = sentProduct;
        this.shopkeeperId = shopkeeperId;
        this.isFinished = isFinished;
        this.num = num;
    }
}
