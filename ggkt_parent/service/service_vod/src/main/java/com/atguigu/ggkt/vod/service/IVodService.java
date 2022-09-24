package com.atguigu.ggkt.vod.service;

import java.io.InputStream;

/**
 * @author Tianjinfei
 * @Version 1.0
 */
public interface IVodService {
    String upload(InputStream inputStream, String originalFilename);

    void removeVideo(String videoSourceId);
}
