package com.example.demo.service.implement;

import com.example.demo.entity.ShoppingUser;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceImp implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public ShoppingUser showUser(String userId) {
        ShoppingUser showUser=new ShoppingUser();
        showUser=userMapper.selectById(Integer.parseInt(userId));
        return showUser;
    }
}
