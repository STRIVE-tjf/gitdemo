package com.atguigu.ggkt.vod.controller;

import com.atguigu.ggkt.model.vod.Video;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vod.service.IVideoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-09-09
 */
@RestController
@RequestMapping("/admin/vod/video")
//@CrossOrigin//处理跨域
public class VideoController {
    @Autowired
    private IVideoService iVideoService;
    //根据id获取小节
    @ApiOperation("根据id获取小节")
    @GetMapping("get/{id}")
    public Result getById(@PathVariable Long id){
        Video video = iVideoService.getById(id);
        return Result.ok(video);
    }
    //新增小节
    @ApiOperation("添加小节")
    @PostMapping("save")
    public Result addVideo(@RequestBody Video video){
        iVideoService.save(video);
        return Result.ok(null);
    }
    //修改小节
    @ApiOperation("修改小节")
    @PostMapping("update")
    public Result update(@RequestBody Video video){
        iVideoService.updateById(video);
        return Result.ok(null);
    }
    //删除小节
    @ApiOperation("根据id删除小节")
    @DeleteMapping("remove/{id}")
    public Result removeById(@PathVariable Long id){
        //iVideoService.removeById(id);
        //根据课程id删除视频
        //iVideoService.removeVideoByCourseId(id);
        //根据小节id删除视频
        iVideoService.removeVideoById(id);
        return Result.ok(null);
    }
}
