package com.atguigu.ggkt.user.controller;

import com.atguigu.ggkt.model.user.UserInfo;
import com.atguigu.ggkt.user.service.IUserInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author tianjf
 * @since 2022-09-15
 */
@RestController
@RequestMapping("/admin/user/userInfo")
public class UserInfoController {
    @Autowired
    private IUserInfoService iUserInfoService;
    @ApiOperation("获取用户列表")
    @GetMapping("inner/getById/{id}")
    //这里涉及到使用feign来远程调用这个接口,所以为了方便,返回值为UserInfo
    public UserInfo getUser(@PathVariable Long id){
        UserInfo userInfo = iUserInfoService.getById(id);
        return userInfo;
    }
}
