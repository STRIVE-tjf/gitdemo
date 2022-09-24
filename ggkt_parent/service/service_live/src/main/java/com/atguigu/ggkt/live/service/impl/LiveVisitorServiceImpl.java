package com.atguigu.ggkt.live.service.impl;


import com.atguigu.ggkt.live.mapper.LiveVisitorMapper;
import com.atguigu.ggkt.live.service.ILiveVisitorService;
import com.atguigu.ggkt.model.live.LiveVisitor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 直播来访者记录表 服务实现类
 * </p>
 *
 * @author tianjf
 * @since 2022-09-22
 */
@Service
public class LiveVisitorServiceImpl extends ServiceImpl<LiveVisitorMapper, LiveVisitor> implements ILiveVisitorService {

}
