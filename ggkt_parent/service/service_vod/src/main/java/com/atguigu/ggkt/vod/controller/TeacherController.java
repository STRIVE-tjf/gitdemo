package com.atguigu.ggkt.vod.controller;

import com.atguigu.ggkt.exception.GgktException;
import com.atguigu.ggkt.model.vod.Teacher;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vo.vod.TeacherQueryVo;
import com.atguigu.ggkt.vod.service.ITeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author tianjf
 * @since 2022-09-03
 */

@RestController
@RequestMapping("/admin/vod/teacher")
@Api(tags = "讲师管理接口")
//@CrossOrigin//解决跨域
public class TeacherController {
    @Autowired
    private ITeacherService iTeacherService;
    // http://localhost:8301/admin/vod/teacher/findAll
    //查询所有讲师
    /*@GetMapping("findAll")
    @ApiOperation("查询所有讲师信息")
    public List<Teacher> findAllTeacher(){
        List<Teacher> list = iTeacherService.list();
        System.out.println(list);
        return list;
    }*/
    @GetMapping("findAll")
    @ApiOperation("查询所有讲师信息")
    public Result findAllTeacher(){
        //模拟异常
        /*try {
            int i = 10/0;
        } catch (Exception e) {
            throw new GgktException(2001,"执行了自定义异常");
        }*/
        List<Teacher> list = iTeacherService.list();
        System.out.println(list);
        return Result.ok(list);
    }

    //根据id逻辑删除讲师
    @DeleteMapping("remove/{id}")
    @ApiOperation("删除讲师信息")
    public Result remove(@PathVariable Long id){
        boolean isSuccess = iTeacherService.removeById(id);
        if (isSuccess){//如果为true
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }
    //分页查询讲师接口
    @ApiOperation("分页查询")
    @PostMapping("findQueryPage/{current}/{limit}")
    public Result find(@PathVariable Long current,
                       @PathVariable Long limit,
                       @RequestBody(required = false) TeacherQueryVo teacherQueryVo){
        //创建page对象
        Page<Teacher> page = new Page<>(current,limit);
        //判断teacherQueryVo是否为空
        if (teacherQueryVo == null) {//查询全部
            IPage<Teacher> pageModel = iTeacherService.page(page, null);
            return Result.ok(pageModel);
        }else {

            String name = teacherQueryVo.getName();//获取讲师姓名
            Integer level = teacherQueryVo.getLevel();//获取讲师等级
            String joinDateBegin = teacherQueryVo.getJoinDateBegin();//获取讲师入驻时间
            String joinDateEnd = teacherQueryVo.getJoinDateEnd();//获取讲师离开时间
            //判断条件非空,并封装条件到MybatisPlus条件构造器中
            QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
            if (!StringUtils.isEmpty(name)){
                queryWrapper.like("name",name);
            }
            if (!StringUtils.isEmpty(level)){
                queryWrapper.eq("level",level);
            }
            if (!StringUtils.isEmpty(joinDateBegin)){
                queryWrapper.ge("joinDateBegin",joinDateBegin);
            }
            if (!StringUtils.isEmpty(joinDateEnd)){
                queryWrapper.le("joinDateEnd",joinDateEnd);
            }

            IPage<Teacher> pageModel = iTeacherService.page(page, queryWrapper);
            return Result.ok(pageModel);
        }
    }
    //添加讲师接口
    @ApiOperation("添加讲师")
    @PostMapping("saveTeacher")
    public Result add(@RequestBody Teacher teacher){
        boolean isSuccess = iTeacherService.save(teacher);
        if (isSuccess){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }

    }
    //修改讲师信息(根据id获取讲师信息)
    @ApiOperation("根据id获取讲师信息")
    @GetMapping("getTeacher/{id}")
    public Result getTeacher(@PathVariable Long id){
        Teacher teacher = iTeacherService.getById(id);
        return Result.ok(teacher);
    }
    //修改讲师(修改)
    @ApiOperation("修改")
    @PostMapping("updateTeacher")
    public Result update(@RequestBody Teacher teacher){
        boolean isSuccess = iTeacherService.updateById(teacher);
        if (isSuccess){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }
    //批量删除讲师接口
    @ApiOperation("批量删除讲师")
    @DeleteMapping("removeBatch")
    //前端传一个Json数组[],后端用一个List接收
    public Result removeBatch(@RequestBody List<Long> idList){
        boolean isSuccess = iTeacherService.removeByIds(idList);
        if (isSuccess){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }



}
