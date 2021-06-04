package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtendShoppingOrder {
    private int id;
    private int buyingUserId;
    private int tradeStatus;
    private int payStatus;
    private Double orderAmount;
    private Double payAmount;
    private Timestamp payTime;
    private Timestamp completionTime;
    private Timestamp createTime;
    private List<Product> productList;

    public ExtendShoppingOrder(int id, int buyingUserId, int tradeStatus, int payStatus, Double orderAmount, Double payAmount, Timestamp payTime, Timestamp completionTime, Timestamp createTime) {
        this.id = id;
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
