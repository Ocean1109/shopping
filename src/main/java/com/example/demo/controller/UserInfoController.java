package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.annotation.UserLoginToken;
import com.example.demo.ao.ChangeMailAo;
import com.example.demo.ao.ChangePasswordAo;
import com.example.demo.ao.SendCodeAo;
import com.example.demo.ao.UserInfoAo;
import com.example.demo.entity.ShoppingUser;
import com.example.demo.mapper.ShoppingUserMapper;
import com.example.demo.util.PatternMatchUtil;
import com.example.demo.util.SendMailUtil;
import com.example.demo.vo.BaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserInfoController {

    @Autowired
    private ShoppingUserMapper shoppingUserMapper;

    private String code;

    /**更改用户信息*/
    @PostMapping("/UpdateUserInfo")
    @ResponseBody
    @UserLoginToken
    public BaseVo UpdateUserInfo(@RequestBody UserInfoAo userInfoAo){
        BaseVo result = new BaseVo();

        QueryWrapper<ShoppingUser> shoppingUserQueryWrapper = Wrappers.query();
        shoppingUserQueryWrapper.eq("id", userInfoAo.getId());
        ShoppingUser queryUser = shoppingUserMapper.selectOne(shoppingUserQueryWrapper);



        ShoppingUser newUserInfo = new ShoppingUser(
                queryUser.getId(),
                (userInfoAo.getTel()!=null)? userInfoAo.getTel(): queryUser.getTel(),
                queryUser.getPassword(),
                (userInfoAo.getUserName()!=null)? userInfoAo.getUserName(): queryUser.getUserName(),
                queryUser.getToken(),
                (userInfoAo.getAddress()!=null)? userInfoAo.getAddress(): queryUser.getAddress(),
                (userInfoAo.getAge()!=null)? Integer.parseInt(userInfoAo.getAge()) : queryUser.getAge(),
                (userInfoAo.getGender()!=null)? Integer.parseInt(userInfoAo.getGender()): queryUser.getGender(),
                queryUser.getMail());

        shoppingUserMapper.update(newUserInfo, shoppingUserQueryWrapper);

        result.setCode(0);
        result.setMessage("修改信息成功");

        return result;
    }

    /**更改用户邮箱*/
    @PostMapping("/ChangeMail")
    @ResponseBody
    @UserLoginToken
    public BaseVo ChangeMail(@RequestBody ChangeMailAo changeMailAo){
        BaseVo result = new BaseVo();

        QueryWrapper<ShoppingUser> shoppingUserQueryWrapper = Wrappers.query();
        shoppingUserQueryWrapper.eq("id", changeMailAo.getId());
        ShoppingUser queryUser = shoppingUserMapper.selectOne(shoppingUserQueryWrapper);

        String password = queryUser.getPassword();

        if(!PatternMatchUtil.isMatchingMail(changeMailAo.getMail())){
            result.setCode(1);
            result.setMessage("邮箱格式不正确");
        }
        else if(!password.equals(changeMailAo.getPassword())){
            result.setCode(1);
            result.setMessage("密码不正确");
        }
        else {
            ShoppingUser newUserInfo = new ShoppingUser(
                    queryUser.getId(),
                    queryUser.getTel(),
                    queryUser.getPassword(),
                    queryUser.getUserName(),
                    queryUser.getToken(),
                    queryUser.getAddress(),
                    queryUser.getAge(),
                    queryUser.getGender(),
                    changeMailAo.getMail());
            shoppingUserMapper.update(newUserInfo, shoppingUserQueryWrapper);

            result.setCode(0);
            result.setMessage("邮箱更改成功");
        }

        return result;
    }

    /**获取验证码*/
    @PostMapping("/SendCode")
    @ResponseBody
    @UserLoginToken
    public BaseVo SendCode(@RequestBody SendCodeAo sendCodeAo){
        BaseVo result = new BaseVo();

        QueryWrapper<ShoppingUser> shoppingUserQueryWrapper = Wrappers.query();
        shoppingUserQueryWrapper.eq("id", sendCodeAo.getId());
        ShoppingUser queryUser = shoppingUserMapper.selectOne(shoppingUserQueryWrapper);

        String code = SendMailUtil.sendCode(queryUser.getMail());

        if(code == "SendingException"){
            result.setCode(1);
            result.setMessage("发送失败");
        }
        else {
            this.code = code;
            result.setCode(0);
            result.setMessage("发送成功");
        }

        return result;
    }

    /**更改用户密码*/
    @PostMapping("/ChangePassword")
    @ResponseBody
    @UserLoginToken
    public BaseVo ChangePassword(@RequestBody ChangePasswordAo changePasswordAo){
        BaseVo result = new BaseVo();

        QueryWrapper<ShoppingUser> shoppingUserQueryWrapper = Wrappers.query();
        shoppingUserQueryWrapper.eq("id", changePasswordAo.getId());
        ShoppingUser queryUser = shoppingUserMapper.selectOne(shoppingUserQueryWrapper);

        if(code == "SendingException"){
            result.setCode(1);
            result.setMessage("发送失败");
        }
        else if(!changePasswordAo.getCode().equals(code)){
            result.setCode(1);
            result.setMessage("验证码错误");
        }
        else {
            ShoppingUser newUserInfo = new ShoppingUser(
                    queryUser.getId(),
                    queryUser.getTel(),
                    changePasswordAo.getPassword(),
                    queryUser.getUserName(),
                    queryUser.getToken(),
                    queryUser.getAddress(),
                    queryUser.getAge(),
                    queryUser.getGender(),
                    queryUser.getMail());
            shoppingUserMapper.update(newUserInfo, shoppingUserQueryWrapper);

            code = null;
            result.setCode(0);
            result.setMessage("修改成功");
        }

        return result;
    }




}
