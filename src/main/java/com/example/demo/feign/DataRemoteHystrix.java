package com.example.demo.feign;

import org.springframework.stereotype.Component;

/**
 * @author huhaiyang
 * @date 2021/9/6
 */
@Component
public class DataRemoteHystrix implements DataRemote{
    @Override
    public String a() {
        return "熔断";
    }
}
