package com.atguigu.ggkt.order.api;

import com.atguigu.ggkt.order.service.IOrderInfoService;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vo.order.OrderFormVo;
import com.atguigu.ggkt.vo.order.OrderInfoVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Tianjinfei
 * @Version 1.0
 */
@RestController
@RequestMapping("api/order/orderInfo")
public class OrderInfoApiController {
    @Autowired
    private IOrderInfoService iOrderInfoService;
    //新增订单
    @ApiOperation("新增订单")
    @PostMapping("submitOrder")
    public Result submitOrder(@RequestBody OrderFormVo orderFormVo, HttpServletRequest request){
        Long orderId = iOrderInfoService.submitOrder(orderFormVo);
        return Result.ok(orderId);
    }

    //根据id获取订单信息
    @ApiOperation(value = "获取")
    @GetMapping("getInfo/{id}")
    public Result getInfo(@PathVariable Long id) {
        OrderInfoVo orderInfoVo = iOrderInfoService.getOrderInfoById(id);
        return Result.ok(orderInfoVo);
    }
}
