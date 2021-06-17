package com.example.demo.service.implement;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.ao.ReleaseAo;
import com.example.demo.entity.*;
import com.example.demo.mapper.*;
import com.example.demo.service.ProductService;
import com.example.demo.service.TokenService;
import com.example.demo.util.OssUtil;
import com.example.demo.vo.BaseVo;
import com.example.demo.vo.ProductDetailVo;
import com.example.demo.vo.ProductVo;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.Port;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    ProductImageMapper productImageMapper;
    @Autowired
    ImageMapper imageMapper;
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
    public List<ProductVo> showProduct(String productType,String brand,String address){
        List<ProductVo> allProduct=new ArrayList<>();
        QueryWrapper<Product> queryWrapper=new QueryWrapper<>();
        if(productType.equals("全部")){
            queryWrapper.select("id","product_desc","product_image","product_price");
        }else{
            if(brand.equals("全部")&&address.equals("全部")){
                QueryWrapper<ProductType> productTypeQueryWrapper=new QueryWrapper<>();
                productTypeQueryWrapper.eq("type_name",productType);
                ProductType searchProductType=productTypeMapper.selectOne(productTypeQueryWrapper);
                queryWrapper.select("id","product_desc","product_image","product_price").eq("product_type_id",searchProductType.getId());
            }else if(brand.equals("全部")&&!address.equals("全部")){//address需要进行选择
                queryWrapper.select("id","product_desc","product_image","product_price").eq("product_address",address);
            }else if(!brand.equals("全部")&&address.equals("全部")){//brand需要进行选择
                QueryWrapper<Brand> brandQueryWrapper=new QueryWrapper<>();
                brandQueryWrapper.eq("brand_name",brand);
                Brand searchBrand=brandMapper.selectOne(brandQueryWrapper);
                queryWrapper.select("id","product_desc","product_image","product_price").eq("brand_id",searchBrand.getId());
            }else{//address和brand需要进行选择
                QueryWrapper<Brand> brandQueryWrapper=new QueryWrapper<>();
                brandQueryWrapper.eq("brand_name",brand);
                Brand searchBrand=brandMapper.selectOne(brandQueryWrapper);
                queryWrapper.select("id","product_desc","product_image","product_price").eq("brand_id",searchBrand.getId()).eq("product_address",address);
            }
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
    public ProductDetailVo showSingleProduct(int id) {
        Product product=productMapper.selectById(id);
        ProductDetailVo productDetailVo = new ProductDetailVo();
        //获取商品的类别名字
        QueryWrapper<ProductType> productTypeQueryWrapper=new QueryWrapper<>();
        productTypeQueryWrapper.eq("id",product.getProductTypeId());
        ProductType productType=productTypeMapper.selectOne(productTypeQueryWrapper);
        //获取商品的品牌名字
        QueryWrapper<Brand> brandQueryWrapper=new QueryWrapper<>();
        brandQueryWrapper.eq("id",product.getBrandId());
        Brand brand=brandMapper.selectOne(brandQueryWrapper);
        //获取商品的详细图片
        QueryWrapper<ProductImage> productImageQueryWrapper=new QueryWrapper<>();
        productImageQueryWrapper.eq("product_id",id);
        List<ProductImage> productImageList=productImageMapper.selectList(productImageQueryWrapper);
        List<String> moreImages=null;
        if(productImageList.size()!=0){
            List<Integer> imageId=new ArrayList<>();
            for(ProductImage productImage:productImageList){
                imageId.add(productImage.getImageId());
            }
            moreImages=new ArrayList<>();
            QueryWrapper<Image> imageQueryWrapper=new QueryWrapper<>();
            imageQueryWrapper.in("id",imageId);
            List<Image> imageList=imageMapper.selectList(imageQueryWrapper);
            for(Image image:imageList){
                moreImages.add(image.getImageUrl());
            }
        }
        //获取商品的规则
        QueryWrapper<ProductRule> productRuleQueryWrapper=new QueryWrapper<>();
        productRuleQueryWrapper.eq("id",product.getProductRuleId());
        ProductRule productRule=productRuleMapper.selectOne(productRuleQueryWrapper);
        String allRule=productRule.getRule();
        List<String> resultRule=null;
        if(allRule!=null){
            String[] splitRule=allRule.split(" ");
            resultRule= Arrays.asList(splitRule);
        }
        //将商品规则具体信息转换为List
        String allProductRule=product.getProductRule();
        List<String> resultProductRule=null;
        if(allProductRule!=null){
            String[] splitProductRule=allProductRule.split(" ");
            resultProductRule=Arrays.asList(splitProductRule);
        }
        //向最终返回的实体中添加消息
        productDetailVo.setProductDesc(product.getProductDesc());
        productDetailVo.setProductImage(product.getProductImage());
        productDetailVo.setMoreImages(moreImages);
        productDetailVo.setProductPrice(product.getProductPrice());
        productDetailVo.setProductType(productType.getTypeName());
        productDetailVo.setProductBrand(brand.getBrandName());
        productDetailVo.setPublishUserId(product.getPublishUserId());
        productDetailVo.setProductAddress(product.getProductAddress());
        productDetailVo.setNumbers(product.getNumbers());
        productDetailVo.setRule(resultRule);
        productDetailVo.setProductRule(resultProductRule);
        return productDetailVo;
    }

    /**
     * @param productImage,releaseProduct
     * @param releaseProduct
     * @return
     */
    @Override
    public Boolean releaseProduct(MultipartFile productImage, List<MultipartFile> moreImages,ReleaseAo releaseProduct){
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
            for(MultipartFile moreImage:moreImages){
                String moreUrl=ossUtil.uploadFile(moreImage);
                Image image=new Image(moreUrl);
                imageMapper.insert(image);
                ProductImage productImage1=new ProductImage(image.getId(),product.getId());
                productImageMapper.insert(productImage1);
            }
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
            url=url.replaceFirst("(.*)//ocean1109.oss-cn-beijing.aliyuncs.com/","");
            String[] split = url.split("\\?");
            if(ossUtil.deleteFile(split[0])){
                //从product表中删除
                productMapper.deleteById(id);
                //先从product_image表中找到之后要删除的imageId，再删除
                QueryWrapper<ProductImage> productImageQueryWrapper=new QueryWrapper<>();
                productImageQueryWrapper.eq("product_id",id);
                List<ProductImage> deleteProductImage=productImageMapper.selectList(productImageQueryWrapper);
                productImageMapper.delete(productImageQueryWrapper);
                List<Integer> deleteImageId=new ArrayList<>();
                for(ProductImage productImage:deleteProductImage){
                    deleteImageId.add(productImage.getImageId());
                }
                QueryWrapper<Image> imageQueryWrapper=new QueryWrapper<>();
                imageQueryWrapper.in("id",deleteImageId);
                List<Image> deleteImages=imageMapper.selectList(imageQueryWrapper);
                for(Image deleteImage:deleteImages){
                    String urlImage=deleteImage.getImageUrl();
                    url=url.replaceFirst("(.*)//ocean1109.oss-cn-beijing.aliyuncs.com/","");
                    String[] splitImage = url.split("\\?");
                    ossUtil.deleteFile(splitImage[0]);
                }
                imageMapper.delete(imageQueryWrapper);
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

    @Override
    public List<ProductVo> search(String productName){
        QueryWrapper<Product> productQueryWrapper=new QueryWrapper<>();
        productQueryWrapper.like("product_desc",productName);
        List<Product> searchProducts=productMapper.selectList(productQueryWrapper);
        List<ProductVo> result=new ArrayList<>();
        for(Product product:searchProducts){
            result.add(new ProductVo(product.getId(),product.getProductDesc(),product.getProductImage(),product.getProductPrice()));
        }
        return result;
    }

    @Override
    public List<ProductVo> listBusinessman(String token){
        int userID=Integer.parseInt(tokenService.getUseridFromToken(token));
        QueryWrapper<Product> productQueryWrapper=new QueryWrapper<>();
        productQueryWrapper.eq("publish_user_id",userID);
        List<Product> products=productMapper.selectList(productQueryWrapper);
        List<ProductVo> result=new ArrayList<>();
        for(Product product:products){
            result.add(new ProductVo(product.getId(),product.getProductDesc(),product.getProductImage(),product.getProductPrice()));
        }
        return result;
    }
}
