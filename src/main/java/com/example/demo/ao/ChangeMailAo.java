package com.example.demo.ao;

import lombok.Data;

@Data
public class ChangeMailAo {
    private String token;
    private String password;
    private String mail;
}
