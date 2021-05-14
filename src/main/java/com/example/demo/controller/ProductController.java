package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.ao.ReleaseAo;
import com.example.demo.entity.Brand;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductType;
import com.example.demo.mapper.BrandMapper;
import com.example.demo.mapper.ProductTypeMapper;
import com.example.demo.service.ProductService;
import com.example.demo.service.TokenService;
import com.example.demo.util.OssUtil;
import com.example.demo.vo.ProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private ProductTypeMapper productTypeMapper;
    @Autowired
    private TokenService tokenService;
    @Autowired
    OssUtil ossUtil; //注入OssUtil

    @GetMapping("/product")
    @ResponseBody
    public List<ProductVo> listAll(){
        return productService.showAllProduct();
    }

    @PostMapping("/release")
    @ResponseBody
    public void release(@RequestPart("productImage")MultipartFile productImage, @RequestPart("images") List<MultipartFile> images, @RequestPart("releaseProduct")ReleaseAo releaseProduct){
        Product product=new Product();
        product.setProductDesc(releaseProduct.getProductDesc());
        product.setProductPrice(releaseProduct.getProductPrice());
        int userId=Integer.parseInt(tokenService.getUseridFromToken(releaseProduct.getUser()));
        product.setPublishUserId(userId);
        product.setProductAddress(releaseProduct.getProductAddress());
        //获取当前时间
        String current = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date());
        Timestamp time = Timestamp.valueOf(current);
        product.setCreateTime(time);
        product.setUpdateTime(time);
        product.setNumbers(releaseProduct.getNumbers());
        product.setProductRule(releaseProduct.getProductRule());
        String url = ossUtil.uploadFile(productImage);
        product.setProductImage(url);
        //查看是否存在brand，存在则填入相应的id，不存在则新增一个brand,并且填入id
        QueryWrapper<Brand> brandQueryWrapper=new QueryWrapper<>();
        brandQueryWrapper.eq("brandName",releaseProduct.getBrand());
        Brand productBrand=brandMapper.selectOne(brandQueryWrapper);
        if(productBrand!=null){//存在
            product.setBrandId(productBrand.getId());
        }else{
            Brand newBrand=new Brand(releaseProduct.getBrand());
            brandMapper.insert(newBrand);
            product.setBrandId(newBrand.getId());
        }
        //查看是否存在productType，存在则填入相应的id，不存在则新增一个type,并且填入id
        QueryWrapper<ProductType> productTypeQueryWrapper=new QueryWrapper<>();
        ProductType productType=productTypeMapper.selectOne(productTypeQueryWrapper);
        if(productType!=null){//存在
            product.setProductTypeId(productType.getId());
        }else{
            ProductType newProductType=new ProductType(releaseProduct.getProductType(),1,time,userId,time,userId);
            productTypeMapper.insert(newProductType);
            product.setProductTypeId(newProductType.getId());
        }
        

    }



}
