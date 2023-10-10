/**
  * @ProjectName: hce-auto-deploy
  * @Copyright: 2022 HangZhou Hikvision System Technology Co., Ltd.   
  * Right Reserved.
  * @address: http://www.hikvision.com
  * @author: zhoushen
  * @date: 2022年3月28日 上午11:44:09  
  * @Description: This content is limited to the use of Hangzhou Hikvision, 
  * it is prohibited to forwarding 
 */
package com.example.demo.common.dto;


import com.example.demo.common.error.IErrorCode;
import org.apache.logging.log4j.util.Strings;

/**
 * <p></p>
 * @author zhoushen 
 * @date 2022年3月28日 上午11:44:09
 * @version V1.0
 * @modificationHistory=========== logic or function modify history
 * @modify by user: userName date
 * @modify by reason: function:reason
 * @since
 */
public class BaseResponse<T> {
    
    //返回编码
    private String code = "0";
    
    //返回消息
    private String msg = "success";
    
    //返回数据
    private T data;
    
    public BaseResponse() {
    }
    
    public BaseResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    
    public BaseResponse(String code, T data) {
        this.code = code;
        this.data = data;
    }
    
    public BaseResponse(T data) {
        this.data = data;
    }
    
    public BaseResponse(IErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.msg = errorCode.getMessage();
    }
    
    public BaseResponse(IErrorCode errorCode, String appendMsg) {
        if (errorCode != null) {
            this.code = errorCode.getCode();
            String msg = errorCode.getMessage();
            if (!Strings.isEmpty(appendMsg)) {
                msg = msg + ": " + appendMsg;
            }
            this.msg = msg;
        }
    }
    
    public String getCode() {
        return code;
    }
    
    public BaseResponse<T> setCode(String code) {
        this.code = code;
        return this;
    }
    
    public String getMsg() {
        return msg;
    }
    
    public BaseResponse<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }
    
    public T getData() {
        return data;
    }
    
    public BaseResponse<T> setData(T data) {
        this.data = data;
        return this;
    }
}
