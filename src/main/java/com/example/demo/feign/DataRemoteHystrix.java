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
    @Override
    public List<AnalysisAction> analysisAction() {
        return null;
    }

    @Override
    public List<CompareNum> compareNum() {
        return null;
    }

    @Override
    public List<CompareAge> compareAge() {
        return null;
    }

    @Override
    public List<TopProductCategory> topProductCategory() {
        return null;
    }

    @Override
    public List<CompareProvince> compareProvince() {
        return null;
    }

    @Override
    public String a() {
        return "熔断";
    }
}
