package me.icharm.orange.Constant.StockNotice;

import javax.persistence.criteria.CriteriaBuilder;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/16 14:42
 */
public enum NoticeTypeEnum {

    RISE(1, "涨幅预警"),
    DROP(2, "跌幅预警");

    public final Integer code;
    public final String msg;

    NoticeTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
