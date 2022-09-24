package com.atguigu.ggkt.live.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author Tianjinfei
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "mtcloud")//读取配置文件中以mtcloud开头的内容
public class MTCloudAccountConfig {

    private String openId;
    private String openToken;

}
