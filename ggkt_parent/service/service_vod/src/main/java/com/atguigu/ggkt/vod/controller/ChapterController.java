package com.atguigu.ggkt.vod.controller;

import com.atguigu.ggkt.model.vod.Chapter;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vo.vod.ChapterVo;
import com.atguigu.ggkt.vod.service.IChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-09-09
 */
@RestController
@RequestMapping("/admin/vod/chapter")
//@CrossOrigin//处理跨域
@Api(tags = "操作章节列表的接口")
public class ChapterController {
    //注入Chapter属性
    @Autowired
    private IChapterService iChapterService;
    //大纲列表
    @ApiOperation("根据课程id获取章节和小结的列表")
    @GetMapping("getNestedTreeList/{courseId}")
    public Result getTreeList(@PathVariable Long courseId){
        List<ChapterVo> chapterVoList = iChapterService.getNetsTreeList(courseId);
        return Result.ok(chapterVoList);
    }
    //添加章节
    @ApiOperation("添加章节")
    @PostMapping("save")
    public Result addChapter(@RequestBody Chapter chapter){
        iChapterService.save(chapter);
        return Result.ok(null);
    }
    //根据id获取章节列表
    @ApiOperation("根据id章节")
    @GetMapping("get/{id}")
    public Result getById(@PathVariable Long id){
        Chapter chapter = iChapterService.getById(id);
        return Result.ok(chapter);
    }
    //修改章节
    @ApiOperation("修改章节")
    @PostMapping("update")
    public Result update(@RequestBody Chapter chapter ){
        iChapterService.updateById(chapter);
        return Result.ok(null);
    }
    //根据id删除章节
    @ApiOperation("根据id删除章节")
    @DeleteMapping("remove/{id}")
    public Result deleteById(@PathVariable Long id){
        iChapterService.removeById(id);
        return Result.ok(null);
    }
}
