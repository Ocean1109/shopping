package com.example.demo.ao;

import lombok.Data;

@Data
public class ShoppingCartAo {
    private int operate;
    /**0:添加
     * 1：删除*/
    private int productId;
    private int userId;
    private int num;
}
