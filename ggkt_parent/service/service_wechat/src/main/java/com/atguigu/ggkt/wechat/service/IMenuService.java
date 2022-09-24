package com.atguigu.ggkt.wechat.service;

import com.atguigu.ggkt.model.wechat.Menu;
import com.atguigu.ggkt.vo.wechat.MenuVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 订单明细 订单明细 服务类
 * </p>
 *
 * @author tianjf
 * @since 2022-09-15
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> findOneMenuInfo();


    List<MenuVo> findMenuInfo();

    void syncMenu();

    void removeMenu();
}
