package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.example.demo.annotation.PassToken;
import com.example.demo.annotation.UserLoginToken;
import com.example.demo.ao.ReleaseAo;
import com.example.demo.entity.Brand;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductRule;
import com.example.demo.entity.ProductType;
import com.example.demo.mapper.BrandMapper;
import com.example.demo.mapper.ProductRuleMapper;
import com.example.demo.mapper.ProductTypeMapper;
import com.example.demo.service.ProductService;
import com.example.demo.service.TokenService;
import com.example.demo.util.OssUtil;
import com.example.demo.vo.BaseVo;
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
    private ProductRuleMapper productRuleMapper;
    @Autowired
    private TokenService tokenService;
    @Autowired
    OssUtil ossUtil;

    /**
     * @param productType
     * @return
     */
    /**根据商品类别列出对应商品概览*/
    @GetMapping("/product/{productType}/{brand}/{address}")
    @ResponseBody
    public List<ProductVo> list(@PathVariable("productType")String productType,@PathVariable("brand")String brand,@PathVariable("address")String address){
        return productService.showProduct(productType,brand,address);
    }

    /**
     * @param productImage
     * @param releaseProduct
     * @return
     */
    /**发布商品*/
    @PostMapping("/release")
    @ResponseBody
    public Boolean release(@RequestParam("productImage")MultipartFile productImage,
                           @RequestParam("moreImages")List<MultipartFile> moreImages,
                           @RequestParam("productDesc")String productDesc,
                           @RequestParam("productPrice")Double productPrice,
                           @RequestParam("productType")String productType,
                           @RequestParam("brand")String brand,
                           @RequestParam("user")String user,
                           @RequestParam("productAddress")String productAddress,
                           @RequestParam("numbers")int numbers,
                           @RequestParam("rule")String rule,
                           @RequestParam("productRule")String productRule){
        ReleaseAo releaseProduct=new ReleaseAo(productDesc,productPrice,productType,brand,user,productAddress,numbers,rule,productRule);
        return productService.releaseProduct(productImage,moreImages,releaseProduct);
    }

    /**
     *
     */
    /**删除商品*/
    @PostMapping("/delete")
    @ResponseBody
    public BaseVo delete(@RequestParam("id")int id){
        return productService.deleteProduct(id);
    }


}
