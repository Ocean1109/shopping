package com.example.demo.ao;

import lombok.Data;

import java.util.List;

@Data
public class OrderAo {
    private String token;
    private List<Integer> productIds;
    private List<Integer> num;
}
