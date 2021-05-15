package com.example.demo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.demo.entity.ProductType;
import com.example.demo.entity.ShoppingUser;
import com.example.demo.mapper.ProductTypeMapper;
import com.example.demo.mapper.ShoppingUserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    ProductTypeMapper productTypeMapper;
    @Autowired
    ShoppingUserMapper shoppingUserMapper;
    @Test
    void contextLoads() {
    }


    @Test
    void insert(){
        ShoppingUser user =new ShoppingUser("15011099680","123456","胡海洋","北京市海淀区",21,1);
        shoppingUserMapper.insert(user);
    }
    @Test
    void delete(){
        shoppingUserMapper.deleteById(0);
    }
    @Test
    void update(){
        ShoppingUser user =new ShoppingUser("15011099680","123456","胡海洋","北京市海淀区西土城路10号",21,1);
        UpdateWrapper<ShoppingUser> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("user_name","胡海洋");
        shoppingUserMapper.update(user,updateWrapper);
    }
    @Test
    void select(){
        ShoppingUser user= shoppingUserMapper.selectById(0);
        System.out.println(user.getUserName());
    }

    @Test
    void productTypeTest(){
        QueryWrapper<ProductType> productTypeQueryWrapper=new QueryWrapper<>();
        productTypeQueryWrapper.eq("type_name","生活用品");
        ProductType productType=productTypeMapper.selectById(0);
        if(productType!=null){//存在
            System.out.println(productType.getId());
        }else{
            System.out.println("新建");
        }
    }

}
