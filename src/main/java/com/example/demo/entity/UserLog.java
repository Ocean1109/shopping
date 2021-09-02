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
    private String id;
    private String userId;
    private String itemId;
    private String catId;
    private String merchantId;
    private String brandId;
    private String month;
    private String day;
    private String action;
    private String ageRange;
    private String gender;
    private String province;

    public UserLog(String userId, String itemId, String catId, String merchantId, String brandId, String month, String day, String action, String ageRange, String gender, String province) {
        this.userId = userId;
        this.itemId = itemId;
        this.catId = catId;
        this.merchantId = merchantId;
        this.brandId = brandId;
        this.month = month;
        this.day = day;
        this.action = action;
        this.ageRange = ageRange;
        this.gender = gender;
        this.province = province;
    }
}
