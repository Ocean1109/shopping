package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingUser {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    private String tel;
    private String password;
    private String userName;
    private String token;
    private String address;
    private int age;
    private int gender;
    private String mail;
    private String code;

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
