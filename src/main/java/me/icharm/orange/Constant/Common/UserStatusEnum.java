package me.icharm.orange.Constant.Common;

import javax.persistence.criteria.CriteriaBuilder;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/16 14:54
 */
public enum  UserStatusEnum {

    NORMAL(5, "正常"),
    UNABLE(60, "不可用"),
    CREATION(65, "初始创建"),
    SCANNED(70, "已扫码, 未授权"),
    REFUSED(75, "拒绝授权");

    public final Integer code;
    public final String msg;

    UserStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
