package me.icharm.orange.Config;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/20 19:55
 */
@Component
public class WechatMpConfig {

    @Autowired
    Environment env;

    @Bean
    public WxMpService wxMpService() {
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage());
        return wxMpService;
    }

    @Bean
    public WxMpConfigStorage wxMpConfigStorage() {
        WxMpInMemoryConfigStorage wxMpConfigStorage = new WxMpInMemoryConfigStorage();
        wxMpConfigStorage.setAppId(env.getProperty("wechat.mp.appId"));
        wxMpConfigStorage.setSecret(env.getProperty("wechat.mp.secret"));
        wxMpConfigStorage.setToken(env.getProperty("wechat.mp.token"));
        wxMpConfigStorage.setAesKey(env.getProperty("wechat.mp.aesKey"));
        return wxMpConfigStorage;
    }
}
