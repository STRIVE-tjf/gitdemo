package com.atguigu.ggkt.vod.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Tianjinfei
 * @Version 1.0
 * 实现InitializingBean在bean创建之前读取配置文件里面的值
 */
@Component
public class ConstantPropertiesUtils implements InitializingBean {
    @Value("${tencent.cos.file.region}")
    private String region;
    @Value("${tencent.cos.file.secretid}")
    private String secretId;
    @Value("${tencent.cos.file.secretkey}")
    private String secretKey;
    @Value("${tencent.cos.file.bucketname}")
    private String bucketName;

    public static String END_POINT;
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String BUCKET_NAME;

    @Override
    public void afterPropertiesSet() throws Exception {
        END_POINT = region;
        ACCESS_KEY_ID = secretId;
        ACCESS_KEY_SECRET = secretKey;
        BUCKET_NAME = bucketName;
    }
}
