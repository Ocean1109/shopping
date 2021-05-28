package com.example.demo.controller;

import com.example.demo.ao.ChangeMailAo;
import com.example.demo.ao.ChangePasswordAo;
import com.example.demo.ao.SendCodeAo;
import com.example.demo.ao.UserInfoAo;
import com.example.demo.service.UserInfoService;
import com.example.demo.vo.BaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserInfoController {

    @Autowired
    UserInfoService userInfoService;

    /**更改用户信息*/
    @PostMapping("/UpdateUserInfo")
    @ResponseBody
    public BaseVo UpdateUserInfo(@RequestBody UserInfoAo userInfoAo){
        return userInfoService.UpdateUserInfo(userInfoAo);
    }

    /**更改用户邮箱*/
    @PostMapping("/ChangeMail")
    @ResponseBody
    public BaseVo ChangeMail(@RequestBody ChangeMailAo changeMailAo){
        return userInfoService.ChangeMail(changeMailAo);
    }

    /**获取验证码*/
    @PostMapping("/SendCode")
    @ResponseBody
    public BaseVo SendCode(@RequestBody SendCodeAo sendCodeAo){
        return userInfoService.SendCode(sendCodeAo);
    }

    /**更改用户密码*/
    @PostMapping("/ChangePassword")
    @ResponseBody
    public BaseVo ChangePassword(@RequestBody ChangePasswordAo changePasswordAo){
        return userInfoService.ChangePassword(changePasswordAo);
    }




}
