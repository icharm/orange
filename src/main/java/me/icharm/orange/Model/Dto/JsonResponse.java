package me.icharm.orange.Model.Dto;

import lombok.Data;

/**
 * Dto for restfull api response
 *
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/20 14:13
 */
@Data
public class JsonResponse {

    private Integer errorCode;
    private String message;
    private Object data;
}