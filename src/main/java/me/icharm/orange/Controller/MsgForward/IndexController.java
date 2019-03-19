package me.icharm.orange.Controller.MsgForward;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import me.icharm.orange.Constant.Common.UserRole;
import me.icharm.orange.Controller.RootController;
import me.icharm.orange.Model.Common.User;
import me.icharm.orange.Model.Dto.JsonResponse;
import me.icharm.orange.Model.MsgForward.Mfuser;
import me.icharm.orange.Model.MsgForward.Record;
import me.icharm.orange.Repository.MsgForward.MfuserRepository;
import me.icharm.orange.Repository.MsgForward.RecordRepository;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/11/16 16:46
 */
@Controller
@Slf4j
@RequestMapping("/msg")
@RolesAllowed(UserRole.MF_USER)
@RestController
public class IndexController extends RootController {

    @Autowired
    MfuserRepository mfuserRepository;

    @Autowired
    RecordRepository recordRepository;

    @Autowired
    WxMpService wxMpService;

    @RequestMapping("/secret")
    public JsonResponse serect(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Mfuser mf = user.getMfuser();
        String secret = mf.getSecret();
        if (StringUtils.isBlank(secret)) {
            secret = md5(user.getOpenid());
            mf.setSecret(secret);
            mfuserRepository.save(mf);
        }
        return JsonResponse.success(secret);
    }
    
//    private void record(String title, String from, String content, String msgId, Mfuser mfuser) {
//        Map<String, String> map = new HashMap<>();
//        map.put("title", title);
//        map.put("from", from);
//        map.put("content", content);
//        String jsonContent = JSON.toJSONString(map);
//        Record record = new Record();
//        record.setContent(jsonContent);
//        record.setMfuser(mfuser);
//        record.setStatus(msgId);
//    }
}
