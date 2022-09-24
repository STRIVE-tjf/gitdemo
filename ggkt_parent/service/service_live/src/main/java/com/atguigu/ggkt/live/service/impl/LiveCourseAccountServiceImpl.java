package com.atguigu.ggkt.live.service.impl;


import com.atguigu.ggkt.live.mapper.LiveCourseAccountMapper;
import com.atguigu.ggkt.live.service.ILiveCourseAccountService;
import com.atguigu.ggkt.model.live.LiveCourseAccount;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 直播课程账号表（受保护信息） 服务实现类
 * </p>
 *
 * @author tianjf
 * @since 2022-09-22
 */
@Service
public class LiveCourseAccountServiceImpl extends ServiceImpl<LiveCourseAccountMapper, LiveCourseAccount> implements ILiveCourseAccountService {
    //根据课程id获取直播课程账户
    @Override
    public LiveCourseAccount getCourseAccountByCourseId(Long id) {
        LambdaQueryWrapper<LiveCourseAccount> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LiveCourseAccount::getLiveCourseId,id);
        LiveCourseAccount liveCourseAccount = baseMapper.selectOne(queryWrapper);
        return liveCourseAccount;
    }
}
