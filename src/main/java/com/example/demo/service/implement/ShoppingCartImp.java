package com.example.demo.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.annotation.UserLoginToken;
import com.example.demo.ao.ShoppingCartAo;
import com.example.demo.entity.*;
import com.example.demo.mapper.OrderProductMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.ShoppingCartMapper;
import com.example.demo.mapper.ShoppingOrderMapper;
import com.example.demo.service.ShoppingCartService;
import com.example.demo.service.TokenService;
import com.example.demo.vo.ProductCartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShoppingCartImp implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private TokenService tokenService;

    public ProductCartVo ProductCartControlling(ShoppingCartAo product){
        ProductCartVo result;

        if(product.getOperate() == 0){
            result = addProduct(product);
        }
        else{
            result = delProduct(product);
        }

        return result;
    }

    /**添加商品*/
    public ProductCartVo addProduct(ShoppingCartAo shoppingCartAo){
        ProductCartVo result = new ProductCartVo();
        QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = Wrappers.query();
        shoppingCartQueryWrapper.eq("user_id", Integer.parseInt(tokenService.getUseridFromToken(shoppingCartAo.getToken()))).eq("product_id", shoppingCartAo.getProductId());
        ShoppingCart queryCart = shoppingCartMapper.selectOne(shoppingCartQueryWrapper);

        if(queryCart!=null){//购物车某个商品数量增加
            ShoppingCart newCart = new ShoppingCart(queryCart.getId(), queryCart.getUserId(), queryCart.getProductId(), queryCart.getProductNumber() + shoppingCartAo.getNum(), 0);
            shoppingCartMapper.updateById(newCart);

            result.setSuccess(true);
            result.setMessage("已添加至购物车");
            result = addShoppingCartList(shoppingCartAo, result);
        }
        else{//添加新商品
            QueryWrapper<Product> productQueryWrapper = Wrappers.query();
            productQueryWrapper.eq("id", shoppingCartAo.getProductId());
            Product queryProduct = productMapper.selectOne(productQueryWrapper);

            if(queryProduct==null){
                result.setSuccess(false);
                result.setMessage("没有该商品");
                result = addShoppingCartList(shoppingCartAo, result);
                return result;
            }

            ShoppingCart newCart = new ShoppingCart(Integer.parseInt(tokenService.getUseridFromToken(shoppingCartAo.getToken())), queryProduct.getId(), shoppingCartAo.getNum());
            shoppingCartMapper.insert(newCart);

            result.setSuccess(true);
            result.setMessage("已增加商品");
            result = addShoppingCartList(shoppingCartAo, result);
        }


        return result;
    }

    /**删除商品*/
    public ProductCartVo delProduct(ShoppingCartAo shoppingCartAo){
        ProductCartVo result = new ProductCartVo();
        QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = Wrappers.query();
        shoppingCartQueryWrapper.eq("user_id", Integer.parseInt(tokenService.getUseridFromToken(shoppingCartAo.getToken()))).eq("product_id", shoppingCartAo.getProductId());
        ShoppingCart queryCart = shoppingCartMapper.selectOne(shoppingCartQueryWrapper);

        int num = shoppingCartAo.getNum();

        if(queryCart==null){//没有该商品
            result.setSuccess(false);
            result.setMessage("没有该商品");
        }
        else if(queryCart.getProductNumber() <= num){//商品数量小于要删除的数量
            result.setSuccess(true);
            result.setMessage("删除成功");
            shoppingCartMapper.delete(shoppingCartQueryWrapper);
        }
        else{
            ShoppingCart newCart = new ShoppingCart(queryCart.getId(), queryCart.getUserId(), queryCart.getProductId(), queryCart.getProductNumber() - shoppingCartAo.getNum(), 0);
            shoppingCartMapper.updateById(newCart);
            result.setSuccess(true);
            result.setMessage("删除成功");
        }

        result = addShoppingCartList(shoppingCartAo, result);
        return result;
    }

    /**列出商品列表*/
    public ProductCartVo addShoppingCartList(ShoppingCartAo product, ProductCartVo productCartVo){
        QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = Wrappers.query();
        shoppingCartQueryWrapper.eq("user_id", Integer.parseInt(tokenService.getUseridFromToken(product.getToken())));
        List<ShoppingCart> queryCart = shoppingCartMapper.selectList(shoppingCartQueryWrapper);

        QueryWrapper<Product> productQueryWrapper = Wrappers.query();

        List<ShoppingCartListInfo> newCartListInfo = new ArrayList<>();
        ShoppingCartListInfo shoppingCartListInfo;

        for(int i = 0; i < queryCart.size(); i++){
            productQueryWrapper = Wrappers.query();
            productQueryWrapper.eq("id", queryCart.get(i).getProductId());
            Product queryProduct = productMapper.selectOne(productQueryWrapper);

            shoppingCartListInfo = new ShoppingCartListInfo(
                    queryProduct.getId(),
                    queryProduct.getProductImage(),
                    queryProduct.getProductDesc(),
                    queryProduct.getProductPrice(),
                    queryCart.get(i).getProductNumber(),
                    0,
                    queryCart.get(i).getPurchased());
            newCartListInfo.add(shoppingCartListInfo);
        }

        productCartVo.setShoppingCartListInfos(newCartListInfo);

        return productCartVo;
    }

    /**列出商品列表*/
    public List<ShoppingCartListInfo> addShoppingCartList(String token){
        QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = Wrappers.query();
        int userId=Integer.parseInt(tokenService.getUseridFromToken(token));
        shoppingCartQueryWrapper.eq("user_id", Integer.parseInt(tokenService.getUseridFromToken(token)));
        List<ShoppingCart> queryCart = shoppingCartMapper.selectList(shoppingCartQueryWrapper);

        QueryWrapper<Product> productQueryWrapper = Wrappers.query();

        List<ShoppingCartListInfo> newCartListInfo = new ArrayList<>();
        ShoppingCartListInfo shoppingCartListInfo;

        for(int i = 0; i < queryCart.size(); i++){
            productQueryWrapper = Wrappers.query();
            productQueryWrapper.eq("id", queryCart.get(i).getProductId());
            Product queryProduct = productMapper.selectOne(productQueryWrapper);

            shoppingCartListInfo = new ShoppingCartListInfo(
                    queryProduct.getId(),
                    queryProduct.getProductImage(),
                    queryProduct.getProductDesc(),
                    queryProduct.getProductPrice(),
                    queryCart.get(i).getProductNumber(),
                    0,
                    queryCart.get(i).getPurchased());
            newCartListInfo.add(shoppingCartListInfo);
        }

        return newCartListInfo;
    }

}
