package me.icharm.orange.Controller.MsgForward;

import lombok.extern.slf4j.Slf4j;
import me.icharm.orange.Constant.Common.UserRole;
import me.icharm.orange.Controller.RootController;
import me.icharm.orange.Model.Common.User;
import me.icharm.orange.Model.Dto.JsonResponse;
import me.icharm.orange.Model.MsgForward.Mfuser;
import me.icharm.orange.Repository.MsgForward.MfuserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

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
}
