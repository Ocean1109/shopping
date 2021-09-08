package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author huhaiyang
 */
@Mapper
@Repository
public interface ProductMapper extends BaseMapper<Product> {
}
