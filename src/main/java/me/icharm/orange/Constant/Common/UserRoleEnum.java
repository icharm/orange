package me.icharm.orange.Constant.Common;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/16 14:51
 */
public enum UserRoleEnum {

    GENERAL("User", "普通用户"),
    SN_USER("SN_USER", "股票预警通知用户"),
    ADMIN("SPUER_ADMIN", "管理员");

    public final String code;
    public final String msg;

    UserRoleEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
