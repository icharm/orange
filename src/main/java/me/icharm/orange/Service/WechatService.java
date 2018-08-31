package me.icharm.orange.Service;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/20 19:12
 */
@Service
public class WechatService {

    @Autowired
    WxMpService wxMpService;

    /**
     * Jump to wechat auth page( Return Url of Wechat authentication server)
     *
     * @param url Wechat authorized callback address(Complete path)
     */
    public String generateAuthUrl(String url) {
        return wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_USERINFO, null);
    }

    public String generateAuthSimpleUrl(String url) {
        return wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_BASE, null);
    }

    public WxMpUser parseAuthData(String authCode) throws WxErrorException {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(authCode);
        WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
        return wxMpUser;
    }
}
