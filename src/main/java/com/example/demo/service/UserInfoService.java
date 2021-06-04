package com.example.demo.service;

import com.example.demo.ao.ChangeMailAo;
import com.example.demo.ao.ChangePasswordAo;
import com.example.demo.ao.UserInfoAo;
import com.example.demo.vo.BaseVo;

public interface UserInfoService {
    /**
     * @param userInfoAo
     * @return
     */
    /**更新用户信息*/
    public BaseVo UpdateUserInfo(UserInfoAo userInfoAo);

    /**
     * @param changeMailAo
     * @return
     */
    /**更新邮箱*/
    public BaseVo ChangeMail(ChangeMailAo changeMailAo);



    /**
     *
     * @param id
     * @return
     */
    /**发送验证码*/
    public BaseVo SendCode(int id);

    /**
     * @param changePasswordAo
     * @return
     */
    /**更改密码*/
    public BaseVo ChangePassword(ChangePasswordAo changePasswordAo);
}
