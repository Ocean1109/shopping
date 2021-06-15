package com.example.demo.service;

import com.example.demo.ao.ReleaseAo;
import com.example.demo.entity.Product;
import com.example.demo.vo.BaseVo;
import com.example.demo.vo.ProductDetailVo;
import com.example.demo.vo.ProductVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    /**
     * @param productType
     * @return 商品实体类list*/
    /**展示相应的商品的概览信息*/
    List<ProductVo> showProduct(String productType,String brand,String address);

    /**
     * @param id
     * @return 商品实体类*/
    /**展示单个商品的信息*/
    ProductDetailVo showSingleProduct(int id);
    /**
     * @param productImage,releaseProduct
     * @return 是否成功发布*/
    /**发布一个商品*/
    Boolean releaseProduct(MultipartFile productImage, List<MultipartFile> moreImages,ReleaseAo releaseProduct);
    /**
     * @param id
     * @return 是否成功删除*/
    /**删除一个商品*/
    BaseVo deleteProduct(int id);
}
