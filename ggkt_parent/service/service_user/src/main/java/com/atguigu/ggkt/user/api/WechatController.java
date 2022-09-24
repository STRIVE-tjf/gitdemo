package com.atguigu.ggkt.user.api;

import com.alibaba.fastjson.JSON;
import com.atguigu.ggkt.model.user.UserInfo;
import com.atguigu.ggkt.user.service.IUserInfoService;
import com.atguigu.ggkt.utils.JwtHelper;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;

/**
 * @author Tianjinfei
 * @Version 1.0
 */
@Controller//这里需要跳转到指定页面,所以用controller,不用RestController
@RequestMapping("/api/user/wechat")
public class WechatController {
    @Autowired
    private IUserInfoService iUserInfoService;
    @Autowired
    private WxMpService wxMpService;
    @Value("${wechat.userInfoUrl}")
    private String userInfoUrl;

    @GetMapping("authorize")//authorize(批准;许可)
    public String authorize(@RequestParam("returnUrl") String returnUrl, HttpServletRequest request){//需要从前端获取到returnUrl
        String url = wxMpService.oauth2buildAuthorizationUrl(userInfoUrl, WxConsts.OAUTH2_SCOPE_USER_INFO, URLEncoder.encode(returnUrl.replace("guiguketan","#")));
        return "redirect:"+url;//重定向到url
    }
    @GetMapping("userInfo")
    public String userInfo(@RequestParam("code") String code,
                           @RequestParam("state") String returnUrl){
        try {
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
            String openId = wxMpOAuth2AccessToken.getOpenId();
            System.out.println("*******openId:"+openId);
            WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
            System.out.println("****wxMpUser:"+JSON.toJSONString(wxMpUser));
            //根据openid获取用户信息(如果没有,从wxMpUser获取到相关信息加入数据库)
            UserInfo userInfo = iUserInfoService.getUserInfoByOpenId(openId);
            if (userInfo == null){
                UserInfo userInfo1 = new UserInfo();
                userInfo.setUnionId(wxMpUser.getUnionId());
                userInfo.setNickName(wxMpUser.getNickname());
                userInfo.setAvatar(wxMpUser.getHeadImgUrl());
                userInfo.setSex(wxMpUser.getSexId());
                userInfo.setProvince(wxMpUser.getProvince());
                iUserInfoService.save(userInfo1);
            }
            String token = JwtHelper.createToken(userInfo.getId(),userInfo.getName());
            if (returnUrl.indexOf("?") == -1){//路径中没有?进行拼接
                return "redirect:"+returnUrl+"?token="+token;
            }else {//如果路径中有?进行拼接,则使用&
                return "redirect:"+returnUrl+"&token="+token;
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return null;
    }
}
