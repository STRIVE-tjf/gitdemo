package com.atguigu.ggkt.vod.api;

import com.atguigu.ggkt.model.vod.Course;
import com.atguigu.ggkt.model.vod.Teacher;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vo.vod.CourseQueryVo;
import com.atguigu.ggkt.vo.vod.CourseVo;
import com.atguigu.ggkt.vod.service.ICourseService;
import com.atguigu.ggkt.vod.service.ITeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author Tianjinfei
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/vod/course")
public class CourseApiController {
    @Autowired
    private ICourseService iCourseService;
    @Autowired
    private ITeacherService iTeacherService;

    //根据课程id查询课程(被service_order模块远程调用)
    @GetMapping("inner/getById/{courseId}")
    //放便远程调用,返回值就不需要写Result
    public Course getById(@PathVariable Long courseId){
        Course course = iCourseService.getById(courseId);
        return course;
    }

    //查询课程列表(分页)
    @ApiOperation("查询课程列表")
    @GetMapping("{subjectParentId}/{page}/{limit}")
    public Result findCourse(@PathVariable Long subjectParentId,//课程一级分类id
                             @PathVariable Long page,//当前页
                             @PathVariable Long limit){
        CourseQueryVo courseQueryVo = new CourseQueryVo();
        courseQueryVo.setSubjectParentId(subjectParentId);
        Page<Course> pageParam = new Page<>(page,limit);
        Map<String,Object> map = iCourseService.findCoursePage(pageParam,courseQueryVo);
        return Result.ok(map);
    }
    //根据id查询课程
    @ApiOperation("根据id查询课程")
    @GetMapping("getInfo/{courseId}")
    public Result getByCourseId(@PathVariable Long courseId){
        Map<String,Object> map = iCourseService.getInfoByCourseId(courseId);
        return Result.ok(map);
    }

    @ApiOperation("根据关键字查询课程")
    @GetMapping("inner/findByKeyword/{keyword}")
    public List<Course> findByKeyword(@PathVariable String keyword){
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title",keyword);
        List<Course> list = iCourseService.list(queryWrapper);
        return list;
    }

    //根据id获取讲师信息(用于远程调用)
    @ApiOperation("根据id获取讲师信息")
    @GetMapping("inner/getTeacher/{id}")
    public Teacher getTeacherInfo(@PathVariable Long id){
        Teacher teacher = iTeacherService.getById(id);
        return teacher;
    }
}
