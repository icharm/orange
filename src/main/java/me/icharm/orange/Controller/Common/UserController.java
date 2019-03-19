package me.icharm.orange.Controller.Common;

import me.icharm.orange.Constant.Common.UserRole;
import me.icharm.orange.Controller.RootController;
import me.icharm.orange.Model.Common.User;
import me.icharm.orange.Model.Dto.JsonResponse;
import me.icharm.orange.Model.MsgForward.Mfuser;
import me.icharm.orange.Repository.Common.UserRepository;
import me.icharm.orange.Repository.MsgForward.MfuserRepository;
import me.icharm.orange.Service.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.security.RolesAllowed;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/12/6 17:26
 */
@Controller
@RequestMapping("/user")
@RolesAllowed(UserRole.GENERAL)
public class UserController extends RootController {

    @Autowired
    MfuserRepository mfuserRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserAuthenticationService userAuthenticationService;

    /**
     *
     * @param authentication Authentication
     * @return JsonResponse
     */
    @RequestMapping("mf-user")
    @ResponseBody
    public JsonResponse msgForwardRole(Authentication authentication) {
        // add MsgForward role.
        User user = (User) authentication.getPrincipal();
        user.addAuthoritie(UserRole.MF_USER);
        // add new MsgForward user.
        Mfuser mfuser = new Mfuser();
        // 在mfuser表中建立user关联
        // mfuser.setUser(user);
        mfuser.setOpenid(user.getUsername());
        mfuser.setSecret(md5(user.getOpenid()));
        mfuser = mfuserRepository.save(mfuser);
        user.setMfuser(mfuser);
        userRepository.save(user);
        // 更新完数据，应该刷新redis里用户信息，否则不一致导致出错
        userAuthenticationService.reAuth(user);
        return JsonResponse.success();
    }
}
