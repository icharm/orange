package me.icharm.orange.Model.Dto;

import lombok.Data;
import me.icharm.orange.Constant.Common.GenericResponseEnum;

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


    /**
     * Normal success response
     *
     * @param data
     * @return
     */
    public static JsonResponse successResponse(Object data){
        JsonResponse response = new JsonResponse();
        response.setErrorCode(GenericResponseEnum.SUCCESS.code);
        response.setMessage(GenericResponseEnum.SUCCESS.msg);
        response.setData(data);
        return response;
    }

    /**
     * Noraml error response
     *
     * @param data
     * @return
     */
    public static JsonResponse errorResponse(Object data){
        JsonResponse response = new JsonResponse();
        response.setErrorCode(GenericResponseEnum.ERROR.code);
        response.setMessage(GenericResponseEnum.ERROR.msg);
        response.setData(data);
        return response;
    }
}