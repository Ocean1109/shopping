package com.example.demo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ocean
 * @date 2021/9/7
 */
@Data
@NoArgsConstructor
public class CompareAge {
    private String gender;
    private String ageRange;
    private int count;
}
