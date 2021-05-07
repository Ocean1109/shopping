package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShoppingUser {
    private int id;
    private String tel;
    private String password;
    private String userName;
    private String token;
    private String address;
    private int age;
    private int gender;


    public ShoppingUser(String tel, String password, String userName, String address, int age, int gender) {
        this.tel = tel;
        this.password = password;
        this.userName = userName;
        this.address = address;
        this.age = age;
        this.gender = gender;
    }

    public ShoppingUser(String tel, String password, String userName, int age, int gender) {
        this.tel = tel;
        this.password = password;
        this.userName = userName;
        this.age = age;
        this.gender = gender;
    }
}
