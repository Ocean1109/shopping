package com.example.demo.service;

import com.example.demo.entity.ShoppingUser;

public interface UserService {
    /**
     * @param userId
     * @return 用户实体类*/
    /**展示用户信息*/
    public ShoppingUser showUser(String userId);
}
