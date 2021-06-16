package com.example.demo.ao;

import lombok.Data;

import java.util.List;

@Data
public class OrderAo {
    private String token;
    private int shopkeeperId;
    private List<Integer> productIds;
}
