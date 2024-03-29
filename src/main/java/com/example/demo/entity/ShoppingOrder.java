package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingOrder {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    private int buyingUserId;
    private int tradeStatus;
    private int payStatus;
    private Double orderAmount;
    private Double payAmount;
    private Timestamp payTime;
    private Timestamp completionTime;
    private Timestamp createTime;

    public ShoppingOrder(int buyingUserId, int tradeStatus, int payStatus, Double orderAmount, Double payAmount, Timestamp payTime, Timestamp completionTime, Timestamp createTime) {
        this.buyingUserId = buyingUserId;
        this.tradeStatus = tradeStatus;
        this.payStatus = payStatus;
        this.orderAmount = orderAmount;
        this.payAmount = payAmount;
        this.payTime = payTime;
        this.completionTime = completionTime;
        this.createTime = createTime;
    }
}
