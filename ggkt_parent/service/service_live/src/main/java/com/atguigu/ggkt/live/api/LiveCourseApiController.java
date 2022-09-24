package com.atguigu.ggkt.live.api;

import com.atguigu.ggkt.live.service.ILiveCourseService;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.utils.AuthContextHolder;
import com.atguigu.ggkt.vo.live.LiveCourseVo;
import io.swagger.annotations.ApiOperation;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Tianjinfei
 * @Version 1.0
 * 与微信建立连接
 */
@RestController
@RequestMapping("api/live/liveCourse")
public class LiveCourseApiController {
    @Autowired
    private ILiveCourseService iLiveCourseService;
    @ApiOperation("获取Access_token")
    @GetMapping("getPlayAuth/{id}")
    public Result getPlayAuth(@PathVariable Long id){
        JSONObject jsonObject = iLiveCourseService.getPlayAuth(id,AuthContextHolder.getUserId());
        return Result.ok(jsonObject);
    }

    @ApiOperation("根据ID查询课程")
    @GetMapping("getInfo/{courseId}")
    public Result getInfo(
            @ApiParam(value = "课程ID", required = true)
            @PathVariable Long courseId){
        Map<String, Object> map = iLiveCourseService.getInfoById(courseId);
        return Result.ok(map);
    }
}
