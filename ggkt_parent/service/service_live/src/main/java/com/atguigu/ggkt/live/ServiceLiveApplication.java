package com.atguigu.ggkt.live;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Tianjinfei
 * @Version 1.0
 */
@SpringBootApplication//主启动类
@EnableDiscoveryClient//在Nacos中进行注册
@EnableFeignClients(basePackages = "com.atguigu")//远程调用接口
@ComponentScan(basePackages = "com.atguigu")//组件扫描
public class ServiceLiveApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceLiveApplication.class,args);
    }
}
