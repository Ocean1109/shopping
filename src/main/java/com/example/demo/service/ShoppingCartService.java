package com.example.demo.service;

import com.example.demo.ao.ShoppingCartAo;
import com.example.demo.vo.ProductCartVo;

public interface ShoppingCartService {
    public ProductCartVo ProductCartControlling(ShoppingCartAo product);
    public ProductCartVo addProduct(ShoppingCartAo product);
    public ProductCartVo delProduct(ShoppingCartAo product);
    public ProductCartVo addShoppingCartList(ShoppingCartAo product, ProductCartVo productCartVo);
}
