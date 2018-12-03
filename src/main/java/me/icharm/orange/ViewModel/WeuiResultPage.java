package me.icharm.orange.ViewModel;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/11/21 17:40
 */
@Data
@Slf4j
public class WeuiResultPage {

    /**
     * 视图模板路径
     */
    public static String path = "weui/result_fragment";

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

    /**
     * This object convert to ModelMap object.
     *
     * @return ModelMap
     */
    public ModelAndView modelAndView() {
        try {
            ModelAndView mv = new ModelAndView(WeuiResultPage.path);
            // Object to map
            Map<String, String> map = BeanUtils.describe(this);
            mv.addAllObjects(map);
            return mv;
        } catch (Exception e) {
            log.error("WeuiResultPage ViewModel Object convert to map error: " + e);
            return null;
        }
    }
}
