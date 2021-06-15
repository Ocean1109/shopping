package com.example.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailVo {
    private String productDesc;//商品描述
    private String productImage;//商品照片
    private List<String> moreImages;//商品详细照片
    private Double productPrice;//商品价格
    private String productType;//商品类别
    private String productBrand;//商品品牌
    private int publishUserId;//商家id
    private String productAddress;//商品地址
    private int numbers;//商品数量
    private List<String> rule;//商品规则
    private List<String> productRule;//商品规则内容
}
