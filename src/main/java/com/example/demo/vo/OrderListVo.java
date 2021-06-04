package com.example.demo.vo;

import com.example.demo.entity.ExtendShoppingOrder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OrderListVo {
    private int code;
    private String message;
    private List<ExtendShoppingOrder> extendShoppingOrders;
}
