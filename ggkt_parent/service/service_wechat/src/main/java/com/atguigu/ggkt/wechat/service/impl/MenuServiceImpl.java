package com.atguigu.ggkt.wechat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.ggkt.exception.GgktException;
import com.atguigu.ggkt.model.wechat.Menu;
import com.atguigu.ggkt.vo.wechat.MenuVo;
import com.atguigu.ggkt.wechat.mapper.MenuMapper;
import com.atguigu.ggkt.wechat.service.IMenuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单明细 订单明细 服务实现类
 * </p>
 *
 * @author tianjf
 * @since 2022-09-15
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {
    @Autowired
    private WxMpService wxMpService;
    //删除公众号菜单
    @Override
    public void removeMenu() {
        try {
            wxMpService.getMenuService().menuDelete();
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new GgktException(20001,"删除菜单失败");
        }
    }

    //同步公众号菜单
    @Override
    public void syncMenu() {
        //获取所有的菜单列表
        List<MenuVo> menuVoList = this.findMenuInfo();
        //最外一层json数组
        JSONArray buttonList = new JSONArray();
        for (MenuVo oneMenuVo :menuVoList) {
            //封装第一层json对象
            JSONObject one = new JSONObject();
            one.put("name",oneMenuVo.getName());
            //封装第二层json数组
            JSONArray subButtonList = new JSONArray();
            for (MenuVo twoMenuVo :oneMenuVo.getChildren()) {
                JSONObject two = new JSONObject();
                two.put("type",twoMenuVo.getType());
                if ("view".equals(twoMenuVo.getType())){
                    two.put("name",twoMenuVo.getName());
                    two.put("url","http://tianjf.viphk.91tunnel.com/#"+twoMenuVo.getUrl());
                }else {
                    two.put("name",twoMenuVo.getName());
                    two.put("key",twoMenuVo.getMeunKey());
                }
                subButtonList.add(two);
            }
            one.put("sub_button",subButtonList);
            buttonList.add(one);
        }
        //封装最外层
        JSONObject button = new JSONObject();
        button.put("button",buttonList);
        try {
            String menuId = this.wxMpService.getMenuService().menuCreate(button.toJSONString());
            System.out.println("******menuId:"+menuId+"*********");
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new GgktException(20001,"同步菜单失败");
        }
    }

    //获取菜单(封装一级菜单和二级菜单)
    @Override
    public List<MenuVo> findMenuInfo() {
        //创建一个list存放最终封装好的数据
        List<MenuVo> finalList = new ArrayList<>();
        //获取所有菜单
        List<Menu> menuList = baseMapper.selectList(null);
        //获取一级菜单
        List<Menu> oneMenuList = menuList.stream().filter(menu ->
                menu.getParentId().longValue() == 0)
                .collect(Collectors.toList());
        //遍历一级菜单,封装到finalList

        for (Menu oneMenu :oneMenuList) {
            //将oneMenu->oneMenuVo
            MenuVo oneMenuVo = new MenuVo();
            BeanUtils.copyProperties(oneMenu,oneMenuVo);
            //获取二级菜单
            List<Menu> twoMenuList = menuList.stream().filter(menu ->
                            menu.getParentId().longValue() == oneMenu.getId())
                    .collect(Collectors.toList());
            //创建一个list来存放二级菜单
            List<MenuVo> children = new ArrayList<>();
            for (Menu twoMenu :twoMenuList) {
                //将menu -> menuVo
                MenuVo twoMenuVo = new MenuVo();
                BeanUtils.copyProperties(twoMenu,twoMenuVo);
                //将twoMenuVo封装到twoMenuVoList中
                children.add(twoMenuVo);
            }
            //将twoMenuVo封装到一级菜单中
            oneMenuVo.setChildren(children);
            //将oneMenuVo封装到finalList
            finalList.add(oneMenuVo);
        }

        return finalList;
    }

    //获取一级菜单
    @Override
    public List<Menu> findOneMenuInfo() {
        //创建条件构造器
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",0);//parent_id为0代表这是第一层目录
        List<Menu> menuList = baseMapper.selectList(queryWrapper);
        return menuList;
    }

}
