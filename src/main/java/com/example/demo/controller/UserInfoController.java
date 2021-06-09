package com.example.demo.controller;

import com.example.demo.ao.ChangeMailAo;
import com.example.demo.ao.ChangePasswordAo;
import com.example.demo.ao.UserInfoAo;
import com.example.demo.service.UserInfoService;
import com.example.demo.vo.BaseVo;
import com.example.demo.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserInfoController {

    @Autowired
    UserInfoService userInfoService;

    /**
     * @param token
     * @return
     */
    /**展示用户信息*/
    @PostMapping("/ShowUserInfo")
    @ResponseBody
    public UserInfoVo ShowUserInfo(@RequestPart("token") String token){
        return userInfoService.ShowUserInfo(token);
    }

    /**
     * @param userInfoAo
     * @return
     */
    /**更改用户信息*/
    @PostMapping("/UpdateUserInfo")
    @ResponseBody
    public BaseVo UpdateUserInfo(@RequestBody UserInfoAo userInfoAo){
        return userInfoService.UpdateUserInfo(userInfoAo);
    }

    /**
     * @param changeMailAo
     * @return
     */
    /**更改用户邮箱*/
    @PostMapping("/ChangeMail")
    @ResponseBody
    public BaseVo ChangeMail(@RequestBody ChangeMailAo changeMailAo){
        return userInfoService.ChangeMail(changeMailAo);
    }

    /**
     * @param id
     * @return
     */
    /**获取验证码*/
    @PostMapping("/SendCode")
    @ResponseBody
    public BaseVo SendCode(@RequestBody int id){
        return userInfoService.SendCode(id);
    }

    /**
     * @param changePasswordAo
     * @return
     */
    /**更改用户密码*/
    @PostMapping("/ChangePassword")
    @ResponseBody
    public BaseVo ChangePassword(@RequestBody ChangePasswordAo changePasswordAo){
        return userInfoService.ChangePassword(changePasswordAo);
    }




}
