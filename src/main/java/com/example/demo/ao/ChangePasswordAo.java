package com.example.demo.ao;

import lombok.Data;

@Data
public class ChangePasswordAo {
    private int id;
    private String code;
    private String password;
}
