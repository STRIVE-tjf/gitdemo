package com.atguigu.ggkt.activity.api;

import com.atguigu.ggkt.activity.service.ICouponInfoService;
import com.atguigu.ggkt.model.activity.CouponInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tianjinfei
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/vod/couponInfo")
public class CouponInfoApiController {
    @Autowired
    private ICouponInfoService iCouponInfoService;
    //根据优惠券id获取优惠券(被service_order模块远程调用)
    @GetMapping("inner/getById/{couponId}")
    public CouponInfo getById(@PathVariable Long couponId){
        CouponInfo couponInfo = iCouponInfoService.getById(couponId);
        return couponInfo;
    }
    //根据coupon_use表的id和订单id修改优惠券的状态(是否使用)(被service_order模块远程调用)
    @GetMapping("inner/updateCouponInfoUseStatus/{couponUseId}/{orderId}")
    public Boolean updateCouponInfoUseStatus(@PathVariable Long couponUseId,
                                             @PathVariable Long orderId){
        iCouponInfoService.updateCouponInfoUseStatus(couponUseId,orderId);
        return true;
    }
}
