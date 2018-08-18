package me.icharm.orange.Constant.Common;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/16 14:51
 */
public enum UserPermissionEnum {

    GENERAL(5, "普通用户"),
    ADMIN(60, "管理员");

    public final Integer code;
    public final String msg;

    UserPermissionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
