package com.example.demo.controller;

import com.example.demo.entity.AnalysisAction;
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

    @GetMapping("/myHello")
    @ResponseBody
    public String index() {
        return dataRemote.a();
    }
}
