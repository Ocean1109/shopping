package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.annotation.UserLoginToken;
import com.example.demo.ao.ShoppingCartAo;
import com.example.demo.entity.Product;
import com.example.demo.entity.ShoppingCart;
import com.example.demo.entity.ShoppingCartListInfo;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.ShoppingCartMapper;
import com.example.demo.service.ShoppingCartService;
import com.example.demo.vo.ProductCartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * @param product
     * @return
     */
    @PostMapping("/ShoppingCart")
    @ResponseBody
    public ProductCartVo ProductCartControlling(@RequestBody ShoppingCartAo product){
        return shoppingCartService.ProductCartControlling(product);
    }

    @PostMapping("ShowShoppingCart")
    @ResponseBody
    public List<ShoppingCartListInfo> addShoppingCartList(@RequestBody String token){
        return shoppingCartService.addShoppingCartList(token);
    }

}