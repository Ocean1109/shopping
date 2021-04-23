package com.example.demo;

import com.example.demo.entity.ShoppingUser;
import com.example.demo.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    UserMapper userMapper;
    @Test
    void contextLoads() {
    }

    @Test
    void select(){
        ShoppingUser user=userMapper.selectById(1);
        System.out.println(user.getName());
    }

}
