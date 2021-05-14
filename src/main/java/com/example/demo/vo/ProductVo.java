package com.example.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductVo {
    private int id;
    private String productDesc;
    private String productImage;
    private Double productPrice;
}
