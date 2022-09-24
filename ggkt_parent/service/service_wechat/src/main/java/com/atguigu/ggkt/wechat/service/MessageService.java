package com.atguigu.ggkt.wechat.service;

import java.util.Map;

/**
 * @author Tianjinfei
 * @Version 1.0
 */
public interface MessageService {
    String receiveMessage(Map<String, String> param);

    void pushPayMessage(Long id);
}
