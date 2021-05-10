package com.example.demo.service;

import com.example.demo.entity.Product;

public interface ProductService {
    /**
     * @des 展示单个商品的信息
     * @param id
     * @return 商品实体类*/
    Product showSingleProduct(int id);
    /**
     * @des 发布一个商品
     * @param product
     * @return 是否成功发布*/
    Boolean releaseProduct(Product product);
}
