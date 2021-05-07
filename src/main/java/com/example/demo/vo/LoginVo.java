package com.example.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginVo {
    private int code;//0代表成功，1代表电话号未注册，2代表密码错误
}
