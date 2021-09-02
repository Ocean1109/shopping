package com.example.demo.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * 阿里云OSS服务器工具类
 * @author huhaiyang
 */
@Component
public class OssUtil {
    protected static final Logger log = LoggerFactory.getLogger(OssUtil.class);

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;
    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    /**
     * 商品图片(包括概览图片和详细图片)
     */
    private final static String[] PRODUCT_IMAGES = {"productImages/","productDetailImages/"};


    /**
     * 单个文件上传
     * @param file
     * @return 返回完整URL地址
     */
    public String uploadFile(MultipartFile file, int whichPath) {
        String fileUrl = uploadImg(file, whichPath);
        String str = getFileUrl(fileUrl, whichPath);
        return str.trim();
    }

    /**
     * 单个文件上传(指定文件名（带后缀）)
     * @param file
     * @return 返回完整URL地址
     */
    public String uploadFile(MultipartFile file,String fileName, int whichPath) {
        try {
            InputStream inputStream = file.getInputStream();
            this.uploadFile(inputStream, fileName, whichPath);
            return fileName;
        }
        catch (Exception e) {
            return "上传失败";
        }
    }

    /**
     * 多文件上传
     * @param fileList
     * @return 返回完整URL，逗号分隔
     */
    public String uploadFile(List<MultipartFile> fileList, int whichPath) {
        String fileUrl = "";
        String str = "";
        String photoUrl = "";
        for(int i = 0;i< fileList.size();i++){
            fileUrl = uploadImg(fileList.get(i), whichPath);
            str = getFileUrl(fileUrl, whichPath);
            if(i == 0){
                photoUrl = str;
            }else {
                photoUrl += "," + str;
            }
        }
        return photoUrl.trim();
    }

    /**
     * 通过文件名获取文完整件路径
     * @param fileUrl
     * @return 完整URL路径
     */
    public String getFileUrl(String fileUrl, int whichPath) {
        if (fileUrl !=null && fileUrl.length()>0) {
            String[] split = fileUrl.split("/");
            String url = this.getUrl(PRODUCT_IMAGES[whichPath] + split[split.length - 1]);
            return url;
        }
        return null;
    }

    /**
     * @param url
     * @return
     */
    /**获取去掉参数的完整路径*/
    private String getShortUrl(String url) {
        String[] imgUrls = url.split("\\?");
        return imgUrls[0].trim();
    }

    /**
     * @param url
     * @return
     */
    /**获取带参数的完整路径*/
    private String getLongUrl(String url) {
        return url.trim();
    }

    /**
     * @param key
     * @return
     */
    /**获得url链接*/
    private String getUrl(String key) {
        // 设置URL过期时间为20年 3600l* 1000*24*365*20
        Date expiration = new Date(new Date().getTime() + 3600L * 1000 * 24 * 365 * 20);
        // 生成URL
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
        if (url != null) {
            return getLongUrl(url.toString());
        }
        return null;
    }

    /**
     * @param file
     * @return
     */
    /**上传文件*/
    private String uploadImg(MultipartFile file, int whichPath) {
        //1、限制最大文件为20M
        if (file.getSize() > 1024 * 1024 *20) {
            return "图片太大";
        }

        String fileName = file.getOriginalFilename();
        //文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        String uuid = UUID.randomUUID().toString();
        String name = uuid + suffix;

        try {
            InputStream inputStream = file.getInputStream();
            this.uploadFile(inputStream, name, whichPath);
            return name;
        }
        catch (Exception e) {
            return "上传失败";
        }
    }

    /**
     * @param instream
     * @param fileName
     * @return
     */
    /**上传文件（指定文件名）*/
    private String uploadFile(InputStream instream, String fileName, int whichPath) {
        String ret = "";
        try {
            //创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(instream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(getContentType(fileName.substring(fileName.lastIndexOf("."))));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            //上传文件
            OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            PutObjectResult putResult = ossClient.putObject(bucketName, PRODUCT_IMAGES[whichPath] + fileName, instream, objectMetadata);
            ret = putResult.getETag();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * @param objectName
     * @return
     */
    public Boolean deleteFile(String objectName){
        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
            ossClient.deleteObject(bucketName, objectName);
            // 关闭OSSClient。
            ossClient.shutdown();
            return true;
        }
        catch (Exception e){
            return false;
        }

    }

    /**
     * @param FilenameExtension
     * @return
     */
    private static String getContentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
                FilenameExtension.equalsIgnoreCase(".jpg") ||
                FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpeg";
        }
        if (FilenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase(".pptx") ||
                FilenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase(".docx") ||
                FilenameExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        //PDF
        if (FilenameExtension.equalsIgnoreCase(".pdf")) {
            return "application/pdf";
        }
        return "image/jpeg";
    }
}
