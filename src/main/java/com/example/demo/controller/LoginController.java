package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.ao.LoginAo;
import com.example.demo.ao.RegisterAo;
import com.example.demo.entity.ShoppingUser;
import com.example.demo.mapper.ShoppingUserMapper;
import com.example.demo.service.TokenService;
import com.example.demo.vo.BaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    @Autowired
    private ShoppingUserMapper shoppingUserMapper;

    @Autowired
    private TokenService tokenService;



    /**
     * @param user
     * @return
     */
    /**登录*/
    @PostMapping("/login")
    @ResponseBody
    public BaseVo login(@RequestBody LoginAo user){
        BaseVo result;
        QueryWrapper<ShoppingUser> shoppingUserQueryWrapper= Wrappers.query();
        shoppingUserQueryWrapper.eq("tel",user.getTel());
        ShoppingUser queryUser=shoppingUserMapper.selectOne(shoppingUserQueryWrapper);
        if(queryUser==null){//用户不存在
            result=new BaseVo(1,"该手机号未注册");
            return result;
        }else{
            if(queryUser.getPassword().equals(user.getPassword())){//登录成功
                //添加token
                String token=tokenService.createToken(String.valueOf(queryUser.getId()));
                queryUser.setToken(token);
                shoppingUserMapper.updateById(queryUser);
                result=new BaseVo(0,token);
                return result;
            }else{//密码不正确
                result=new BaseVo(2,"密码输入错误");
                return result;
            }
        }
    }


    /**
     * @param user
     * @return
     */
    /**注册*/
    @PostMapping("/register")
    @ResponseBody
    public BaseVo register(@RequestBody RegisterAo user){
        BaseVo result;
        QueryWrapper<ShoppingUser> shoppingUserQueryWrapper= Wrappers.query();
        shoppingUserQueryWrapper.eq("tel",user.getTel());
        ShoppingUser queryUser=shoppingUserMapper.selectOne(shoppingUserQueryWrapper);
        if(queryUser!=null){//手机号被注册
            result=new BaseVo(1,"该手机号已被注册");
            return result;
        }else{//成功注册
            int gender=0;
            if(user.getGender().equals('男')){
                gender=1;
            }
            ShoppingUser newUser =new ShoppingUser(user.getTel(),user.getPassword(),user.getUserName(),user.getAge(),gender);
            shoppingUserMapper.insert(newUser);
            //添加token
            ShoppingUser queryUserAgain=shoppingUserMapper.selectOne(shoppingUserQueryWrapper);
            String token=tokenService.createToken(String.valueOf(queryUserAgain.getId()));
            queryUserAgain.setToken(token);
            shoppingUserMapper.updateById(queryUserAgain);
            result=new BaseVo(0,token);
            return result;
        }
    }



}
