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
public class Praise {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    private int productId;
    private int userId;
    private String userName;
    private int status;
    private Timestamp praiseTime;

    public Praise(int productId, int userId, String userName, int status, Timestamp praiseTime) {
        this.productId = productId;
        this.userId = userId;
        this.userName = userName;
        this.status = status;
        this.praiseTime = praiseTime;
    }
}
