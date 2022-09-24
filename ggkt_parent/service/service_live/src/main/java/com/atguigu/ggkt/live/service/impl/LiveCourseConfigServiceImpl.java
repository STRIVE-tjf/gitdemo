package com.atguigu.ggkt.live.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.ggkt.exception.GgktException;
import com.atguigu.ggkt.live.mapper.LiveCourseConfigMapper;
import com.atguigu.ggkt.live.mtcloud.CommonResult;
import com.atguigu.ggkt.live.mtcloud.MTCloud;
import com.atguigu.ggkt.live.service.ILiveCourseConfigService;
import com.atguigu.ggkt.live.service.ILiveCourseGoodsService;
import com.atguigu.ggkt.model.live.LiveCourseConfig;
import com.atguigu.ggkt.model.live.LiveCourseGoods;
import com.atguigu.ggkt.vo.live.LiveCourseConfigVo;
import com.atguigu.ggkt.vo.live.LiveCourseGoodsView;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 直播课程配置表 服务实现类
 * </p>
 *
 * @author tianjf
 * @since 2022-09-22
 */
@Service
public class LiveCourseConfigServiceImpl extends ServiceImpl<LiveCourseConfigMapper, LiveCourseConfig> implements ILiveCourseConfigService {
    @Autowired
    private ILiveCourseConfigService iLiveCourseConfigService;
    @Autowired
    private ILiveCourseGoodsService iLiveCourseGoodsService;
    @Autowired
    private MTCloud mtCloud;
    @Override
    public LiveCourseConfig getLiveCourseConfig(Long courseId) {
        LambdaQueryWrapper<LiveCourseConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LiveCourseConfig::getLiveCourseId,courseId);
        LiveCourseConfig liveCourseConfig = baseMapper.selectOne(queryWrapper);
        return liveCourseConfig;
    }

}
