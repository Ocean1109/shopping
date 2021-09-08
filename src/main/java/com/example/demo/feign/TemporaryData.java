package com.example.demo.feign;

import com.example.demo.entity.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ocean
 * @date 2021/9/7
 */
@Data
public class TemporaryData {
    private volatile static TemporaryData temporaryData;
    private List<AnalysisAction> demoAnalysisAction = new ArrayList<>();
    private List<CompareNum> demoCompareNum  = new ArrayList<>();
    private List<CompareAge> demoCompareAge  = new ArrayList<>();
    private List<TopProductCategory> demoTopProductCategory  = new ArrayList<>();
    private List<CompareProvince> demoCompareProvince  = new ArrayList<>();
    private TemporaryData(){}
    public static TemporaryData getTemporaryData(){
        if(temporaryData == null){
            synchronized (TemporaryData.class){
                if(temporaryData == null){
                    temporaryData = new TemporaryData();
                }
            }
        }
        return temporaryData;
    }
}
