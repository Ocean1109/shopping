package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.entity.ShoppingUser;
import com.example.demo.mapper.ShoppingUserMapper;
import com.example.demo.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
    @Autowired
    private ShoppingUserMapper shoppingUserMapper;

    @PostMapping("/login")
    @ResponseBody
    public LoginVo login(String tel,String password){
        LoginVo result;
        QueryWrapper<ShoppingUser> shoppingUserQueryWrapper= Wrappers.query();
        shoppingUserQueryWrapper.eq("tel",tel);
        ShoppingUser queryUser=shoppingUserMapper.selectOne(shoppingUserQueryWrapper);
        if(queryUser==null){//用户不存在
            result=new LoginVo(1);
            return result;
        }else{
            if(queryUser.getPassword().equals(password)){//登录成功
                result=new LoginVo(0);
                return result;
            }else{//密码不正确
                result=new LoginVo(2);
                return result;
            }
        }



    }


}
