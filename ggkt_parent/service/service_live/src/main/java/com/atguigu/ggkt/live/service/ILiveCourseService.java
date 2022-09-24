package com.atguigu.ggkt.live.service;


import com.alibaba.fastjson.JSONObject;
import com.atguigu.ggkt.model.live.LiveCourse;
import com.atguigu.ggkt.vo.live.LiveCourseConfigVo;
import com.atguigu.ggkt.vo.live.LiveCourseFormVo;
import com.atguigu.ggkt.vo.live.LiveCourseVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 直播课程表 服务类
 * </p>
 *
 * @author tianjf
 * @since 2022-09-22
 */
public interface ILiveCourseService extends IService<LiveCourse> {

    IPage<LiveCourse> selectPage(Page<LiveCourse> pageParam);

    void saveLive(LiveCourseFormVo liveCourseFormVo);

    void deleteCourse(Long id);

    LiveCourseFormVo getLiveCourseFormVo(Long id);

    void updateLiveCourse(LiveCourseFormVo liveCourseFormVo);

    LiveCourseConfigVo getCourseConfigByCourseId(Long id);

    void updateConfig(LiveCourseConfigVo liveCourseConfigVo);

    List<LiveCourseVo> findLatelyList();

    JSONObject getPlayAuth(Long id, Long userId);

    Map<String, Object> getInfoById(Long courseId);
}
