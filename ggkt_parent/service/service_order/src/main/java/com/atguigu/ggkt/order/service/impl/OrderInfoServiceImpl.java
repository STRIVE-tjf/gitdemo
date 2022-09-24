package com.atguigu.ggkt.order.service.impl;

import com.atguigu.ggkt.client.activity.ActivityFeignClient;
import com.atguigu.ggkt.client.course.CourseFeignClient;
import com.atguigu.ggkt.client.user.UserInfoFeignClient;
import com.atguigu.ggkt.exception.GgktException;
import com.atguigu.ggkt.model.activity.CouponInfo;
import com.atguigu.ggkt.model.order.OrderDetail;
import com.atguigu.ggkt.model.order.OrderInfo;

import com.atguigu.ggkt.model.user.UserInfo;
import com.atguigu.ggkt.model.vod.Course;
import com.atguigu.ggkt.order.mapper.OrderInfoMapper;
import com.atguigu.ggkt.order.service.IOrderDetailService;
import com.atguigu.ggkt.order.service.IOrderInfoService;
import com.atguigu.ggkt.utils.AuthContextHolder;
import com.atguigu.ggkt.utils.OrderNoUtils;
import com.atguigu.ggkt.vo.order.OrderFormVo;
import com.atguigu.ggkt.vo.order.OrderInfoQueryVo;
import com.atguigu.ggkt.vo.order.OrderInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 订单表 服务实现类
 * </p>
 *
 * @author tianjf
 * @since 2022-09-14
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {
    @Autowired
    private IOrderDetailService iOrderDetailService;
    @Autowired
    private UserInfoFeignClient userInfoFeignClient;
    @Autowired
    private CourseFeignClient courseFeignClient;
    @Autowired
    private ActivityFeignClient activityFeignClient;

    @Override
    public OrderInfoVo getOrderInfoById(Long id) {
        OrderInfo orderInfo = baseMapper.selectById(id);
        OrderDetail orderDetail = iOrderDetailService.getById(id);
        OrderInfoVo orderInfoVo = new OrderInfoVo();
        BeanUtils.copyProperties(orderInfo,orderInfoVo);
        orderInfoVo.setCourseId(orderDetail.getCourseId());
        orderInfoVo.setCourseName(orderDetail.getCourseName());
        return orderInfoVo;
    }

    //新增订单
    @Override
    public Long submitOrder(OrderFormVo orderFormVo) {
        //获取封装条件
        Long userId = AuthContextHolder.getUserId();
        Long couponId = orderFormVo.getCouponId();
        Long courseId = orderFormVo.getCourseId();

        //判断用户有没有当前订单
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getUserId,userId);
        queryWrapper.eq(OrderDetail::getCourseId,courseId);
        OrderDetail orderDetail = iOrderDetailService.getOne(queryWrapper);
        //判断订单是否存在,如果不存在返回订单id
        if (orderDetail == null){
            return orderDetail.getOrderId();
        }
        //远程调用CourseFeignClient接口,根据课程id获取课程信息
        Course course = courseFeignClient.getById(courseId);
        //判断课程是否存在
        if (course == null){
            throw new GgktException(20001,"课程信息不存在");
        }
        //远程调用UserInfoFeignClient接口,根据id获取用户信息
        UserInfo userInfo = userInfoFeignClient.getById(userId);
        //判断用户用户是否存在
        if (userInfo == null){
            throw new GgktException(20001,"用户不存在");
        }
        //判断优惠券是否存在
        BigDecimal couponReduce = new BigDecimal(0);
        if (couponId != null){
            //远程调用ActivityFeignClient接口
            CouponInfo couponInfo = activityFeignClient.getById(couponId);
            couponReduce = couponInfo.getAmount();
        }
        //封装数据到订单基本信息表中
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(userId);
        orderInfo.setNickName(userInfo.getNickName());
        orderInfo.setPhone(userInfo.getPhone());
        orderInfo.setProvince(userInfo.getProvince());
        orderInfo.setOriginAmount(course.getPrice());
        orderInfo.setCouponReduce(couponReduce);
        orderInfo.setFinalAmount(orderInfo.getOriginAmount().subtract(orderInfo.getCouponReduce()));
        orderInfo.setOutTradeNo(OrderNoUtils.getOrderNo());//订单流水号
        orderInfo.setTradeBody(course.getTitle());
        orderInfo.setOrderStatus("0");
        this.save(orderInfo);
        //封装数据到订单详情表中
        OrderDetail orderDetail1 = new OrderDetail();
        orderDetail1.setOrderId(orderInfo.getId());
        orderDetail1.setUserId(userId);
        orderDetail1.setCourseId(courseId);
        orderDetail1.setCourseName(course.getTitle());
        orderDetail1.setCover(course.getCover());
        orderDetail1.setOriginAmount(course.getPrice());
        orderDetail1.setCouponReduce(new BigDecimal(0));
        orderDetail1.setFinalAmount(orderDetail.getOriginAmount().subtract(orderDetail.getCouponReduce()));
        iOrderDetailService.save(orderDetail1);
        //更新优惠券的使用状态
        //更新优惠券状态
        if(null != orderFormVo.getCouponUseId()) {
            activityFeignClient.updateCouponInfoUseStatus(orderFormVo.getCouponUseId(), orderInfo.getId());
        }
        return orderInfo.getId();

    }

    @Override
    public Map<String, Object> findOrderPage(Page<OrderInfo> pageParam, OrderInfoQueryVo orderInfoQueryVo) {
        //获取信息
        Long userId = orderInfoQueryVo.getUserId();//用户id
        String nickName = orderInfoQueryVo.getNickName();//用户昵称
        String phone = orderInfoQueryVo.getPhone();//用户电话
        String createTimeBegin = orderInfoQueryVo.getCreateTimeBegin();//订单创建时间
        String createTimeEnd = orderInfoQueryVo.getCreateTimeEnd();//结束时间
        Integer orderStatus = orderInfoQueryVo.getOrderStatus();//订单状态
        String outTradeNo = orderInfoQueryVo.getOutTradeNo();//交易流水号
        String province = orderInfoQueryVo.getProvince();//获取省份
        //创建条件构造器
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        //判断条件非空并封装条件
        if (!StringUtils.isEmpty(userId)){
            queryWrapper.eq("user_id",userId);
        }
        if (!StringUtils.isEmpty(outTradeNo)){
            queryWrapper.eq("out_trade_no",outTradeNo);
        }
        if (!StringUtils.isEmpty(phone)){
            queryWrapper.eq("phone",phone);
        }
        if (!StringUtils.isEmpty(createTimeBegin)){
            queryWrapper.ge("create_time",createTimeBegin);//大于一个创建时间
        }
        if (!StringUtils.isEmpty(createTimeEnd)){
            queryWrapper.le("create_time",createTimeEnd);//小于一个创建时间
        }
        if (!StringUtils.isEmpty(orderStatus)){
            queryWrapper.eq("order_status",orderStatus);
        }
        Page<OrderInfo> orderInfoPage = baseMapper.selectPage(pageParam, queryWrapper);
        long pages = orderInfoPage.getPages();//获取总页数
        long current = orderInfoPage.getCurrent();//获取当前页
        long total = orderInfoPage.getTotal();//获取总记录数
        List<OrderInfo> records = orderInfoPage.getRecords();//获取当前页的记录数
        records.stream().forEach(item -> {
            this.getOrderDetail(item);
        });
        Map<String,Object> map = new HashMap<>();
        map.put("pages",pages);
        map.put("current",current);
        map.put("total",total);
        map.put("records",records);
        return map;
    }

    private OrderInfo getOrderDetail(OrderInfo orderInfo) {
        Long id = orderInfo.getId();
        OrderDetail orderDetail = iOrderDetailService.getById(id);
        if (orderDetail != null){
            String courseName = orderDetail.getCourseName();
            orderInfo.getParam().put("course_name",courseName);
        }
        return orderInfo;
    }
    //修改订单状态


    @Override
    public void updateOrderStatus(String out_trade_no) {
        LambdaQueryWrapper<OrderInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderInfo::getOutTradeNo,out_trade_no);
        OrderInfo orderInfo = baseMapper.selectOne(queryWrapper);
        orderInfo.setOrderStatus("1");
        baseMapper.updateById(orderInfo);
    }
}
