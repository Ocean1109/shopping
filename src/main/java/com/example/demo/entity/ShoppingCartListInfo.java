package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartListInfo {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    private String image;
    private String name;
    private double price;
    private int productNumber;
    private int type;/**恒为0*/
    private int purchased;/**0没购买*/
}
