package com.atguigu.ggkt.wechat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Tianjinfei
 * @Version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient//在Nacos中进行注册
@EnableFeignClients(basePackages = "com.atguigu")//远程调用
@ComponentScan(basePackages = "com.atguigu")

public class ServiceWechatApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceWechatApplication.class,args);
    }
}
