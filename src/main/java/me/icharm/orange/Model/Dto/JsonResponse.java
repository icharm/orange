package me.icharm.orange.Model.Dto;

import lombok.Data;
import me.icharm.orange.Constant.Common.GenericResponseEnum;

/**
 * Dto for restfull api response body
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
    public static JsonResponse success(Object data) {
        JsonResponse response = new JsonResponse();
        response.setErrorCode(GenericResponseEnum.SUCCESS.code);
        response.setMessage(GenericResponseEnum.SUCCESS.msg);
        response.setData(data);
        return response;
    }

    public static JsonResponse success() {
        return JsonResponse.success(null);
    }

    /**
     * Noraml error response
     *
     * @param data
     * @return
     */
    public static JsonResponse error(Object data) {
        JsonResponse response = new JsonResponse();
        response.setErrorCode(GenericResponseEnum.ERROR.code);
        response.setMessage(GenericResponseEnum.ERROR.msg);
        response.setData(data);
        return response;
    }

    public static JsonResponse error() {
        return JsonResponse.error(null);
    }

    /**
     * Common response
     *
     * @param resp GenericResponseEnum object
     * @param data object
     * @return
     */
    public static JsonResponse generic(GenericResponseEnum resp, Object data) {
        JsonResponse response = new JsonResponse();
        response.setErrorCode(resp.code);
        response.setMessage(resp.msg);
        response.setData(data);
        return response;
    }

    /**
     * Common response
     *
     * @param resp GenericResponseEnum object
     * @return
     */
    public static JsonResponse generic(GenericResponseEnum resp) {
        return JsonResponse.generic(resp, null);
    }

    /**
     * Quickly generated resopnse
     *
     * @param code code
     * @param msg msg
     * @param data data
     * @return
     */
    public static JsonResponse quick(Integer code, String msg, Object data) {
        JsonResponse response = new JsonResponse();
        response.setErrorCode(code);
        response.setMessage(msg);
        response.setData(data);
        return response;
    }

    /**
     * Quickly generated resopnse
     *
     * @param code code
     * @param msg msg
     * @return
     */
    public static JsonResponse quick(Integer code, String msg) {
        return JsonResponse.quick(code, msg, null);
    }
}