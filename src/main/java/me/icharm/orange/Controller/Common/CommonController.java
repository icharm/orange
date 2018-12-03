package me.icharm.orange.Controller.Common;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.icharm.orange.Client.SinaStockClient;
import me.icharm.orange.Constant.Common.GenericResponseEnum;
import me.icharm.orange.Controller.RootController;
import me.icharm.orange.Model.Common.User;
import me.icharm.orange.Model.Dto.JsonResponse;
import me.icharm.orange.Model.Dto.StockData;
import me.icharm.orange.Repository.Common.SystemParameterRepository;
import me.icharm.orange.Repository.Common.UserRepository;
import me.icharm.orange.Service.RedisService;
import me.icharm.orange.Service.SystemService;
import me.icharm.orange.Service.UserAuthenticationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/20 17:27
 */
@Slf4j
@Controller
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
    @ResponseBody
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
     * @param response HttpServletResponse
     * @throws IOException
     */
    @RequestMapping("/wechat-auth")
    public ModelAndView auth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        allowCrossDomain(response);
        // 授权成功后由微信服务端重定向的URL
        String redirectUrl = request.getParameter("url"); // systemService.rootPathOfServer() + "/common/login";
        String url = wxMpService.oauth2buildAuthorizationUrl(redirectUrl, WxConsts.OAuth2Scope.SNSAPI_BASE, null);
        response.sendRedirect(url);
        return null;
    }



    /**
     * 微信内跳转授权登录
     *
     * @param code
     * @return
     * @throws WxErrorException
     */
    @RequestMapping("/login")
    @ResponseBody
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
     * Logout, delete token in redis.
     *
     * @param token
     */
    @RequestMapping("/logout")
    public void logout(@RequestParam("token") String token) {
        redisService.delete(token);
    }
}
