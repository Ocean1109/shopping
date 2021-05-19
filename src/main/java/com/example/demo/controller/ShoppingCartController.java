package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.ao.AddProductAo;
import com.example.demo.entity.Product;
import com.example.demo.entity.ShoppingCart;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.ShoppingCartMapper;
import com.example.demo.vo.ProductCartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ShoppingCartController {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private ProductMapper productMapper;

    @PostMapping("/ShoppingCart")
    @ResponseBody
    public ProductCartVo addProduct(@RequestBody AddProductAo product){
        ProductCartVo result = new ProductCartVo();
        QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = Wrappers.query();
        shoppingCartQueryWrapper.eq("id", product.getId());
        ShoppingCart queryCart = shoppingCartMapper.selectOne(shoppingCartQueryWrapper);

        if(queryCart!=null){//购物车某个商品数量加一
            ShoppingCart newCart = new ShoppingCart(queryCart.getId(), queryCart.getUserId(), queryCart.getProductId(), queryCart.getProductNumber() + 1);
            shoppingCartMapper.updateById(newCart);

            result.setSuccess(true);
        }
        else{//添加新商品
            QueryWrapper<Product> productQueryWrapper = Wrappers.query();
            productQueryWrapper.eq("id", product.getId());
            Product queryProduct = productMapper.selectOne(productQueryWrapper);

            ShoppingCart newCart = new ShoppingCart(product.getUserId(), queryProduct.getId(), 1);
            shoppingCartMapper.insert(newCart);

            result.setSuccess(true);
        }


        return result;
    }

    public ProductCartVo delProduct(@RequestBody AddProductAo product){
        ProductCartVo result = null;
        QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = Wrappers.query();
        shoppingCartQueryWrapper.eq("id", product.getId());
        ShoppingCart queryCart = shoppingCartMapper.selectOne(shoppingCartQueryWrapper);

        if(queryCart==null){//没有该商品
            
        }
        else{
            shoppingCartMapper.deleteById(product.getId());
        }

        return result;
    }



}
