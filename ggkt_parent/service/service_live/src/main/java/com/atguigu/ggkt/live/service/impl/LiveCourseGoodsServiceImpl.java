package com.atguigu.ggkt.live.service.impl;


import com.atguigu.ggkt.live.mapper.LiveCourseGoodsMapper;
import com.atguigu.ggkt.live.service.ILiveCourseGoodsService;
import com.atguigu.ggkt.model.live.LiveCourseGoods;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 直播课程关联推荐表 服务实现类
 * </p>
 *
 * @author tianjf
 * @since 2022-09-22
 */
@Service
public class LiveCourseGoodsServiceImpl extends ServiceImpl<LiveCourseGoodsMapper, LiveCourseGoods> implements ILiveCourseGoodsService {
    //根据直播课程id获取商品列表

    @Override
    public List<LiveCourseGoods> getGoods(Long courseId) {
        LambdaQueryWrapper<LiveCourseGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LiveCourseGoods::getLiveCourseId,courseId);
        List<LiveCourseGoods> liveCourseGoods = baseMapper.selectList(queryWrapper);
        return liveCourseGoods;
    }
}
