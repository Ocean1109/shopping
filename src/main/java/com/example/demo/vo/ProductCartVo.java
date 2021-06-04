package com.example.demo.vo;

import com.example.demo.entity.ShoppingCartListInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCartVo {
    private boolean success;
    private String message;
    private List<ShoppingCartListInfo> shoppingCartListInfos;
}
