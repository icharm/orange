package me.icharm.orange.Constant.Common;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/20 16:38
 */
public enum GenericResponseEnum {

    ERROR(9999, "Error"),
    SUCCESS(0, "Successed"),
    PARAM(9001, "参数错误"),
    PARAM_NULL(9002, "关键参数为空");

    public final Integer code;
    public final String msg;

    GenericResponseEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
