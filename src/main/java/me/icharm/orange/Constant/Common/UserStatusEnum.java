package me.icharm.orange.Constant.Common;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/16 14:54
 */
public enum  UserStatusEnum {

    NORMAL(5, "正常"),
    UNABLE(60, "不可用");

    public final Integer code;
    public final String msg;

    UserStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
