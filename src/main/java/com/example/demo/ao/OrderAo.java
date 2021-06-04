package com.example.demo.ao;

import lombok.Data;

import java.util.List;

@Data
public class OrderAo {
    private int userId;
    private List<Integer> productIds;
}
