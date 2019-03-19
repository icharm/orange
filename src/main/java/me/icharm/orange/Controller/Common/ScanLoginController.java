package me.icharm.orange.Controller.Common;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpService;
import me.icharm.orange.Constant.Common.UserStatusEnum;
import me.icharm.orange.Controller.RootController;
import me.icharm.orange.Model.Common.User;
import me.icharm.orange.Model.Dto.JsonResponse;
import me.icharm.orange.Model.Dto.SimpleUser;
import me.icharm.orange.Service.QrcodeService;
import me.icharm.orange.Service.SystemService;
import me.icharm.orange.Service.UserAuthenticationService;
import me.icharm.orange.ViewModel.WeuiResultPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/11/30 16:14
 */
@Slf4j
@Controller
@RequestMapping("/slogin")
public class ScanLoginController extends RootController {

    @Autowired
    UserAuthenticationService authentication;

    @Autowired
    QrcodeService qrcode;

    @Autowired
    WxMpService wxMpService;

    @Autowired
    SystemService systemService;

    /**
     * 二维码绘制token获取、状态查询
     * 1. 如果请求参数token为空，则生成新的token
     * 2. token不为空，则查询token的扫码状态
     *  2.1 token statas => CREATION => 初始创建
     *  2.2 token status => SANNED => 已扫码未授权
     *  2.3 token status => NORMAL => 已授权，返回用户信息
     *  2.4 token status => REFUSED => 拒绝授权
     *
     * @return JSON
     */
    @RequestMapping("/qrcode")
    @ResponseBody
    public JsonResponse qrcode(HttpServletRequest request, HttpServletResponse response) {
        // 考虑怎么防止恶意刷token, 每一次刷token都会将空用户写入redis，很容易搞爆内存
        // String ip = request.getRemoteAddr();
        allowCrossDomain(response);
        String token = request.getParameter("token");
        if (StringUtils.isBlank(token)) {
            token =  qrcode.token();
            return JsonResponse.success(token);
        }

        // query qrcode status
        Integer status = qrcode.status(token);
        if (UserStatusEnum.CREATION.code.equals(status)) {
            // not scan
            status = hold30s(token, UserStatusEnum.CREATION.code);
            return JsonResponse.quick(status, "Query successed.", status);
        }
        if (UserStatusEnum.SCANNED.code.equals(status)) {
            // scanned
            status = hold30s(token, UserStatusEnum.SCANNED.code);
//            return JsonResponse.quick(status, "Query successed.", status);

            if (UserStatusEnum.NORMAL.code.equals(status)) {
                // authed, get userinfo.
                Optional<User> op = authentication.findByToken(token);
                if (!op.isPresent()) // 如果qrcode状态为scanned，但是找不到用户信息，则返回已扫码状态，等待前端再次发起查询，此处有可能造成死循环。
                    return JsonResponse.quick(UserStatusEnum.SCANNED.code, "Query successed.", UserStatusEnum.SCANNED.code);
                // 删除对应的qrcode token
                qrcode.delete(token);
                SimpleUser suser = SimpleUser.purify(op.get());
                return JsonResponse.quick(UserStatusEnum.NORMAL.code, "Login successed.", JSON.toJSONString(suser));

            }
        }
        return JsonResponse.quick(status, "Query successed.", status);
    }

    /**
     * Redirect to wechat authentication page
     *
     * @param token String
     * @param response HttpServletResponse
     * @exception IOException redirect failed.
     */
    @RequestMapping("/we-oauth")
    public void weOauth(@RequestParam("token") String token, HttpServletResponse response) throws IOException {
        allowCrossDomain(response);
        // Update qrcode status.
        qrcode.update(token, UserStatusEnum.SCANNED.code);
        // 授权成功后由微信服务端重定向到下面的login控制器
        String redirectUrl = systemService.serverAddress() + "/slogin/login/" + token;
        String url = wxMpService.oauth2buildAuthorizationUrl(redirectUrl, WxConsts.OAuth2Scope.SNSAPI_BASE, null);
        response.sendRedirect(url);
    }

    /**
     * PC扫码授权登录
     *
     * @param token qrcode中的token，经由微信跳转带过来
     * @param code 微信换取用户信息的临时code
     * @return ModelAndView
     */
    @RequestMapping("/login/{token}")
    public ModelAndView login(@PathVariable String token, @RequestParam("code") String code) {
        // todo 测试用户取消授权的返回信息
        User user = codeToUserinfo(code);
        WeuiResultPage view = new WeuiResultPage();
        if (user == null) {
            qrcode.update(token, UserStatusEnum.REFUSED.code);
            view.setIcon("warn");
            view.setTitle("授权失败");
            view.setContent("您拒绝了登录授权，如需授权，请关闭此页面后重新扫码。");
        } else {
            // relogin user first.
            authentication.refresh(token, user);
            // update qrcode status.
            qrcode.update(token, UserStatusEnum.NORMAL.code);
            view.setIcon("success");
            view.setTitle("授权成功");
            view.setContent("小主人，欢迎光临，您可以关闭本页面了，（-：");
        }
        return view.modelAndView();
    }

    /**
     * 每秒查一下redis中qrcode的状态，持续30秒，如果状态改变，则立刻返回改变的状态，30s结束后状态未变，返回原状态
     *
     * @param token String
     * @param state Integer
     * @return Interge
     */
    private Integer hold30s(String token, Integer state) {
        int i = 0;
        Integer status;
        try {
            while (i <= 30) {
                i++;
                status = qrcode.status(token);
                if (!state.equals(status))
                    return status;
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            log.error("Query qrcode status interrupted. " + e);
        }
        return state;
    }
}
