package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
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
}
