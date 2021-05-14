package com.example.demo.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.Product;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.service.ProductService;
import com.example.demo.vo.ProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    ProductMapper productMapper;

    @Override
    public List<ProductVo> showAllProduct(){
        List<ProductVo> allProduct=new ArrayList<>();
        QueryWrapper<Product> queryWrapper=new QueryWrapper<>();
        queryWrapper.select("id","product_desc","product_image","product_price");
        List<Product> products=productMapper.selectList(queryWrapper);
        for(Product product:products){
            allProduct.add(new ProductVo(product.getId(),product.getProductDesc(),product.getProductImage(),product.getProductPrice()));
        }
        return allProduct;
    }
    @Override
    public Product showSingleProduct(int id) {
        Product product=new Product();
        product=productMapper.selectById(id);
        return product;
    }

    @Override
    public Boolean releaseProduct(Product product){
        int result=productMapper.insert(product);
        if(result==1){
            return true;
        }else{
            return false;
        }
    }
}
