package me.icharm.orange.Constant.Common;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/16 14:46
 */
public enum SystemParameterKeyEnum {

    TEST("TEST", "test");

    public final String code;
    public final String msg;

    SystemParameterKeyEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
