package com.example.demo.vo;

import com.example.demo.entity.Order4Shopkeeper;
import lombok.Data;

import java.util.List;

@Data
public class OrderList4ShopkeeperVo {
    private int code;
    private String message;
    public List<Order4Shopkeeper> order4Shopkeepers;
}


