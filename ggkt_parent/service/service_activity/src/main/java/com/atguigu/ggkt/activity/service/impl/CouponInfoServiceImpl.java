package com.atguigu.ggkt.activity.service.impl;

import com.atguigu.ggkt.activity.mapper.CouponInfoMapper;
import com.atguigu.ggkt.activity.service.ICouponInfoService;
import com.atguigu.ggkt.activity.service.ICouponUseService;
import com.atguigu.ggkt.client.user.UserInfoFeignClient;
import com.atguigu.ggkt.model.activity.CouponInfo;
import com.atguigu.ggkt.model.activity.CouponUse;
import com.atguigu.ggkt.model.user.UserInfo;
import com.atguigu.ggkt.vo.activity.CouponInfoVo;
import com.atguigu.ggkt.vo.activity.CouponUseQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 优惠券信息 服务实现类
 * </p>
 *
 * @author tianjf
 * @since 2022-09-15
 */
@Service
public class CouponInfoServiceImpl extends ServiceImpl<CouponInfoMapper, CouponInfo> implements ICouponInfoService {
    @Autowired
    private ICouponUseService iCouponUseService;
    @Autowired
    private UserInfoFeignClient userInfoFeignClient;//远程调用的借口

    //根据coupon_use表的id和订单id修改优惠券的状态(是否使用)(被service_order模块远程调用)
    @Override
    public void updateCouponInfoUseStatus(Long couponUseId, Long orderId) {
        CouponUse couponUse = new CouponUse();
        couponUse.setId(couponUseId);
        couponUse.setOrderId(orderId);
        couponUse.setCouponStatus("1");
        couponUse.setCreateTime(new Date());
        iCouponUseService.updateById(couponUse);
    }

    @Override
    public IPage<CouponUse> findCouponUsePage(Page<CouponUse> pageParam, CouponUseQueryVo couponUseQueryVo) {
        //创建条件构造器对象
        QueryWrapper<CouponUse> queryWrapper = new QueryWrapper<>();
        //获取条件
        Long couponId = couponUseQueryVo.getCouponId();
        String couponStatus = couponUseQueryVo.getCouponStatus();
        String timeBegin = couponUseQueryVo.getGetTimeBegin();
        String timeEnd = couponUseQueryVo.getGetTimeEnd();
        //判断条件非空并封装
        if (!StringUtils.isEmpty(couponId)){
            queryWrapper.eq("coupon_id",couponId);
        }
        if (!StringUtils.isEmpty(couponStatus)){
            queryWrapper.eq("coupon_status",couponStatus);
        }
        if (!StringUtils.isEmpty(timeBegin)){
            queryWrapper.ge("create_time",timeBegin);
        }
        if (!StringUtils.isEmpty(timeEnd)){
            queryWrapper.le("create_time",timeEnd);
        }
        IPage<CouponUse> page = iCouponUseService.page(pageParam, queryWrapper);
        List<CouponUse> records = page.getRecords();
        //封装用户昵称和手机号码
        records.stream().forEach(item -> {
            this.getUserInfoByCouponUse(item);
        });
        return page;
    }

    private CouponUse getUserInfoByCouponUse(CouponUse couponUse) {
        Long userId = couponUse.getUserId();
        if (!StringUtils.isEmpty(userId)){
            //远程调用
            UserInfo userInfo = userInfoFeignClient.getById(userId);
            if (userInfo != null){
                String nickName = userInfo.getNickName();
                String phone = userInfo.getPhone();
                couponUse.getParam().put("nickName",nickName);
                couponUse.getParam().put("phone",phone);
            }
        }
        return couponUse;
    }
}
