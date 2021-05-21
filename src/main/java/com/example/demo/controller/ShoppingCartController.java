package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.annotation.UserLoginToken;
import com.example.demo.ao.ShoppingCartAo;
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

import java.util.List;

@Controller
public class ShoppingCartController {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private ProductMapper productMapper;

    @PostMapping("/ShoppingCart")
    @ResponseBody
    @UserLoginToken
    public ProductCartVo ProductCartControlling(@RequestBody ShoppingCartAo product){
        ProductCartVo result = new ProductCartVo();

        if(product.getOperate() == 0){
            result = addProduct(product);
        }
        else{
            result = delProduct(product);
        }

        return result;
    }

    /**添加商品*/
    public ProductCartVo addProduct(ShoppingCartAo product){
        ProductCartVo result = new ProductCartVo();
        QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = Wrappers.query();
        shoppingCartQueryWrapper.eq("product_id", product.getProductId());
        ShoppingCart queryCart = shoppingCartMapper.selectOne(shoppingCartQueryWrapper);

        if(queryCart!=null){//购物车某个商品数量增加
            ShoppingCart newCart = new ShoppingCart(queryCart.getId(), queryCart.getUserId(), queryCart.getProductId(), queryCart.getProductNumber() + product.getNum());
            shoppingCartMapper.updateById(newCart);

            result.setSuccess(true);
            result.setMessage("已添加至购物车");
            result = addShoppingCartList(product, result);
        }
        else{//添加新商品
            QueryWrapper<Product> productQueryWrapper = Wrappers.query();
            productQueryWrapper.eq("id", product.getProductId());
            Product queryProduct = productMapper.selectOne(productQueryWrapper);

            if(queryProduct==null){
                result.setSuccess(false);
                result.setMessage("没有该商品");
                result = addShoppingCartList(product, result);
                return result;
            }

            ShoppingCart newCart = new ShoppingCart(product.getUserId(), queryProduct.getId(), product.getNum());
            shoppingCartMapper.insert(newCart);

            result.setSuccess(true);
            result.setMessage("已增加商品");
            result = addShoppingCartList(product, result);
        }


        return result;
    }

    /**删除商品*/
    public ProductCartVo delProduct(ShoppingCartAo product){
        ProductCartVo result = new ProductCartVo();
        QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = Wrappers.query();
        shoppingCartQueryWrapper.eq("product_id", product.getProductId());
        ShoppingCart queryCart = shoppingCartMapper.selectOne(shoppingCartQueryWrapper);

        int num = product.getNum();

        if(queryCart==null){//没有该商品
            result.setSuccess(false);
            result.setMessage("没有该商品");
        }
        else if(queryCart.getProductNumber() <= num){//商品数量小于要删除的数量
            result.setSuccess(true);
            result.setMessage("删除成功");
            shoppingCartMapper.deleteById(product.getProductId());
        }
        else{
            ShoppingCart newCart = new ShoppingCart(queryCart.getId(), queryCart.getUserId(), queryCart.getProductId(), queryCart.getProductNumber() - product.getNum());
            shoppingCartMapper.updateById(newCart);
            result.setSuccess(true);
            result.setMessage("删除成功");
        }

        result = addShoppingCartList(product, result);
        return result;
    }

    /**列出商品列表*/
    public ProductCartVo addShoppingCartList(ShoppingCartAo product, ProductCartVo productCartVo){
        QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = Wrappers.query();
        shoppingCartQueryWrapper.eq("user_id", product.getUserId());
        List<ShoppingCart> queryCart = shoppingCartMapper.selectList(shoppingCartQueryWrapper);

        productCartVo.setShoppingCartList(queryCart);

        return productCartVo;
    }





}
