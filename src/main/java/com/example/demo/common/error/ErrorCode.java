/**
  * @ProjectName: hce-auto-deploy
  * @Copyright: 2019 HangZhou Hikvision System Technology Co., Ltd.
  * Right Reserved.
  * @address: http://www.hikvision.com
  * @author: zhoushen
  * @date: 2019年9月25日 下午5:51:13
  * @Description: This content is limited to the use of Hangzhou Hikvision,
  * it is prohibited to forwarding
 */

package com.example.demo.common.error;


public enum ErrorCode implements IErrorCode {

    /**
     * 成功
     */
    SUCCESS("0", "成功"),

    /**
     * 失败
     */
    ERR_ERROR("00001", "失败"),

    /**
     * 参数为空
     */
    ERR_PARAM_EMPTY("00002", "参数为空"),

    /**
     * 参数错误
     */
    ERR_PARAM_INVALID("00003", "参数错误"),

    /**
     * 警告信息
     */
    ERR_WARN("00004", "警告信息");



    private String code;

    private String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
