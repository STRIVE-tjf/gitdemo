package com.atguigu.ggkt.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Tianjinfei
 * @Version 1.0
 */
@SpringBootApplication//springboot启动类
@EnableDiscoveryClient//在Nacos中进行注册
public class ServiceUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceUserApplication.class,args);
    }
}
