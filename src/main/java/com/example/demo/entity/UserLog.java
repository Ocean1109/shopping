package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLog {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    private int userId;
    private int itemId;
    private int catId;
    private int merchantId;
    private int brandId;
    private int month;
    private int day;
    private int action;
    private int age_range;
    private int gender;
    private String province;

    public UserLog(int userId, int itemId, int catId, int merchantId, int brandId, int month, int day, int action, int age_range, int gender, String province) {
        this.userId = userId;
        this.itemId = itemId;
        this.catId = catId;
        this.merchantId = merchantId;
        this.brandId = brandId;
        this.month = month;
        this.day = day;
        this.action = action;
        this.age_range = age_range;
        this.gender = gender;
        this.province = province;
    }
}
