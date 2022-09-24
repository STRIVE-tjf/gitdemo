package com.atguigu.ggkt.vod.controller;

import com.atguigu.ggkt.model.vod.Subject;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vod.service.ISubjectService;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-09-09
 */
@RestController
@RequestMapping("/admin/vod/subject")
//@CrossOrigin//处理跨域
public class SubjectController {
    @Autowired
    private ISubjectService iSubjectService;
    //获取课程列表功能接口
    @ApiOperation("获取课程列表")
    @GetMapping("getChildSubject/{id}")
    public Result findSubjectList(@PathVariable Long id){
        List<Subject> subjectList =  iSubjectService.selectSubjectList(id);
        return Result.ok(subjectList);
    }
    //课程列表导出功能接口
    @ApiOperation("导出课程列表")
    @GetMapping("exportData")
    public void exportData(HttpServletResponse response){
        iSubjectService.exportDataList(response);
    }
    //课程列表导入功能接口
    @ApiOperation("课程列表导入")
    @PostMapping("importData")
    public Result importData(MultipartFile file){
        iSubjectService.importDataList(file);
        return Result.ok(null);
    }
}
