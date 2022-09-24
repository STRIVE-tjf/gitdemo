package com.atguigu.ggkt.user.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Tianjinfei
 * @Version 1.0
 */
@Configuration
@MapperScan("com.atguigu.ggkt.user.mapper")
public class UserConfig {
    //创建分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }
}
