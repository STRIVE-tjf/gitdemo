package com.atguigu.ggkt.vod.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Tianjinfei
 * @Version 1.0
 */
public interface FileService {
    String upload(MultipartFile file);
}
