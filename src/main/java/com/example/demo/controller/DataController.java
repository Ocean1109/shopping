package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.feign.DataRemote;
import com.example.demo.feign.TemporaryData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author huhaiyang
 * @date 2021/9/6
 */
@Controller
public class DataController {
    @Autowired
    DataRemote dataRemote;

    @GetMapping("/analysisAction")
    @ResponseBody
    public List<AnalysisAction> analysisAction() {
        List<AnalysisAction> result = dataRemote.analysisAction();
        TemporaryData.getTemporaryData().setDemoAnalysisAction(result);
        return result;
    }

    @GetMapping("/compareNum")
    @ResponseBody
    public List<CompareNum> compareNum(){
        List<CompareNum> result = dataRemote.compareNum();
        TemporaryData.getTemporaryData().setDemoCompareNum(result);
        return result;
    }

    @GetMapping("/compareAge")
    @ResponseBody
    public List<CompareAge> compareAge(){
        List<CompareAge> result = dataRemote.compareAge();
        TemporaryData.getTemporaryData().setDemoCompareAge(result);
        return result;
    }

    @GetMapping("/topProductCategory")
    @ResponseBody
    public List<TopProductCategory> topProductCategory(){
        List<TopProductCategory> result = dataRemote.topProductCategory();
        TemporaryData.getTemporaryData().setDemoTopProductCategory(result);
        return result;
    }

    @GetMapping("/compareProvince")
    @ResponseBody
    public List<CompareProvince> compareProvince(){
        List<CompareProvince> result = dataRemote.compareProvince();
        TemporaryData.getTemporaryData().setDemoCompareProvince(result);
        return result;
    }

    @GetMapping("/myHello")
    @ResponseBody
    public String index() {
        return dataRemote.a();
    }
}
