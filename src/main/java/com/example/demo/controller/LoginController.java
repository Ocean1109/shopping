package com.example.demo.controller;

import com.example.demo.mapper.ShoppingUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class LoginController {
    @Autowired
    private ShoppingUserMapper shoppingUserMapper;


}
