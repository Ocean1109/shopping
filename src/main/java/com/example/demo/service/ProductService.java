package com.example.demo.service;

import com.example.demo.ao.ReleaseAo;
import com.example.demo.entity.Product;
import com.example.demo.vo.BaseVo;
import com.example.demo.vo.ProductVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    /**
     * @des 展示相应的商品的概览信息
     * @param productType
     * @return 商品实体类list*/
    List<ProductVo> showProduct(String productType,String brand,String address);
    /**
     * @des 展示单个商品的信息
     * @param id
     * @return 商品实体类*/
    Product showSingleProduct(int id);
    /**
     * @des 发布一个商品
     * @param productImage,releaseProduct
     * @return 是否成功发布*/
    Boolean releaseProduct(MultipartFile productImage, List<MultipartFile> moreImages,ReleaseAo releaseProduct);
    /**
     * @des 删除一个商品
     * @param id
     * @return 是否成功删除*/
    BaseVo deleteProduct(int id);
}
