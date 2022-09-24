package com.atguigu.ggkt.vod.controller;

import com.atguigu.ggkt.exception.GgktException;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vod.service.IVodService;
import com.atguigu.ggkt.vod.utils.ConstantPropertiesUtils;
import com.atguigu.ggkt.vod.utils.Signature;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * @author Tianjinfei
 * @Version 1.0
 */
@Api(tags = "视频上传接口")
@RestController
@RequestMapping("/admin/vod")
//@CrossOrigin
public class VodController {
    @Autowired
    private IVodService iVodService;
    //获取客户端上传文件的id
    @GetMapping("user/sign")
    public Result sign(){
        Signature sign = new Signature();
        // 设置 App 的云 API 密钥
        sign.setSecretId(ConstantPropertiesUtils.ACCESS_KEY_ID);
        sign.setSecretKey(ConstantPropertiesUtils.ACCESS_KEY_SECRET);
        sign.setCurrentTime(System.currentTimeMillis() / 1000);
        sign.setRandom(new Random().nextInt(java.lang.Integer.MAX_VALUE));
        sign.setSignValidDuration(3600 * 24 * 2); // 签名有效期：2天
        try {
            String signature = sign.getUploadSignature();
            System.out.println("signature : " + signature);
            return Result.ok(signature);
        } catch (Exception e) {
            System.out.print("获取签名失败");
            e.printStackTrace();
            return Result.ok(null);
        }
    }
    //服务器端上传视频
    @ApiOperation("腾讯云视频上传")
    @PostMapping("upload")
    public Result upload(@PathVariable MultipartFile file) throws IOException {
        //根据file创建输入流
        InputStream inputStream = file.getInputStream();
        //获取文件名
        String originalFilename = file.getOriginalFilename();
        String videoId =  iVodService.upload(inputStream,originalFilename);
        return Result.ok(videoId);

    }
    //删除视频(删除小节时删除视频,删除课程时删除视频)
    @ApiOperation("删除视频接口")
    @DeleteMapping("remove/{videoSourceId}")
    public Result removeVideo(String videoSourceId){
        iVodService.removeVideo(videoSourceId);
        return Result.ok(null);
    }
}
