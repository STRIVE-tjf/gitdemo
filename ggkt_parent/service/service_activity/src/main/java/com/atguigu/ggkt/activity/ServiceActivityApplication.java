package com.atguigu.ggkt.activity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Tianjinfei
 * @Version 1.0
 */
@SpringBootApplication//当前模块启动类
@EnableDiscoveryClient//在Nacos中进行注册
@EnableFeignClients(basePackages = "com.atguigu")//远程调用
public class ServiceActivityApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceActivityApplication.class,args);
    }
}
