package com.atguigu.ggkt.live.service;


import com.atguigu.ggkt.model.live.LiveCourseConfig;
import com.atguigu.ggkt.vo.live.LiveCourseConfigVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 直播课程配置表 服务类
 * </p>
 *
 * @author tianjf
 * @since 2022-09-22
 */
public interface ILiveCourseConfigService extends IService<LiveCourseConfig> {

    LiveCourseConfig getLiveCourseConfig(Long courseId);


}
