package com.atguigu.ggkt.vod.controller;

import com.atguigu.ggkt.model.vod.Course;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vo.vod.CourseFormVo;
import com.atguigu.ggkt.vo.vod.CoursePublishVo;
import com.atguigu.ggkt.vo.vod.CourseQueryVo;
import com.atguigu.ggkt.vod.service.ICourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-09-09
 */
@Api(tags = "课程列表接口")
@RestController
@RequestMapping("/admin/vod/course")
//@CrossOrigin//处理跨域
public class CourseController {
    //注入依赖
    @Autowired
    private ICourseService iCourseService;

    //课程分类列表
    @ApiOperation("分页查询课程分类列表")
    @GetMapping("{page}/{limit}")//page:当前页,limit:每页显示多少条记录
    public Result findPage(@PathVariable Long page,
                           @PathVariable Long limit,
                           CourseQueryVo courseQueryVo){
        Page<Course> pageParam = new Page<>(page,limit);
        Map<String,Object> map = iCourseService.findPages(pageParam,courseQueryVo);
        return Result.ok(map);
    }
    //添加课程
    @ApiOperation("添加课程")
    @PostMapping("save")
    public Result add(@RequestBody CourseFormVo courseFormVo){
        Long courseId = iCourseService.addCourse(courseFormVo);
        return Result.ok(courseId);
    }
    //根据id获取课程信息
    @ApiOperation("根据id获取课程信息")
    @GetMapping("get/{id}")
    public Result getById(@PathVariable Long id){
        CourseFormVo courseFormVo = iCourseService.getCourseById(id);
        return Result.ok(courseFormVo);
    }
    //根据id获取到课程信息过后进行修改操作
    @ApiOperation("修改课程")
    @PostMapping("update")
    public Result updateById(@RequestBody CourseFormVo courseFormVo){
        iCourseService.updateCourseFormVo(courseFormVo);
        return Result.ok(courseFormVo.getId());
    }

    //根据id查询将要发布的课程的信息,以便做最后的确认
    @ApiOperation("根据id查询将要发布的课程的信息")
    @GetMapping("getCoursePublishVo/{id}")
    public Result getCoursePublishVo(@PathVariable Long id){
        CoursePublishVo coursePublishVo = iCourseService.getCoursePublishVo(id);
        return Result.ok(coursePublishVo);
    }
    //最终发布课程
    @ApiOperation("最终发布课程")
    @PutMapping("publishCourseById/{id}")
    public Result publishCourse(@PathVariable Long id){
        iCourseService.publishCourseById(id);
        return Result.ok(null);
    }
    //删除课程
    @ApiOperation("删除课程")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        iCourseService.removeCourse(id);
        return Result.ok(null);
    }
    //查询全部直播课程
    @GetMapping("findAll")
    public Result findAll() {
        List<Course> list = iCourseService.findlist();
        return Result.ok(list);
    }
}
