package com.atguigu.ggkt.vod.controller;

import com.atguigu.ggkt.result.Result;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tianjinfei
 * @Version 1.0
 */
@RestController
@RequestMapping("/admin/vod/user")
//@CrossOrigin//解决跨域的问题的第一种方式
/*第二种解决跨域的方式是httpclient,第三种解决跨域的问题的方式是gateway*/
public class UserLoginController {
    /*
    {
        "code":20000,
        "data":
            {
                "token":
                "admin-token"
            }
     }
    */
    @PostMapping("login")
    public Result login(){
        Map<String,Object> map = new HashMap<>();
        map.put("token","admin-token");
        return Result.ok(map);
    }
    /*
    {
    "code":20000,
    "data":
        {
            "roles":["admin"],
            "introduction":"I am a super administrator",
            "avatar":"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif",
            "name":"Super Admin"
         }
    }
    */
    @GetMapping("info")
    public Result info(){
        Map<String,Object> map = new HashMap<>();
        map.put("roles","admin");
        map.put("introduction","I am a super administrator");
        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        map.put("name","Super Admin");

        return Result.ok(map);
    }

}
