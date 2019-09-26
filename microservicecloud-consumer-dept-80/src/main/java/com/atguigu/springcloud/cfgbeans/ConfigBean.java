package com.atguigu.springcloud.cfgbeans;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author Administrator
 */
@Configuration
public class ConfigBean {
    @Bean(name = "restTemplate")
    @LoadBalanced//springcloud ribbon是基于netflix实现的一套客户端负载均衡的工具
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }


}
