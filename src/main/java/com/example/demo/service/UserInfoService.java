package com.example.demo.service;

import com.example.demo.ao.ChangeMailAo;
import com.example.demo.ao.ChangePasswordAo;
import com.example.demo.ao.UserInfoAo;
import com.example.demo.vo.BaseVo;
import com.example.demo.vo.UserInfoVo;
import org.springframework.scheduling.annotation.Async;

public interface UserInfoService {
    /**
     * @param token
     * @return
     */
    /**展示用户信息*/
    @Async
    public UserInfoVo ShowUserInfo(String token);

    /**
     * @param userInfoAo
     * @return
     */
    /**更新用户信息*/
    @Async
    public BaseVo UpdateUserInfo(UserInfoAo userInfoAo);

    /**
     * @param changeMailAo
     * @return
     */
    /**更新邮箱*/
    @Async
    public BaseVo ChangeMail(ChangeMailAo changeMailAo);

    /**
     *
     * @param id
     * @return
     */
    /**发送验证码*/
    @Async
    public BaseVo SendCode(int id);

    /**
     * @param changePasswordAo
     * @return
     */
    /**更改密码*/
    @Async
    public BaseVo ChangePassword(ChangePasswordAo changePasswordAo);
}
