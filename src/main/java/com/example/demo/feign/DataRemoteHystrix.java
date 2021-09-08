package com.example.demo.feign;

import com.example.demo.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author huhaiyang
 * @date 2021/9/6
 */
@Component
public class DataRemoteHystrix implements DataRemote{
    private TemporaryData temporaryData = TemporaryData.getTemporaryData();
    @Override
    public List<AnalysisAction> analysisAction() {
        return temporaryData.getDemoAnalysisAction();
    }

    @Override
    public List<CompareNum> compareNum() {
        return temporaryData.getDemoCompareNum();
    }

    @Override
    public List<CompareAge> compareAge() {
        return temporaryData.getDemoCompareAge();
    }

    @Override
    public List<TopProductCategory> topProductCategory() {
        return temporaryData.getDemoTopProductCategory();
    }

    @Override
    public List<CompareProvince> compareProvince() {
        return temporaryData.getDemoCompareProvince();
    }

    @Override
    public String a() {
        return "熔断";
    }
}
