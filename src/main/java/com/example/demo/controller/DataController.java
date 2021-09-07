package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.feign.DataRemote;
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
        return dataRemote.analysisAction();
    }

    @GetMapping("/compareNum")
    @ResponseBody
    public List<CompareNum> compareNum(){
        return dataRemote.compareNum();
    }

    @GetMapping("/compareAge")
    @ResponseBody
    public List<CompareAge> compareAge(){
        return dataRemote.compareAge();
    }

    @GetMapping("/topProductCategory")
    @ResponseBody
    public List<TopProductCategory> topProductCategory(){
        return dataRemote.topProductCategory();
    }

    @GetMapping("/compareProvince")
    @ResponseBody
    public List<CompareProvince> compareProvince(){
        return dataRemote.compareProvince();
    }

    @GetMapping("/myHello")
    @ResponseBody
    public String index() {
        return dataRemote.a();
    }
}
