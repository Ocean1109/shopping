package com.example.demo.ao;

import lombok.Data;

@Data
public class ChangePasswordAo {
    private String token;
    private String code;
    private String password;
}
