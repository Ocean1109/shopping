package com.example.demo.service;

import com.example.demo.ao.ChangeMailAo;
import com.example.demo.ao.ChangePasswordAo;
import com.example.demo.ao.SendCodeAo;
import com.example.demo.ao.UserInfoAo;
import com.example.demo.vo.BaseVo;

public interface UserInfoService {
    public BaseVo UpdateUserInfo(UserInfoAo userInfoAo);
    public BaseVo ChangeMail(ChangeMailAo changeMailAo);
    public BaseVo SendCode(SendCodeAo sendCodeAo);
    public BaseVo ChangePassword(ChangePasswordAo changePasswordAo);
}
