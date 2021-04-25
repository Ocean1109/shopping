package com.example.demo.service.implement;

import com.example.demo.entity.ShoppingUser;
import com.example.demo.mapper.ShoppingUserMapper;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceImp implements UserService {

    @Autowired
    ShoppingUserMapper shoppingUserMapper;

    @Override
    public ShoppingUser showUser(String userId) {
        ShoppingUser showUser=new ShoppingUser();
        showUser= shoppingUserMapper.selectById(Integer.parseInt(userId));
        return showUser;
    }
}
