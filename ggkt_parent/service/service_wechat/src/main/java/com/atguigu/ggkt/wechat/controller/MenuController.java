package com.atguigu.ggkt.wechat.controller;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.ggkt.exception.GgktException;
import com.atguigu.ggkt.model.wechat.Menu;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vo.wechat.MenuVo;
import com.atguigu.ggkt.wechat.service.IMenuService;
import com.atguigu.ggkt.wechat.utils.ConstantPropertiesUtil;
import com.atguigu.ggkt.wechat.utils.HttpClientUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 订单明细 订单明细 前端控制器
 * </p>
 *
 * @author tianjf
 * @since 2022-09-15
 */
@RestController
@RequestMapping("/admin/wechat/menu")
public class MenuController {
    @Autowired
    private IMenuService iMenuService;
    //删除公众号菜单
    @ApiOperation("删除公众号菜单")
    @DeleteMapping("remove")
    public Result remove(){
        iMenuService.removeMenu();
        return Result.ok(null);
    }
    //同步公众号菜单
    @ApiOperation("同步公众号菜单")
    @GetMapping("syncMenu")
    public Result syncMenu(){
        iMenuService.syncMenu();
        return Result.ok(null);
    }
    //获取access_token
    @GetMapping("getAccessToken")
    public Result getAccessToken(){
        //拼接请求地址
        StringBuffer buffer = new StringBuffer();
        buffer.append("https://api.weixin.qq.com/cgi-bin/token");
        buffer.append("?grant_type=client_credential");
        buffer.append("&appid=%s");//%s占位符
        buffer.append("&secret=%s");
        //设置请求地址的参数(%s)
        String url = String.format(buffer.toString()
                , ConstantPropertiesUtil.ACCESS_KEY_ID
                , ConstantPropertiesUtil.ACCESS_KEY_SECRET);
        //发送http请求
        String tokenString = null;
        try {
            tokenString = HttpClientUtils.get(url);
            //获取access_token
            JSONObject jsonObject = JSONObject.parseObject(tokenString);
            String access_token = jsonObject.getString("access_token");
            return Result.ok(access_token);
        } catch (Exception e) {
            throw new GgktException(20001,"获取access_token失败");
        }
    }

    //获取菜单(封装一级菜单和二级菜单)
    @ApiOperation("获取菜单")
    @GetMapping("findMenuInfo")
    public Result findMenuInfo(){
        List<MenuVo> menuList = iMenuService.findMenuInfo();
        return Result.ok(menuList);
    }

    //获取一级菜单
    @ApiOperation("获取一级菜单")
    @GetMapping("findOneMenuInfo")
    public Result findOneMenuInfo(){
        List<Menu> oneMenuList = iMenuService.findOneMenuInfo();
        return Result.ok(oneMenuList);
    }

    //根据id获取菜单信息
    @ApiOperation("根据id获取菜单")
    @GetMapping("get/{id}")
    public Result getMenuById(@PathVariable Long id){
        Menu menu = iMenuService.getById(id);
        return Result.ok(menu);
    }
    //添加菜单
    @ApiOperation("添加菜单")
    @PostMapping("save")
    public Result saveMenu(@RequestBody Menu menu){
        iMenuService.save(menu);
        return Result.ok(null);
    }
    //修改菜单
    @ApiOperation("修改菜单")
    @PutMapping("update")
    public Result update(@RequestBody Menu menu){
        iMenuService.updateById(menu);
        return Result.ok(null);
    }
    //根据id删除菜单
    @ApiOperation("根据id删除菜单")
    @DeleteMapping("remove/{id}")
    public Result removeById(@PathVariable Long id){
        iMenuService.removeById(id);
        return Result.ok(null);
    }
    //批量删除
    @ApiOperation("批量删除")
    @DeleteMapping("batchRemove")
    public Result batchRemoveByIds(@RequestBody List<Long> ids){
        iMenuService.removeByIds(ids);
        return Result.ok(null);
    }

}
