package com.example.demo.service;

import com.example.demo.ao.ReleaseAo;
import com.example.demo.entity.Product;
import com.example.demo.vo.ProductVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<ProductVo> showAllProduct();
    /**
     * @des 展示单个商品的信息
     * @param id
     * @return 商品实体类*/
    Product showSingleProduct(int id);
    /**
     * @des 发布一个商品
     * @param product
     * @return 是否成功发布*/
    public Boolean releaseProduct(MultipartFile productImage, ReleaseAo releaseProduct);
}
