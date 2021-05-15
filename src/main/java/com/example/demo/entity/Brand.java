package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Brand {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    private String brandName;
    private String description;

    public Brand(String brandName) {
        this.brandName = brandName;
    }
}
