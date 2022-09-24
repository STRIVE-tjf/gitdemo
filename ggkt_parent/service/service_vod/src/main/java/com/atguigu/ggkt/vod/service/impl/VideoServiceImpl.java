package com.atguigu.ggkt.vod.service.impl;

import com.atguigu.ggkt.exception.GgktException;
import com.atguigu.ggkt.model.vod.Video;
import com.atguigu.ggkt.vod.mapper.VideoMapper;
import com.atguigu.ggkt.vod.service.IVideoService;
import com.atguigu.ggkt.vod.service.IVodService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-09-09
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements IVideoService {
    @Autowired
    private IVodService iVodService;
    @Autowired
    private IVideoService iVideoService;
    @Value("${tencent.video.appId}")
    private String appId;
    /*@Override
    public void removeByCourseId(Long id) {
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id",id);
        baseMapper.delete(videoQueryWrapper);
    }*/
    //根据课程id删除视频
    @Override
    public void removeByCourseId(Long id) {
        //根据课程id获取所有小节列表
        // 1.封装查询条件构造器
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",id);
        List<Video> videoList = baseMapper.selectList(queryWrapper);
        for (Video video :videoList) {
            //根据小节id获取视频的id
            String videoSourceId = video.getVideoSourceId();
            if (!StringUtils.isEmpty(videoSourceId)){
                iVodService.removeVideo(videoSourceId);
            }
        }
        //根据课程id删除小节
        baseMapper.delete(queryWrapper);
    }

    //根据小节id删除视频
    @Override
    public void removeVideoById(Long id) {
        Video video = baseMapper.selectById(id);
        String videoSourceId = video.getVideoSourceId();
        if (!StringUtils.isEmpty(videoSourceId)){
            iVodService.removeVideo(videoSourceId);
        }
        //根据id删除小节
        baseMapper.deleteById(id);
    }

    //获取腾讯云点播授权
    @Override
    public Map<String, Object> getPlayAuth(Long courseId, Long videoId) {
        //获取小节
        Video video = iVideoService.getById(videoId);
        if (video == null){
            throw new GgktException(20001,"没有相关小节信息");
        }
        Map<String,Object> map = new HashMap<>();
        map.put("videoSourceId",video.getVideoSourceId());
        map.put("appId",appId);
        return map;
    }
}
