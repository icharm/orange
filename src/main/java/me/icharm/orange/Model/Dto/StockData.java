package me.icharm.orange.Model.Dto;

import lombok.Data;

/**
 * Simple stock data dto
 *
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/19 17:55
 */
@Data
public class StockData {

    private String code;
    private String name;

    // Recent trading day open price
    private Double openPrice;

    // Recent trading day close price
    private Double closePrice;

    // Recent real-time stock price
    private Double nowPrice;

    private Double maxPrice;
    private Double minPrice;

    // The latest real-time ups or downs percent, unit:%
    private Double range;
}
