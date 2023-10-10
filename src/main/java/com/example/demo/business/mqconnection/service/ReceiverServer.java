package com.example.demo.business.mqconnection.service;

import com.example.demo.business.mqconnection.model.MQConnectionInfo;
import com.example.demo.business.mqconnection.util.MqUtil;
import org.springframework.stereotype.Service;

/**
 * @author :sunjian23
 * @date : 2022/12/18 20:20
 */
@Service
public class ReceiverServer {
    public void receive(MQConnectionInfo mqConnectionInfo, String clientId) throws Exception {
        String sessionId = clientId.substring(0, clientId.indexOf("_"));
        //此处用于校验切割的id是否正确，因为这个id是16进制的，所以可以校验一下。
        Integer.valueOf(sessionId,16);
        MqUtil.createConsumer(mqConnectionInfo, sessionId);
    }
}
