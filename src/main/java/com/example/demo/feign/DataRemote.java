package com.example.demo.feign;

import com.example.demo.entity.AnalysisAction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author huhaiyang
 * @date 2021/9/6
 */
@FeignClient(name = "spring-cloud-producer",fallback = DataRemoteHystrix.class)
public interface DataRemote {

    @RequestMapping("/analysisAction")
    List<AnalysisAction> analysisAction();

    @RequestMapping("/hello")
    String a();
}
