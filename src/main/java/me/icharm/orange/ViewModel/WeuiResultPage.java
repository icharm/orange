package me.icharm.orange.ViewModel;

import lombok.Data;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/11/21 17:40
 */
@Data
public class WeuiResultPage {

    /**
     * 视图模板路径
     */
    public String path = "classpath:resources/templates/weui/result_page";

    /**
     * 图标
     */
    private String icon;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 绿色按钮名称
     */
    private String btnPrimary;

    /**
     * 白色按钮名称
     */
    private String btnDefault;

    /**
     * 绿色按钮事件
     */
    private String btnPrimaryAction;

    /**
     * 白色按钮事件
     */
    private String btnDefaultAction;

}
