/**
  * @ProjectName: hce-auto-deploy
  * @Copyright: 2022 HangZhou Hikvision System Technology Co., Ltd.   
  * Right Reserved.
  * @address: http://www.hikvision.com
  * @author: zhoushen
  * @date: 2022年3月28日 上午11:40:00  
  * @Description: This content is limited to the use of Hangzhou Hikvision, 
  * it is prohibited to forwarding 
 */
package com.example.demo.common.error;


public interface IErrorCode {
    
    /**
     * 获取错误码
     * @return
     */
    String getCode();
    
    /**
     * 获取错误信息
     * @return
     */
    String getMessage();
}
