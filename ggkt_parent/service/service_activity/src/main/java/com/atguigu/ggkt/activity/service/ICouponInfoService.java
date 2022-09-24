package com.atguigu.ggkt.activity.service;

import com.atguigu.ggkt.model.activity.CouponInfo;
import com.atguigu.ggkt.model.activity.CouponUse;
import com.atguigu.ggkt.vo.activity.CouponInfoVo;
import com.atguigu.ggkt.vo.activity.CouponUseQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 优惠券信息 服务类
 * </p>
 *
 * @author tianjf
 * @since 2022-09-15
 */
public interface ICouponInfoService extends IService<CouponInfo> {

    IPage<CouponUse> findCouponUsePage(Page<CouponUse> pageParam, CouponUseQueryVo couponUseQueryVo);

    void updateCouponInfoUseStatus(Long couponUseId, Long orderId);
}
