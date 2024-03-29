package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductImage {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    private int imageId;
    private int productId;

    public ProductImage(int imageId, int productId) {
        this.imageId = imageId;
        this.productId = productId;
    }
}
