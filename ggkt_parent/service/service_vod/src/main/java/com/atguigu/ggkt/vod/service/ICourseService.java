package com.atguigu.ggkt.vod.service;

import com.atguigu.ggkt.model.vod.Course;
import com.atguigu.ggkt.vo.vod.CourseFormVo;
import com.atguigu.ggkt.vo.vod.CoursePublishVo;
import com.atguigu.ggkt.vo.vod.CourseQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-09-09
 */
public interface ICourseService extends IService<Course> {

    Map<String, Object> findPages(Page<Course> pageParam, CourseQueryVo courseQueryVo);


    Long addCourse(CourseFormVo courseFormVo);

    CourseFormVo getCourseById(Long id);

    void updateCourseFormVo(CourseFormVo courseFormVo);

    CoursePublishVo getCoursePublishVo(Long id);

    void publishCourseById(Long id);

    void removeCourse(Long id);

    Map<String, Object> findCoursePage(Page<Course> pageParam, CourseQueryVo courseQueryVo);

    Map<String, Object> getInfoByCourseId(Long courseId);

    List<Course> findlist();
}
