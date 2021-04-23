package com.example.demo.controller;

import com.example.demo.entity.ShoppingUser;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
    @Autowired
    private UserMapper userMapper;

    @PostMapping("/login")
    @ResponseBody
    public String login(){
        ShoppingUser user=userMapper.selectById(1);
        return user.getName();
    }
}
