package com.example.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseVo {
    /**登录：
     * 0:成功
     * 1:电话号未注册
     * 2:密码错误
     * 注册：
     * 0:成功
     * 1:手机号已经被注册*/
    private int code;
    private String message;
}
