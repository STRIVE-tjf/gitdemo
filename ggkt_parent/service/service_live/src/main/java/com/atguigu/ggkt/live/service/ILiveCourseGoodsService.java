package com.atguigu.ggkt.live.service;


import com.atguigu.ggkt.model.live.LiveCourseGoods;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 直播课程关联推荐表 服务类
 * </p>
 *
 * @author tianjf
 * @since 2022-09-22
 */
public interface ILiveCourseGoodsService extends IService<LiveCourseGoods> {

    List<LiveCourseGoods> getGoods(Long courseId);
}
