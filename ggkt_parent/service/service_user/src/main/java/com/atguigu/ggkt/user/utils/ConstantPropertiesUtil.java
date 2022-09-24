package com.atguigu.ggkt.user.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Tianjinfei
 * @Version 1.0
 */
@Component
public class ConstantPropertiesUtil implements InitializingBean {
    @Value("${wechat.mpAppId}")
    private String appId;
    @Value("${wechat.mpAppSecret}")
    private String appSecret;

    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    @Override
    public void afterPropertiesSet() throws Exception {
        ACCESS_KEY_ID=appId;
        ACCESS_KEY_SECRET=appSecret;
    }
}
