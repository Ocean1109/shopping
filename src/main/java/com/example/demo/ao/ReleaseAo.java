package com.example.demo.ao;

import lombok.Data;

@Data
public class ReleaseAo {
    private String productDesc;
    private Double productPrice;
    private String productType;
    private String brand;
    private String user;//应该是token，后端需要解析为userId
    private String productAddress;
    private int numbers;
    private String rule;//如"书籍出版社 书籍页数"
    private String productRule;//如"人民出版社 100页"
}
