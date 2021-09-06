package com.example.demo.service;

import com.example.demo.ao.ShoppingCartAo;
import com.example.demo.entity.ShoppingCartListInfo;
import com.example.demo.vo.ProductCartVo;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface ShoppingCartService {
    @Async
    public ProductCartVo ProductCartControlling(ShoppingCartAo product);
    @Async
    public ProductCartVo addProduct(ShoppingCartAo product);
    @Async
    public ProductCartVo delProduct(ShoppingCartAo product);
    public ProductCartVo addShoppingCartList(ShoppingCartAo product, ProductCartVo productCartVo);
    public List<ShoppingCartListInfo> addShoppingCartList(String token);
}
