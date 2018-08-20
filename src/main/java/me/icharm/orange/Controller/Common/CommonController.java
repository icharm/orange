package me.icharm.orange.Controller.Common;

import me.icharm.orange.Client.SinaStockClient;
import me.icharm.orange.Constant.Common.GenericResponseEnum;
import me.icharm.orange.Model.Dto.JsonResponse;
import me.icharm.orange.Model.Dto.StockData;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/20 17:27
 */
@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    SinaStockClient sinaStockClient;

    /**
     * get real-time stock data by code
     *
     * @param request HttpServletRequest
     * @return JsonResponse
     */
    @RequestMapping("/stock-data")
    public JsonResponse simpleStockData(HttpServletRequest request){
        JsonResponse response = new JsonResponse();
        String code = request.getParameter("code");
        if(StringUtils.isBlank(code)){
            response.setErrorCode(GenericResponseEnum.PARAM.code);
            response.setMessage(GenericResponseEnum.PARAM.msg);
        }

        StockData stockData = sinaStockClient.stockData(code);
        response.setErrorCode(GenericResponseEnum.SUCCESS.code);
        response.setMessage(GenericResponseEnum.SUCCESS.msg);
        response.setData(stockData);
        return response;
    }
}
