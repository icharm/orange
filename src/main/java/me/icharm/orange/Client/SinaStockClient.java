package me.icharm.orange.Client;

import me.icharm.orange.Model.Dto.StockData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Sina stock data api package
 *
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/19 9:23
 */
@Service
public class SinaStockClient {

    private static final String prefix = "http://hq.sinajs.cn/list=";

    /**
     * Recongized stock type form stock code
     *
     * @param code String
     * @return String :
     * sh  =>  Shanghai Stock Exchange
     * sz  =>  Shenzhen Stock Exchange
     */
    private String recognizeType(String code) {
        String subCode = StringUtils.substring(code, 0, 2);
        String type;
        switch (subCode) {
            case "00":
                type = "sz";    // A
                break;
            case "20":
                type = "sz";    // B
                break;
            case "30":
                type = "sz";    // Entrepreneurship 创业板
                break;
            case "60":
                type = "sh";    // A
                break;
            case "90":
                type = "sh";    // B
                break;
            default:
                type = "sz";
        }
        return type;
    }

    /**
     * Generate full request url
     *
     * @param code String
     * @return String
     */
    private String generateUrl(String code) {
        return prefix + recognizeType(code) + code;
    }

    /**
     * Generate full request url
     *
     * @param codes List<String>
     * @return String
     */
    private String generateUrl(List<String> codes) {
        StringBuilder builder = new StringBuilder();
        for (String code : codes) {
            builder.append(",");
            builder.append(recognizeType(code));
            builder.append(code);
        }
        builder = builder.delete(0, 1);
        return prefix + builder.toString();
    }


    /**
     * Parse string type stock date to Map
     *
     * @param data String
     * @return Map
     */
    private StockData parseStockData(String data) {
        String[] arr = data.split(",");
        StockData stock = new StockData();
        // Match all numbers
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(arr[0]);
        if (!matcher.find()) {
            return null;
        }
        stock.setCode(matcher.group());

        // Match all Chinese characters
        pattern = Pattern.compile("[\\u4e00-\\u9fa5]+$");
        matcher = pattern.matcher(arr[0]);
        if (!matcher.find()) {
            return null;
        }
        stock.setName(matcher.group());

        // Recent trading day open price
        stock.setOpenPrice(Double.valueOf(arr[1]));
        // Recent trading day close price
        Double close = Double.valueOf(arr[2]);
        stock.setClosePrice(Double.valueOf(arr[2]));

        // Recent real-time stock price
        Double now = Double.valueOf(arr[3]);
        stock.setNowPrice(now);

        stock.setMaxPrice(Double.valueOf(arr[4]));
        stock.setMinPrice(Double.valueOf(arr[5]));

        // The latest real-time ups or downs
        BigDecimal range = new BigDecimal((now - close) / close)
                .multiply(new BigDecimal(100))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
        stock.setRange(range.doubleValue());

        return stock;
    }

    /**
     * Parse multiple stock data
     *
     * @param data String
     * @return List
     */
    private List<StockData> parseMultipleStockData(String data) {
        String[] arr = data.split(";\n");
        List<StockData> list = new ArrayList<>();
        for (String sd : arr) {
            list.add(parseStockData(sd));
        }
        return list;
    }


    /**
     * Get simple stock data by code
     *
     * @param code String
     * @return Common
     */
    public StockData stockData(String code) {
        String url = generateUrl(code);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> responseEntity = rt.getForEntity(url, String.class);
        String body = responseEntity.getBody();
        if (body.length() < 25) {
            return null;
        }
        return parseStockData(body);
    }

    /**
     * Get list simple stock data by list code
     *
     * @param codes List
     * @return List
     */
    public List<StockData> multipleStockDate(List<String> codes) {
        String url = generateUrl(codes);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> responseEntity = rt.getForEntity(url, String.class);
        String body = responseEntity.getBody();
        if (body.length() < 25) {
            return null;
        }
        return parseMultipleStockData(body);
    }
}
