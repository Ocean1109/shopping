package com.example.demo.service.implement;

import com.alibaba.fastjson.JSON;
import com.example.demo.entity.Product;
import com.example.demo.mapper.ProductMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@WebAppConfiguration
class ProductServiceImpTest {

    @Autowired
    ProductMapper productMapper;
    @Test
    void deleteProduct() {
        Product deleteProduct=productMapper.selectById(3);
        String url=deleteProduct.getProductImage();
        url=url.replaceFirst("(.*)//ocean1109.oss-cn-beijing.aliyuncs.com/"," ");
        String[] split = url.split("\\?");
        System.out.println(split[0]);
    }

}
