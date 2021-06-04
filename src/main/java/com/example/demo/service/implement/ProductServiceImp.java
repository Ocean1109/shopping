package com.example.demo.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.ao.ReleaseAo;
import com.example.demo.entity.Brand;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductRule;
import com.example.demo.entity.ProductType;
import com.example.demo.mapper.BrandMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.ProductRuleMapper;
import com.example.demo.mapper.ProductTypeMapper;
import com.example.demo.service.ProductService;
import com.example.demo.service.TokenService;
import com.example.demo.util.OssUtil;
import com.example.demo.vo.BaseVo;
import com.example.demo.vo.ProductVo;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    ProductMapper productMapper;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private ProductTypeMapper productTypeMapper;
    @Autowired
    private ProductRuleMapper productRuleMapper;
    @Autowired
    private TokenService tokenService;
    @Autowired
    OssUtil ossUtil; //注入OssUtil

    /**
     * @param productType
     * @return
     */
    @Override
    public List<ProductVo> showProduct(String productType){
        List<ProductVo> allProduct=new ArrayList<>();
        QueryWrapper<Product> queryWrapper=new QueryWrapper<>();
        if(productType.equals("所有")){
            queryWrapper.select("id","product_desc","product_image","product_price");
        }else{
            QueryWrapper<ProductType> productTypeQueryWrapper=new QueryWrapper<>();
            productTypeQueryWrapper.eq("type_name",productType);
            ProductType searchProductType=productTypeMapper.selectOne(productTypeQueryWrapper);
            queryWrapper.select("id","product_desc","product_image","product_price").eq("product_type_id",searchProductType.getId());
        }
        List<Product> products=productMapper.selectList(queryWrapper);
        for(Product product:products){
            allProduct.add(new ProductVo(product.getId(),product.getProductDesc(),product.getProductImage(),product.getProductPrice()));
        }
        return allProduct;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Product showSingleProduct(int id) {
        Product product=new Product();
        product=productMapper.selectById(id);
        return product;
    }

    /**
     * @param productImage,releaseProduct
     * @param releaseProduct
     * @return
     */
    @Override
    public Boolean releaseProduct(MultipartFile productImage, ReleaseAo releaseProduct){
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
        //查看是否存在brand，存在则填入相应的id，不存在则新增一个brand,并且填入id
        QueryWrapper<Brand> brandQueryWrapper=new QueryWrapper<>();
        brandQueryWrapper.eq("brand_name",releaseProduct.getBrand());
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
        productTypeQueryWrapper.eq("type_name",releaseProduct.getProductType());
        ProductType productType=productTypeMapper.selectOne(productTypeQueryWrapper);
        if(productType!=null){//存在
            product.setProductTypeId(productType.getId());
        }else{
            ProductType newProductType=new ProductType(releaseProduct.getProductType(),1,time,userId,time,userId);
            productTypeMapper.insert(newProductType);
            product.setProductTypeId(newProductType.getId());
        }
        //查看是否存在rule，存在则填入相应的id，不存在则新增一个rule,并且填入id
        QueryWrapper<ProductRule> productRuleQueryWrapper=new QueryWrapper<>();
        productRuleQueryWrapper.eq("rule",releaseProduct.getRule());
        ProductRule productRule=productRuleMapper.selectOne(productRuleQueryWrapper);
        if(productRule!=null){//存在
            product.setProductRuleId(productRule.getId());
        }else{
            ProductRule newProductRule=new ProductRule(userId,releaseProduct.getRule());
            productRuleMapper.insert(newProductRule);
            product.setProductRuleId(newProductRule.getId());
        }
        String url = ossUtil.uploadFile(productImage);
        product.setProductImage(url);
        int result=productMapper.insert(product);
        if(result==1){
            return true;
        }else{
            return false;
        }
    }

    /**
     * @param id
     * @return
     */
    @Override
    public BaseVo deleteProduct(int id) {
        BaseVo baseVo=new BaseVo();
        try{
            Product deleteProduct=productMapper.selectById(id);
            String url=deleteProduct.getProductImage();
            url=url.replaceFirst("(.*)//ocean1109.oss-cn-beijing.aliyuncs.com/"," ");
            String[] split = url.split("\\?");
            if(ossUtil.deleteFile(split[0])){
                productMapper.deleteById(id);
                baseVo.setCode(0);
                baseVo.setMessage("删除成功");
                return baseVo;
            }else{
                baseVo.setCode(1);
                baseVo.setMessage("删除失败");
                return baseVo;
            }
        }
        catch (Exception e){
            baseVo.setCode(1);
            baseVo.setMessage(e.getMessage());
            return baseVo;
        }

    }
}
