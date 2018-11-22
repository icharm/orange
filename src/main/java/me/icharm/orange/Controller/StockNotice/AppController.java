package me.icharm.orange.Controller.StockNotice;

import lombok.extern.slf4j.Slf4j;
import me.icharm.orange.Client.SinaStockClient;
import me.icharm.orange.Constant.Common.GenericResponseEnum;
import me.icharm.orange.Controller.RootController;
import me.icharm.orange.Model.Common.User;
import me.icharm.orange.Model.Dto.JsonResponse;
import me.icharm.orange.Model.Dto.StockData;
import me.icharm.orange.Model.StockNotice.StockRule;
import me.icharm.orange.Repository.StockNotice.StockRuleRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/19 9:17
 */
@Slf4j
@RestController
@RequestMapping("/stock-notice")
public class AppController extends RootController {

    @Autowired
    SinaStockClient sinaStockClient;

    @Autowired
    StockRuleRepository stockRuleRepository;

    /**
     * add a new stock notice rule to database
     *
     * @param request HttpServletRequest
     * @return JsonResponse
     */
    @RequestMapping("/add")
    public JsonResponse add(
            HttpServletRequest request,
            HttpServletResponse resp,
            @AuthenticationPrincipal final User user
    ) {
        allowCrossDomain(resp);
        StockRule rule = new StockRule();
        try {
            rule.setCode(request.getParameter("code"));
            rule.setName(request.getParameter("name"));
            rule.setBasePrice(Double.valueOf(request.getParameter("base-price")));
            rule.setRiseCheck(Boolean.valueOf(request.getParameter("rise-check")));
            rule.setDropCheck(Boolean.valueOf(request.getParameter("drop-check")));
            if (rule.getRiseCheck()) {
                rule.setRisePrice(Double.valueOf(request.getParameter("rise-price")));
                rule.setRisePercent(Double.valueOf(request.getParameter("rise-percent")));
            }
            if (rule.getDropCheck()) {
                rule.setDropPrice(Double.valueOf(request.getParameter("drop-price")));
                rule.setDropPercent(Double.valueOf(request.getParameter("drop-percent")));
            }

            // Parameter Check
            JsonResponse response = ruleCheck(rule);
            if (response != null) {
                return response;
            }

            // Set User info
            rule.setUser(user.getId());
            rule.setOpenid(user.getOpenid());

            stockRuleRepository.save(rule);
            return JsonResponse.success(null);
        } catch (NullPointerException e) {
            log.error("Some import params is null.", e);
            return JsonResponse.generic(GenericResponseEnum.PARAM_NULL);
        } catch (Exception e) {
            log.error("Unknow error.", e);
            return JsonResponse.error(null);
        }

    }

    /**
     * delete a stock notice rule form database by id
     *
     * @param request HttpServletRequest
     * @return JsonResponse
     */
    @RequestMapping("/delete")
    public JsonResponse delete(HttpServletRequest request, HttpServletResponse resp) {
        allowCrossDomain(resp);
        JsonResponse response = new JsonResponse();
        String strId = request.getParameter("id");
        if (StringUtils.isBlank(strId)) {
            response.setErrorCode(GenericResponseEnum.PARAM.code);
            response.setMessage(GenericResponseEnum.PARAM.msg);
            return response;
        }
        Long id = Long.valueOf(strId);
        stockRuleRepository.deleteById(id);
        response.setErrorCode(GenericResponseEnum.SUCCESS.code);
        response.setMessage(GenericResponseEnum.SUCCESS.msg);
        return response;
    }

    /**
     * stock notice rule parameter check
     *
     * @param rule StockRule
     * @return error -> JsonResponse ; pass -> null
     */
    private JsonResponse ruleCheck(StockRule rule) {
        String code = rule.getCode();
        StockData stock = sinaStockClient.stockData(code);
        Double nowPrice = stock.getNowPrice();
        Double basePrice = rule.getBasePrice();

        if (rule.getRiseCheck()) {
            Double risePrice = rule.getRisePrice();
            if (risePrice <= nowPrice || risePrice <= basePrice) {
                return JsonResponse.quick(1, "涨幅预警价格不能低于现价或者基准价格。");
            }
        }

        if (rule.getDropCheck()) {
            Double dropPrice = rule.getDropPrice();
            if (dropPrice >= nowPrice || dropPrice >= basePrice) {
                return JsonResponse.quick(1, "跌幅预警价格不能高于现价或者基准价格。");
            }
        }
        return null;
    }
}
