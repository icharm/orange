package me.icharm.orange.Controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.icharm.orange.Constant.Common.UserRole;
import me.icharm.orange.Constant.Common.UserStatusEnum;
import me.icharm.orange.Model.Common.User;
import me.icharm.orange.Repository.Common.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/12/6 17:26
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
     * @param code wechat temp code.
     * @return User
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
     * 对字符串md5加密
     *
     * @param str
     * @return
     */
    protected String md5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            log.error("MD5加密出错");
            return "";
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
        user.addAuthoritie(UserRole.GENERAL);
        user.setStatus(UserStatusEnum.NORMAL.code);
        return userRepository.save(user);
    }

}
