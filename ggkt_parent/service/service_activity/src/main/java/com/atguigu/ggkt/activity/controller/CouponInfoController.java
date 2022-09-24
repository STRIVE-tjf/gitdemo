package com.atguigu.ggkt.activity.controller;

import com.atguigu.ggkt.activity.service.ICouponInfoService;
import com.atguigu.ggkt.model.activity.CouponInfo;
import com.atguigu.ggkt.model.activity.CouponUse;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vo.activity.CouponInfoVo;
import com.atguigu.ggkt.vo.activity.CouponUseQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
 * 优惠券信息 前端控制器
 * </p>
 *
 * @author tianjf
 * @since 2022-09-15
 */
@Api(tags = "优惠券相关功能接口")
@RestController
@RequestMapping("/admin/activity/couponInfo")
public class CouponInfoController {
    @Autowired
    private ICouponInfoService iCouponInfoService;
    //获取优惠券列表
    @ApiOperation("获取优惠券列表")
    @GetMapping("{page}/{limit}")
    public Result couponList(@PathVariable Long page, //当前页
                             @PathVariable Long limit //每页显示记录数
                             ){
        //创建Page对象
        Page<CouponInfo> pageParam = new Page<>(page,limit);
        IPage<CouponInfo> pageModel = iCouponInfoService.page(pageParam);
        return Result.ok(pageModel);
    }
    //根据id获取优惠券
    @ApiOperation("根据id获取优惠券")
    @GetMapping("get/{id}")
    public Result getById(@PathVariable String id){
        CouponInfo couponInfo = iCouponInfoService.getById(id);
        return Result.ok(couponInfo);
    }
    //新增优惠券
    @ApiOperation("新增优惠券")
    @PostMapping("sava")
    public Result addCoupon(@RequestBody CouponInfo couponInfo){
        iCouponInfoService.save(couponInfo);
        return Result.ok(null);
    }
    //修改优惠券
    @ApiOperation("修改优惠券")
    @PutMapping("update")
    public Result updateCoupon(@RequestBody CouponInfo couponInfo){
        iCouponInfoService.updateById(couponInfo);
        return Result.ok(null);
    }
    //根据id删除优惠券
    @ApiOperation("删除优惠券")
    @DeleteMapping("remove/{id}")
    public Result deleteCouponById(@PathVariable String id){
        iCouponInfoService.removeById(id);
        return Result.ok(null);
    }
    //批量删除优惠券
    @ApiOperation("批量删除优惠券")
    @DeleteMapping("batchRemove")
    public Result batchDelete(@RequestBody List<String> ids){
        iCouponInfoService.removeByIds(ids);
        return Result.ok(null);
    }
    //获取已使用的优惠券列表
    @ApiOperation("获取已使用的优惠券列表")
    @GetMapping("couponUse/{page}/{limit}")
    public Result findCouponPage(@PathVariable Long page,
                                 @PathVariable Long limit,
                                 CouponUseQueryVo couponUseQueryVo){
        //创建Page对象
        Page<CouponUse> pageParam = new Page<>(page,limit);
        IPage<CouponUse> pageModel = iCouponInfoService.findCouponUsePage(pageParam,couponUseQueryVo);
        return Result.ok(pageModel);
    }
}
