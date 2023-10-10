/**
 * @ProjectName: hce-auto-deploy
 * @Copyright: 2020 HangZhou Hikvision System Technology Co., Ltd.
 * Right Reserved.
 * @address: http://www.hikvision.com
 * @author: zhoushen
 * @date: 2020年1月9日 下午1:08:59
 * @Description: This content is limited to the use of Hangzhou Hikvision,
 * it is prohibited to forwarding
 */
package com.example.demo.common.util;


import com.example.demo.common.dto.BaseResponse;
import com.example.demo.common.error.ErrorCode;
import com.example.demo.common.error.IErrorCode;


public final class ResponseUtil {

    private static final String ERROR_CODE_MSG_KEY = "errorCode.%s.description";

    /**
     * @function 生成基础的错误信息
     */
    public static <T> BaseResponse<T> build(IErrorCode errorCode) {
        BaseResponse<T> response = new BaseResponse<T>();
        response.setCode(errorCode.getCode());
        response.setMsg(errorCode.getMessage());
        return response;
    }

    /**
     * @function 返回带数据的结构，code是string型
     */
    public static <T> BaseResponse<T> build(String errorCode) {
        BaseResponse<T> response = new BaseResponse<T>();
        response.setCode(errorCode);
        response.setMsg(String.format(ERROR_CODE_MSG_KEY, errorCode));
        return response;
    }

    /**
     * @function 返回带数据的结构
     */
    public static <T> BaseResponse<T> build(IErrorCode errorCode, T data) {
        BaseResponse<T> response = new BaseResponse<T>();
        response.setCode(errorCode.getCode());
        response.setMsg(errorCode.getMessage());
        response.setData(data);
        return response;
    }

    /**
     * @function 返回带数据的结构，code是string型
     */
    public static <T> BaseResponse<T> build(String errorCode, T data) {
        BaseResponse<T> response = new BaseResponse<T>();
        response.setCode(errorCode);
        response.setMsg(String.format(ERROR_CODE_MSG_KEY, errorCode));
        response.setData(data);
        return response;
    }

    /**
     * @function 返回转换后的数据的结构
     */
    public static <T> BaseResponse<T> build(BaseResponse<T> source) {
        BaseResponse<T> response = new BaseResponse<T>();
        response.setCode(source.getCode());
        response.setMsg(source.getMsg());
        response.setData(source.getData());
        return response;
    }

    /**
     * @function 是否成功
     */
    public static Boolean isSuccess(String code) {
        return ErrorCode.SUCCESS.getCode().equals(code);
    }

    /**
     * @function 是否成功
     */
    public static Boolean isSuccess(IErrorCode code) {
        return ErrorCode.SUCCESS.equals(code);
    }

    /**
     * @function 是否成功
     */
    public static Boolean isSuccess(BaseResponse<?> result) {
        return null != result && ErrorCode.SUCCESS.getCode().equals(result.getCode());
    }

    /**
     * @function 是否成功
     */
    public static Boolean isSuccessAll(BaseResponse<?> result) {
        return null != result && ErrorCode.SUCCESS.getCode().equals(result.getCode()) && null != result.getData();
    }
}
