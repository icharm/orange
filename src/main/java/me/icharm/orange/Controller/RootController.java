package me.icharm.orange.Controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.icharm.orange.Constant.Common.UserRoleEnum;
import me.icharm.orange.Constant.Common.UserStatusEnum;
import me.icharm.orange.Model.Common.User;
import me.icharm.orange.Repository.Common.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 18073621
 * @version 1.0
 */
@Slf4j
public class RootController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WxMpService wxMpService;

    /**
     *  Allow XMLHttpRequest cross domain
     *
     * @param response HttpServletResponse
     */
    public void allowCrossDomain(HttpServletResponse response){
        // To allow cross origin request
        response.setHeader("Access-Control-Allow-Origin", "*");
        // Allowed request type
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        // Cache repose max 3600s
        response.setHeader("Access-Control-Max-Age", "3600");
        // Specify this XMLHttpRequest response
        // response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
    }

    /**
     * Code换取用户信息，新用户入库。
     *
     * @param code
     * @return
     */
    protected User codeToUserinfo(String code) {
        try {
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
            String openid = wxMpOAuth2AccessToken.getOpenId();
            // Determine if the User is already exists in database
            User user = userRepository.findUserByOpenid(openid);
            if (user != null) {
                // old User
                return user;
            }

            // new User
            // then reflesh access_token to SNSAPI_USERINFO Scope type
            wxMpOAuth2AccessToken.setScope(WxConsts.OAuth2Scope.SNSAPI_USERINFO);
            wxMpOAuth2AccessToken = wxMpService.oauth2refreshAccessToken(wxMpOAuth2AccessToken.getRefreshToken());

            // query User info from wechat server
            WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);

            // save wchatUserinfo
            user = new User();
            user = saveWchatUserinfo(user, wxMpUser);

            // login
            return user;
        } catch (Exception e) {
            log.error(" executed error.", e);
            return null;
        }
    }

    /**
     * Convert wechat User info to icharm User.
     * Then save into database.
     *
     * @param user
     * @param wxMpUser
     */
    private User saveWchatUserinfo(User user, WxMpUser wxMpUser) {
        user.setOpenid(wxMpUser.getOpenId());
        user.setNickName(wxMpUser.getNickname());
        user.setAvatar(wxMpUser.getHeadImgUrl());
        user.setSex(wxMpUser.getSexDesc());
        Map<String, Object> locMap = new HashMap<>();
        locMap.put("country", wxMpUser.getCountry());
        locMap.put("province", wxMpUser.getProvince());
        locMap.put("city", wxMpUser.getCity());
        user.setLocation(JSON.toJSONString(locMap));
        user.setAuthorities(UserRoleEnum.GENERAL.code);
        user.setStatus(UserStatusEnum.NORMAL.code);
        return userRepository.save(user);
    }

}
