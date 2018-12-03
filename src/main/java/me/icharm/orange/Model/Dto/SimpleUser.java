package me.icharm.orange.Model.Dto;

import lombok.Data;
import me.icharm.orange.Model.Common.User;

/**
 * Simple user info.
 *
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/12/3 11:36
 */
@Data
public class SimpleUser {

    /**
     * nick name
     */
    private String nickName;

    /**
     * avatar url
     */
    private String avatar;

    /**
     * sex
     */
    private String sex;

    /**
     * User location
     */
    private String location;

    /**
     * Purify userinfo from User.
     *
     * @param user User
     * @return SimpleUser
     */
    public static SimpleUser purify(User user) {
        SimpleUser suser = new SimpleUser();
        suser.setAvatar(user.getAvatar());
        suser.setNickName(user.getNickName());
        suser.setSex(user.getSex());
        suser.setLocation(user.getLocation());
        return suser;
    }
}
