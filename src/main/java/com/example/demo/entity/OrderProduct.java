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
    private boolean sentProduct;
    private int shopkeeperId;
    private boolean isFinished;

    public OrderProduct(int orderId, int productId, boolean sentProduct, int shopkeeperId, boolean isFinished) {
        this.orderId = orderId;
        this.productId = productId;
        this.sentProduct = sentProduct;
        this.shopkeeperId = shopkeeperId;
        this.isFinished = isFinished;
    }
}
