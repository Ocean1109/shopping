package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.ao.LoginAo;
import com.example.demo.ao.RegisterAo;
import com.example.demo.entity.ShoppingUser;
import com.example.demo.mapper.ShoppingUserMapper;
import com.example.demo.service.TokenService;
import com.example.demo.vo.BaseVo;
import com.example.demo.vo.LoginSuccessVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author huhaiyang
 */
@Controller
public class LoginController {

    @Autowired
    private ShoppingUserMapper shoppingUserMapper;

    @Autowired
    private TokenService tokenService;

    private final ReentrantLock lock = new ReentrantLock();

    /**持久化登录**/
    @GetMapping("/isLogin")
    @ResponseBody
    public LoginSuccessVo isLogin(HttpServletRequest request){
        LoginSuccessVo result;
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("token")){
                    int id = Integer.parseInt(tokenService.getUseridFromToken(cookie.getValue()));
                    ShoppingUser user = shoppingUserMapper.selectById(id);
                    result=new LoginSuccessVo(0,cookie.getValue(),user.getUserName());
                    return result;
                }
            }
            result = new LoginSuccessVo(3,"未登录","");
            return result;
        }
        result = new LoginSuccessVo(3,"未登录","");
        return result;
    }


    /**
     * @param user
     * @return
     */
    /**登录*/
    @PostMapping("/login")
    @ResponseBody
    public LoginSuccessVo login(@RequestBody LoginAo user, HttpServletResponse response){
        LoginSuccessVo result;
        QueryWrapper<ShoppingUser> shoppingUserQueryWrapper= Wrappers.query();
        shoppingUserQueryWrapper.eq("tel",user.getTel());
        ShoppingUser queryUser=shoppingUserMapper.selectOne(shoppingUserQueryWrapper);
        lock.lock();
        try {
            //用户不存在
            if(queryUser==null){
                result=new LoginSuccessVo(1,"该手机号未注册","");
                return result;
            }else{
                //登录成功
                if(queryUser.getPassword().equals(user.getPassword())){
                    //添加token
                    String token=tokenService.createToken(String.valueOf(queryUser.getId()));
                    queryUser.setToken(token);
                    shoppingUserMapper.updateById(queryUser);
                    Cookie cookie = new Cookie("token",token);
                    //cookie保存7天
                    cookie.setMaxAge(7*24*60*60);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    result=new LoginSuccessVo(0,token,queryUser.getUserName());
                    return result;
                }else{//密码不正确
                    result=new LoginSuccessVo(2,"密码输入错误","");
                    return result;
                }
            }
        }
        finally {
            lock.unlock();
        }

    }


    /**
     * @param user
     * @return
     */
    /**注册*/
    @PostMapping("/register")
    @ResponseBody
    public BaseVo register(@RequestBody RegisterAo user,HttpServletResponse response){
        BaseVo result;
        QueryWrapper<ShoppingUser> shoppingUserQueryWrapper= Wrappers.query();
        shoppingUserQueryWrapper.eq("tel",user.getTel());
        ShoppingUser queryUser=shoppingUserMapper.selectOne(shoppingUserQueryWrapper);

        lock.lock();

        try {
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
                Cookie cookie = new Cookie("token",token);
                response.addCookie(cookie);
                shoppingUserMapper.updateById(queryUserAgain);
                result=new BaseVo(0,token);
                return result;
            }
        }
        finally {
            lock.unlock();
        }

    }



}
