package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OrderController {

    @PostMapping("/generateOrder")
    @ResponseBody
    public void generateOrder(){

    }
}
