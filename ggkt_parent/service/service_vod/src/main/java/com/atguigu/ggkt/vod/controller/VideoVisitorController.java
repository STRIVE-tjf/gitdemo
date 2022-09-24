package com.atguigu.ggkt.vod.controller;

import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vod.service.IVideoVisitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 视频来访者记录表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-09-12
 */
@RestController
@RequestMapping("/admin/vod/videoVisitor")
//@CrossOrigin//处理跨域
@Api(tags = "观看人数统计")
public class VideoVisitorController {
    //注入videoVisitorService
    @Autowired
    private IVideoVisitorService iVideoVisitorService;
    //统计观看人数
    @ApiOperation("统计观看人数")
    @GetMapping("findCount/{courseId}/{startDate}/{endDate}")
    public Result findCount(@PathVariable Long courseId,
                            @PathVariable String startDate,
                            @PathVariable String endDate){
        Map<String,Object> map = iVideoVisitorService.findCount(courseId,startDate,endDate);
        return Result.ok(map);
    }
}
