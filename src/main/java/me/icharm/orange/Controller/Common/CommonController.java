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
import me.icharm.orange.Constant.Common.UserStatusEnum;
import me.icharm.orange.Controller.RootController;
import me.icharm.orange.Model.Common.SystemParameter;
import me.icharm.orange.Model.Common.User;
import me.icharm.orange.Model.Dto.JsonResponse;
import me.icharm.orange.Model.Dto.StockData;
import me.icharm.orange.Repository.Common.SystemParameterRepository;
import me.icharm.orange.Repository.Common.UserRepository;
import me.icharm.orange.Service.RedisService;
import me.icharm.orange.Service.SystemService;
import me.icharm.orange.Service.UserAuthenticationService;
import me.icharm.orange.ViewModel.WeuiResultPage;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/20 17:27
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController extends RootController {

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
    SystemParameterRepository systemParameterRepository;

    @Autowired
    RedisService redisService;


    /**
     * get real-time stock data by code
     *
     * @param request HttpServletRequest
     * @return JsonResponse
     */
    @RequestMapping("/stock-data")
    public JsonResponse simpleStockData(HttpServletRequest request, HttpServletResponse resp) {
        allowCrossDomain(resp);
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
    public void auth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        allowCrossDomain(response);
        String token = request.getParameter("token");
        if (StringUtils.isNotBlank(token)) {

            User user = authentication.findByToken(token).get();
            user.setStatus(UserStatusEnum.SCANNED.code);
        }
        String redirectUrl = request.getParameter("url"); // systemService.rootPathOfServer() + "/common/login";
        String url = wxMpService.oauth2buildAuthorizationUrl(redirectUrl, WxConsts.OAuth2Scope.SNSAPI_BASE, null);
        response.sendRedirect(url);
    }

    /**
     * Code换取用户信息，新用户入库。
     *
     * @param code
     * @return
     */
    private User codeToUserinfo(String code) {
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
     * PC扫码授权登录
     *
     * @param code
     * @param resp
     * @return view
     */
    @RequestMapping("/scan-login")
    public String scanLogin(@RequestParam("code") String code, HttpServletResponse resp, ModelMap map) {
        allowCrossDomain(resp);
        User user = null;//codeToUserinfo(code);
        WeuiResultPage object = new WeuiResultPage();
        if (user == null) {
            object.setIcon("success");
            object.setTitle("授权成功");
            object.setBtnPrimary("确定");
            object.setBtnPrimaryAction("window.close();");
        } else {
            object.setIcon("warn");
            object.setTitle("授权失败");
            object.setBtnPrimary("确定");
            object.setBtnPrimaryAction("window.close();");
            object.setBtnDefault("重新授权");
            object.setBtnDefaultAction("history.back();");
        }
        map.addAttribute("object", object);
        return object.path;
    }

    /**
     * 微信内跳转授权登录
     *
     * @param code
     * @return
     * @throws WxErrorException
     */
    @RequestMapping("/login")
    public JsonResponse login(@RequestParam("code") String code, HttpServletResponse resp) throws WxErrorException {
        allowCrossDomain(resp);
        Map<String, Object> dataMap = new HashMap<>();

        User user = codeToUserinfo(code);
        if(user == null) {
            return JsonResponse.error();
        }

        // login
        dataMap.put("token", authentication.login(user));
        dataMap.put("nickName", user.getNickName());
        dataMap.put("avatar", user.getAvatar());
        String data = JSON.toJSONString(dataMap);
        return JsonResponse.success(data);
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
        user.setAuthorities(UserRoleEnum.SN_USER.code);
        return userRepository.save(user);
    }

    /**
     * Logout, delete token in redis.
     *
     * @param token
     */
    @RequestMapping("/logout")
    public void logout(@RequestParam("token") String token) {
        redisService.delete(token);
    }

    /**
     * 二维码绘制code
     * @return
     */
    @RequestMapping("/qrcode")
    public JsonResponse token(@RequestParam("code") String token) {
        if (StringUtils.isBlank(token)) {
            // Generate a empty user.
            User user = new User();
            token =  authentication.login(user);
            return JsonResponse.success(token);
        } else {
            // query qrcode status
            User user = authentication.findByToken(token).get();
            Integer status = user.getStatus();
            if (status >= UserStatusEnum.UNABLE.code)
                return JsonResponse.quick(status, "success", status);
            else if (UserStatusEnum.NORMAL.code.equals(status)) {
                // 用户已授权登录
                return JsonResponse.quick(status, "Has logged.", JSON.toJSONString(user));
            }
        }
        return JsonResponse.error();
    }

}
