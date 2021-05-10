package com.example.demo.service.implement;

import com.example.demo.entity.Product;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductServiceImp implements ProductService {

    @Autowired
    ProductMapper productMapper;

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
