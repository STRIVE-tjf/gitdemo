package com.atguigu.ggkt.live.controller;

import com.atguigu.ggkt.live.service.ILiveCourseAccountService;
import com.atguigu.ggkt.live.service.ILiveCourseConfigService;
import com.atguigu.ggkt.live.service.ILiveCourseService;
import com.atguigu.ggkt.model.live.LiveCourse;
import com.atguigu.ggkt.model.live.LiveCourseAccount;
import com.atguigu.ggkt.model.live.LiveCourseConfig;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vo.live.LiveCourseConfigVo;
import com.atguigu.ggkt.vo.live.LiveCourseFormVo;
import com.atguigu.ggkt.vo.live.LiveCourseVo;
import com.atguigu.ggkt.vo.vod.CourseFormVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 直播课程表 前端控制器
 * </p>
 *
 * @author tianjf
 * @since 2022-09-22
 */
@RestController
@RequestMapping("/admin/live/liveCourse")
public class LiveCourseController {
    //注入属性
    @Autowired
    private ILiveCourseService iLiveCourseService;
    @Autowired
    private ILiveCourseAccountService iLiveCourseAccountService;
    @Autowired
    private ILiveCourseConfigService iLiveCourseConfigService;

    //分页查询直播课程
    @ApiOperation("分页查询直播课程信息")
    @GetMapping("{page}/{limit}")
    public Result getLiveCourse(@PathVariable Long page,
                                @PathVariable Long limit){
        Page<LiveCourse> pageParam = new Page<>(page,limit);
        IPage<LiveCourse> pageModel = iLiveCourseService.selectPage(pageParam);
        return Result.ok(pageModel);
    }

    //添加直播课程
    @ApiOperation("添加直播课程")
    @PostMapping("save")
    public Result save(@RequestBody LiveCourseFormVo liveCourseFormVo){//从前端表单提交的内容获取参数
        iLiveCourseService.saveLive(liveCourseFormVo);
        return Result.ok(null);

    }
    //删除直播课程
    @ApiOperation("删除直播课程")
    @GetMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        iLiveCourseService.deleteCourse(id);
        return Result.ok(null);
    }
    //根据id获取直播课程的基本信息
    @ApiOperation("获取直播课程的基本信息")
    @GetMapping("get/{id}")
    public Result getById(@PathVariable Long id){
        LiveCourse liveCourse = iLiveCourseService.getById(id);
        return Result.ok(liveCourse);
    }
    //根据直播课程id获取课程基本信息和详情信息
    @ApiOperation("获取课程基本信息和详情信息")
    @GetMapping("getInfo/{id}")
    public Result getInfo(@PathVariable Long id){
        LiveCourseFormVo liveCourseFormVo = iLiveCourseService.getLiveCourseFormVo(id);
        return Result.ok(liveCourseFormVo);
    }
    //修改直播课程
    @ApiOperation("修改直播课程")
    @PutMapping("update")
    public Result update(@RequestBody LiveCourseFormVo liveCourseFormVo){
        iLiveCourseService.updateLiveCourse(liveCourseFormVo);
        return Result.ok(null);
    }

    //获取直播课程账号的信息
    @ApiOperation("获取直播课程账号")
    @GetMapping("getLiveCourseAccount/{id}")
    public Result getLiveCourseAccount(@PathVariable Long id){
        LiveCourseAccount liveCourseAccount = iLiveCourseAccountService.getCourseAccountByCourseId(id);
        return Result.ok(liveCourseAccount);
    }
    //根据课程id获取直播配置信息
    @ApiOperation("获取直播配置信息")
    @GetMapping("getCourseConfig/{id}")
    public Result getCourseConfig(@PathVariable Long id){
        LiveCourseConfigVo liveCourseConfigVo = iLiveCourseService.getCourseConfigByCourseId(id);
        return Result.ok(liveCourseConfigVo);
    }
    //修改直播配置信息
    @ApiOperation("修改直播配置信息")
    @PutMapping("updateConfig")
    public Result updateConfig(@RequestBody LiveCourseConfigVo liveCourseConfigVo){
        iLiveCourseService.updateConfig(liveCourseConfigVo);
        return Result.ok(null);
    }
    //获取最近直播课程
    @ApiOperation("获取最近直播课程")
    @GetMapping("findLatelyList")
    public Result findLatelyList(){
        List<LiveCourseVo> liveCourseVoList = iLiveCourseService.findLatelyList();
        return Result.ok(liveCourseVoList);
    }
}
