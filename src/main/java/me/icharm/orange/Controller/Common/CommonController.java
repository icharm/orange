package me.icharm.orange.Controller.Common;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.icharm.orange.Client.SinaStockClient;
import me.icharm.orange.Constant.Common.GenericResponseEnum;
import me.icharm.orange.Constant.Common.UserRoleEnum;
import me.icharm.orange.Model.Common.SystemParameter;
import me.icharm.orange.Model.Common.User;
import me.icharm.orange.Model.Dto.JsonResponse;
import me.icharm.orange.Model.Dto.StockData;
import me.icharm.orange.Repository.Common.SystemParameterRepository;
import me.icharm.orange.Repository.Common.UserRepository;
import me.icharm.orange.Service.RedisService;
import me.icharm.orange.Service.SystemService;
import me.icharm.orange.Service.UserAuthenticationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/20 17:27
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    SinaStockClient sinaStockClient;

    @Autowired
    UserAuthenticationService authentication;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WxMpService wxMpService;

    @Autowired
    SystemService systemService;

    @Autowired
    SystemParameterRepository   systemParameterRepository;

    @Autowired
    RedisService redisService;



    /**
     * get real-time stock data by code
     *
     * @param request HttpServletRequest
     * @return JsonResponse
     */
    @RequestMapping("/stock-data")
    public JsonResponse simpleStockData(HttpServletRequest request) {
        JsonResponse response = new JsonResponse();
        String code = request.getParameter("code");
        if (StringUtils.isBlank(code)) {
            response.setErrorCode(GenericResponseEnum.PARAM.code);
            response.setMessage(GenericResponseEnum.PARAM.msg);
        }

        StockData stockData = sinaStockClient.stockData(code);
        response.setErrorCode(GenericResponseEnum.SUCCESS.code);
        response.setMessage(GenericResponseEnum.SUCCESS.msg);
        response.setData(stockData);
        return response;
    }

    /**
     * Redirect to wechat authentication page
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping("/wechat-auth")
    public void auth(HttpServletResponse response) throws IOException {
        String redirectUrl = systemService.rootPathOfServer() + "/common/login";
        String url = wxMpService.oauth2buildAuthorizationUrl(redirectUrl, WxConsts.OAuth2Scope.SNSAPI_BASE, null);
        response.sendRedirect(url);
    }

    /**
     * @param code
     * @return
     * @throws WxErrorException
     */
    @RequestMapping("/login")
    public JsonResponse login(@RequestParam("code") String code) throws WxErrorException {
        JsonResponse response = new JsonResponse();
        Map<String, Object> dataMap = new HashMap<>();

        try {
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
            String openid = wxMpOAuth2AccessToken.getOpenId();
            // Determine if the user is already exists in database
            User user = userRepository.findUserByOpenid(openid);
            if (user != null) {
                // old user
                dataMap.put("token", authentication.login(user));
                dataMap.put("user", user);
                String data = JSON.toJSONString(dataMap);
                return JsonResponse.successResponse(data);
            }

            // new user
            // then reflesh access_token to SNSAPI_USERINFO Scope type
            wxMpOAuth2AccessToken.setScope(WxConsts.OAuth2Scope.SNSAPI_USERINFO);
            wxMpOAuth2AccessToken = wxMpService.oauth2refreshAccessToken(wxMpOAuth2AccessToken.getRefreshToken());

            // query user info from wechat server
            WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);

            // save wchatUserinfo
            user = new User();
            user = saveWchatUserinfo(user, wxMpUser);

            // login
            dataMap.put("token", authentication.login(user));
            dataMap.put("user", user);
            String data = JSON.toJSONString(dataMap);
            return JsonResponse.successResponse(data);

        } catch (Exception e) {
            log.error("Controller.Common.login executed error.", e);
            return JsonResponse.errorResponse(null);
        }
    }

    /**
     * Convert wechat user info to icharm user.
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
        user.setRole(UserRoleEnum.SN_USER.code);
        return userRepository.save(user);
    }

}
