package com.example.demo.service;

import com.example.demo.entity.ShoppingUser;

public interface UserService {
    /**
     * @des 展示用户信息
     * @param userId
     * @return 用户实体类*/
    ShoppingUser showUser(String userId);
}
