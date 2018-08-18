package me.icharm.orange.Constant.StockNotice;

/**
 * 预警规则状态 枚举
 *
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/15 14:28
 */
public enum StockRuleStatusEnum {

    NORMAL(10, "正常生效状态"),
    OVER_RISE_TIMES(15, "涨幅预警超出当天预警次数"),
    OVER_DROP_TIMES(20, "跌幅预警超过当天预警次数"),
    NORMAL_MAX(25, "正常预警状态最大值"),
    SILENCE(30, "当天无法触发预警"),
    OVER_TIMES(35, "超过当天预警次数"),
    DISABLE(40, "规则被禁止");

    public final Integer code;
    public final String msg;

    StockRuleStatusEnum(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
