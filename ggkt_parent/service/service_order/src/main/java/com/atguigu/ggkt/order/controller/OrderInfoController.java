package com.atguigu.ggkt.order.controller;

import com.atguigu.ggkt.model.order.OrderInfo;
import com.atguigu.ggkt.order.service.IOrderInfoService;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vo.order.OrderInfoQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 订单表 订单表 前端控制器
 * </p>
 *
 * @author tianjf
 * @since 2022-09-14
 */
@Api(tags = "订单操作接口")
@RestController
@RequestMapping("/admin/order/orderInfo")
public class OrderInfoController {
    @Autowired
    private IOrderInfoService iOrderInfoService;

    //订单分页查询
    @ApiOperation("订单分页查询")
    @GetMapping("{page}/{limit}")//page:当前页,limit:每页显示的记录数
    public Result listOrder(@PathVariable Long page,
                                @PathVariable Long limit,
                                OrderInfoQueryVo orderInfoQueryVo){
        //创建Page对象
        Page<OrderInfo> pageParam = new Page<>(page,limit);
        Map<String,Object> map = iOrderInfoService.findOrderPage(pageParam,orderInfoQueryVo);
        return Result.ok(map);
    }
}
