package com.atguigu.ggkt.live.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Tianjinfei
 * @Version 1.0
 * 直播模块相关配置类
 */

@Configuration
@MapperScan("com.atguigu.ggkt.live.mapper")
public class LiveConfig {
    //配置分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }
}
