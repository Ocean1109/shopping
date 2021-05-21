package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductRule {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    private int userId;
    private String rule;

    public ProductRule(int userId, String rule) {
        this.userId = userId;
        this.rule = rule;
    }
}
