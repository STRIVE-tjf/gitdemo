package com.atguigu.ggkt.vod.api;

import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vod.service.IVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Tianjinfei
 * @Version 1.0
 */
@Api(tags = "腾讯云视频点播")
@RestController
@RequestMapping("/api/vod")
public class VideoApiController {
    @Autowired
    private IVideoService videoService;
    @ApiOperation("获取腾讯云点播授权")
    @GetMapping("getPlayAuth/{courseId}/{videoId}")
    public Result getPlayAuth(@PathVariable Long courseId,
                              @PathVariable Long videoId){
        Map<String,Object> map = videoService.getPlayAuth(courseId,videoId);
        return Result.ok(map);
    }
}
