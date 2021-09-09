package com.example.demo.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.ao.ShoppingCartAo;
import com.example.demo.entity.*;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.ShoppingCartMapper;
import com.example.demo.redis.RedisUtils;
import com.example.demo.mapper.OrderProductMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.ShoppingCartMapper;
import com.example.demo.mapper.ShoppingOrderMapper;
import com.example.demo.service.ShoppingCartService;
import com.example.demo.service.TokenService;
import com.example.demo.vo.ProductCartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ShoppingCartImp implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private TokenService tokenService;

    @Autowired
    RedisUtils redisUtils;

    private final ReentrantLock lock = new ReentrantLock();

    @Override
    @Async
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
    @Override
    @Async
    public ProductCartVo addProduct(ShoppingCartAo shoppingCartAo){

        ProductCartVo result = new ProductCartVo();

        lock.lock();
        try{
            QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = Wrappers.query();
            shoppingCartQueryWrapper.eq("user_id", Integer.parseInt(tokenService.getUseridFromToken(shoppingCartAo.getToken()))).eq("product_id", shoppingCartAo.getProductId());
            ShoppingCart queryCart = shoppingCartMapper.selectOne(shoppingCartQueryWrapper);

            if(queryCart!=null){
                //购物车某个商品数量增加
                ShoppingCart newCart = new ShoppingCart(queryCart.getId(), queryCart.getUserId(), queryCart.getProductId(), queryCart.getProductNumber() + shoppingCartAo.getNum(), 0);
                shoppingCartMapper.updateById(newCart);

                redisUtils.delete("shopping_cart_product_number_" + queryCart.getId());

                result.setSuccess(true);
                result.setMessage("已添加至购物车");
                result = addShoppingCartList(shoppingCartAo, result);
            }
            else {
                //添加新商品
                QueryWrapper<Product> productQueryWrapper = Wrappers.query();
                productQueryWrapper.eq("id", shoppingCartAo.getProductId());
                Product queryProduct = productMapper.selectOne(productQueryWrapper);

                if (queryProduct == null) {
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
        }
        finally {
            lock.unlock();
        }

        return result;
    }

    /**删除商品*/
    @Override
    @Async
    public ProductCartVo delProduct(ShoppingCartAo shoppingCartAo){
        ProductCartVo result = new ProductCartVo();

        lock.lock();
        try {

        QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = Wrappers.query();

        shoppingCartQueryWrapper.eq("user_id", Integer.parseInt(tokenService.getUseridFromToken(shoppingCartAo.getToken()))).eq("product_id", shoppingCartAo.getProductId());
        ShoppingCart queryCart = shoppingCartMapper.selectOne(shoppingCartQueryWrapper);

            int num = shoppingCartAo.getNum();

            if(queryCart==null){
                //没有该商品
                result.setSuccess(false);
                result.setMessage("没有该商品");
            }
            else if(queryCart.getProductNumber() <= num){
                //商品数量小于要删除的数量
                shoppingCartMapper.delete(shoppingCartQueryWrapper);

                redisUtils.delete("shopping_cart_user_id_" + queryCart.getId());
                redisUtils.delete("shopping_cart_product_id_" + queryCart.getId());
                redisUtils.delete("shopping_cart_product_number_" + queryCart.getId());
                redisUtils.delete("shopping_cart_purchased_" + queryCart.getId());

                result.setSuccess(true);
                result.setMessage("删除成功");
            }
            else{
                ShoppingCart newCart = new ShoppingCart(queryCart.getId(), queryCart.getUserId(), queryCart.getProductId(), queryCart.getProductNumber() - shoppingCartAo.getNum(), 0);
                shoppingCartMapper.updateById(newCart);

                redisUtils.delete("shopping_cart_product_number_" + queryCart.getId());

                result.setSuccess(true);
                result.setMessage("删除成功");
            }

            result = addShoppingCartList(shoppingCartAo, result);
        }
        finally {
            lock.unlock();
        }


        return result;
    }

    /**列出商品列表*/
    @Override
    public ProductCartVo addShoppingCartList(ShoppingCartAo shoppingCartAo, ProductCartVo productCartVo){
        QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = Wrappers.query();
        shoppingCartQueryWrapper.eq("user_id", Integer.parseInt(tokenService.getUseridFromToken(shoppingCartAo.getToken())));

        List<ShoppingCart> queryCart = shoppingCartMapper.selectList(shoppingCartQueryWrapper);

        QueryWrapper<Product> productQueryWrapper = Wrappers.query();

        List<ShoppingCartListInfo> newCartListInfo = new ArrayList<>();
        ShoppingCartListInfo shoppingCartListInfo;
        String exist;

        for(int i = 0; i < queryCart.size(); i++){

            exist = redisUtils.get("shopping_cart_product_number_" + queryCart.get(i).getId());

            if(exist == null){
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

                redisUtils.set("shopping_cart_user_id_" + queryCart.get(i).getId(), tokenService.getUseridFromToken(shoppingCartAo.getToken()));
                redisUtils.set("shopping_cart_product_id_" + queryCart.get(i).getId(), String.valueOf(queryProduct.getId()));
                redisUtils.set("shopping_cart_product_number_" + queryCart.get(i).getId(), String.valueOf(queryCart.get(i).getProductNumber()));
                redisUtils.set("shopping_cart_purchased_" + queryCart.get(i).getId(), String.valueOf(queryCart.get(i).getPurchased()));

            }
            else{
                productQueryWrapper = Wrappers.query();
                productQueryWrapper.eq("id", queryCart.get(i).getProductId());
                Product queryProduct = productMapper.selectOne(productQueryWrapper);

                shoppingCartListInfo = new ShoppingCartListInfo(
                        queryProduct.getId(),
                        queryProduct.getProductImage(),
                        queryProduct.getProductDesc(),
                        queryProduct.getProductPrice(),
                        Integer.parseInt(redisUtils.get("shopping_cart_product_number_" + queryCart.get(i).getId())),
                        0,
                        Integer.parseInt(redisUtils.get("shopping_cart_purchased_" + queryCart.get(i).getId())));
                newCartListInfo.add(shoppingCartListInfo);
            }

        }

        productCartVo.setShoppingCartListInfos(newCartListInfo);

        return productCartVo;
    }

    /**列出商品列表*/
    @Override
    public List<ShoppingCartListInfo> addShoppingCartList(String token){
        QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = Wrappers.query();
        int userId=Integer.parseInt(tokenService.getUseridFromToken(token));
        shoppingCartQueryWrapper.eq("user_id", Integer.parseInt(tokenService.getUseridFromToken(token)));
        List<ShoppingCart> queryCart = shoppingCartMapper.selectList(shoppingCartQueryWrapper);

        QueryWrapper<Product> productQueryWrapper = Wrappers.query();

        List<ShoppingCartListInfo> newCartListInfo = new ArrayList<>();
        ShoppingCartListInfo shoppingCartListInfo;
        String exist;

        for(int i = 0; i < queryCart.size(); i++){

            exist = redisUtils.get("shopping_cart_product_number_" + queryCart.get(i).getId());

            if(exist == null){
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

                redisUtils.set("shopping_cart_user_id_" + queryCart.get(i).getId(), tokenService.getUseridFromToken(token));
                redisUtils.set("shopping_cart_product_id_" + queryCart.get(i).getId(), String.valueOf(queryProduct.getId()));
                redisUtils.set("shopping_cart_product_number_" + queryCart.get(i).getId(), String.valueOf(queryCart.get(i).getProductNumber()));
                redisUtils.set("shopping_cart_purchased_" + queryCart.get(i).getId(), String.valueOf(queryCart.get(i).getPurchased()));
            }
            else{
                productQueryWrapper = Wrappers.query();
                productQueryWrapper.eq("id", queryCart.get(i).getProductId());
                Product queryProduct = productMapper.selectOne(productQueryWrapper);

                shoppingCartListInfo = new ShoppingCartListInfo(
                        queryProduct.getId(),
                        queryProduct.getProductImage(),
                        queryProduct.getProductDesc(),
                        queryProduct.getProductPrice(),
                        Integer.parseInt(redisUtils.get("shopping_cart_product_number_" + queryCart.get(i).getId())),
                        0,
                        Integer.parseInt(redisUtils.get("shopping_cart_purchased_" + queryCart.get(i).getId())));
                newCartListInfo.add(shoppingCartListInfo);
            }

        }

        return newCartListInfo;
    }

}
