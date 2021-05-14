package com.example.demo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Brand {
    private int id;
    private String brandName;
    private String description;

    public Brand(String brandName) {
        this.brandName = brandName;
    }
}
