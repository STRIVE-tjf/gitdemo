package com.atguigu.ggkt.order.api;

import com.atguigu.ggkt.exception.GgktException;
import com.atguigu.ggkt.order.service.IOrderInfoService;
import com.atguigu.ggkt.order.service.WxPayService;
import com.atguigu.ggkt.result.Result;
import io.swagger.annotations.ApiOperation;
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
 * 微信支付接口
 */
@RestController
@RequestMapping("api/order/wxPay")
public class WxPayController {
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private IOrderInfoService iOrderInfoService;

    @ApiOperation(value = "下单 小程序支付")
    @GetMapping("/createJsapi/{orderNo}")
    public Result createJsapi(
            @ApiParam(name = "orderNo", value = "订单No", required = true)
            @PathVariable("orderNo") String orderNo) {
        return Result.ok(wxPayService.createJsapi(orderNo));
    }

    @ApiOperation(value = "查询支付状态")
    @GetMapping("/queryPayStatus/{orderNo}")
    public Result queryPayStatus(
            @ApiParam(name = "orderNo", value = "订单No", required = true)
            @PathVariable("orderNo") String orderNo) {
        Map<String,String> resultMap = wxPayService.queryPayStatus(orderNo);
        if (resultMap != null){
            throw new GgktException(20001,"订单支付失败");
        }
        if ("SUCCESS".equals(resultMap.get("trade_state"))) {//如果成功
            //更改订单状态，处理支付结果
            String out_trade_no = resultMap.get("out_trade_no");
            System.out.println("out_trade_no:"+out_trade_no);
            iOrderInfoService.updateOrderStatus(out_trade_no);
            return Result.ok(null).message("支付成功");
        }
        return Result.ok(null).message("支付中");
    }
}
