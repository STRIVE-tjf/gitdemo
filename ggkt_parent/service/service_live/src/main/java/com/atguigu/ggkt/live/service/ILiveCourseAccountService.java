package com.atguigu.ggkt.live.service;

import com.atguigu.ggkt.model.live.LiveCourseAccount;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 直播课程账号表（受保护信息） 服务类
 * </p>
 *
 * @author tianjf
 * @since 2022-09-22
 */
public interface ILiveCourseAccountService extends IService<LiveCourseAccount> {

    LiveCourseAccount getCourseAccountByCourseId(Long id);
}
