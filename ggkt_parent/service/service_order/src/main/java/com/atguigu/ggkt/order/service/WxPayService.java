package com.atguigu.ggkt.order.service;

import java.util.Map;

/**
 * @author Tianjinfei
 * @Version 1.0
 */
public interface WxPayService {
    Object createJsapi(String orderNo);

    Map<String, String> queryPayStatus(String orderNo);
}
