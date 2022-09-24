package com.atguigu.ggkt.live.service;


import com.atguigu.ggkt.model.live.LiveCourseDescription;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程简介 服务类
 * </p>
 *
 * @author tianjf
 * @since 2022-09-22
 */
public interface ILiveCourseDescriptionService extends IService<LiveCourseDescription> {

    LiveCourseDescription getCourseDescription(Long id);
}
