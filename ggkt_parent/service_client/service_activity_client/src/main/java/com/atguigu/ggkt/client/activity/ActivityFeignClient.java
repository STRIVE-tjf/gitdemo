package com.atguigu.ggkt.client.activity;

import com.atguigu.ggkt.model.activity.CouponInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Tianjinfei
 * @Version 1.0
 */
@FeignClient(value = "service-activity")
public interface ActivityFeignClient {
    //根据优惠券id获取优惠券(被service_order模块远程调用)
    @GetMapping("/api/vod/couponInfo/inner/getById/{couponId}")
    CouponInfo getById(@PathVariable Long couponId);
    @GetMapping("/api/vod/couponInfo/inner/updateCouponInfoUseStatus/{couponUseId}/{orderId}")
    Boolean updateCouponInfoUseStatus(@PathVariable Long couponUseId,
                                      @PathVariable Long orderId);

}
