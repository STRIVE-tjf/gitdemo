package com.atguigu.ggkt.vod.service;

import com.atguigu.ggkt.model.vod.Video;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-09-09
 */
public interface IVideoService extends IService<Video> {

    void removeByCourseId(Long id);
    //根据小节id删除视频
    void removeVideoById(Long id);

    Map<String, Object> getPlayAuth(Long courseId, Long videoId);
    //根据课程id删除视频
    //void removeVideoByCourseId(Long id);
}
