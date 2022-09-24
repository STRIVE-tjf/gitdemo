package com.atguigu.ggkt.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Tianjinfei
 * @Version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient//在nacos里面进行注册
@EnableFeignClients(basePackages = "com.atguigu")//远程调用接口
public class ServiceOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceOrderApplication.class,args);
    }
}
