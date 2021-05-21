package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class ProductType {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    private String typeName;
    private int status;
    private Timestamp createTime;
    private int createMan;
    private Timestamp updateTime;
    private int updateMan;

    public ProductType(String typeName, int status, Timestamp createTime, int createMan, Timestamp updateTime, int updateMan) {
        this.typeName = typeName;
        this.status = status;
        this.createTime = createTime;
        this.createMan = createMan;
        this.updateTime = updateTime;
        this.updateMan = updateMan;
    }
}
