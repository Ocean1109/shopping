package com.example.demo.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author huhaiyang
 * @date 2021/9/6
 */
@FeignClient(name = "spring-cloud-producer")
public interface DataRemote {
    @RequestMapping("/hello")
    String a();
}
