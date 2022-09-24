package com.atguigu.ggkt.live.service.impl;


import com.atguigu.ggkt.live.mapper.LiveCourseDescriptionMapper;
import com.atguigu.ggkt.live.service.ILiveCourseDescriptionService;
import com.atguigu.ggkt.model.live.LiveCourseDescription;
import com.atguigu.ggkt.model.vod.CourseDescription;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程简介 服务实现类
 * </p>
 *
 * @author tianjf
 * @since 2022-09-22
 */
@Service
public class LiveCourseDescriptionServiceImpl extends ServiceImpl<LiveCourseDescriptionMapper, LiveCourseDescription> implements ILiveCourseDescriptionService {
    //根据id获取直播课程详情信息
    @Override
    public LiveCourseDescription getCourseDescription(Long id) {
        LambdaQueryWrapper<LiveCourseDescription> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LiveCourseDescription::getLiveCourseId,id);
        LiveCourseDescription liveCourseDescription = baseMapper.selectOne(queryWrapper);
        return liveCourseDescription;
    }
}
