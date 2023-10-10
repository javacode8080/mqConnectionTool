package com.example.demo.business.mqconnection.controller;

import com.example.demo.business.mqconnection.model.MQConnectionInfo;
import com.example.demo.business.mqconnection.service.ReceiverServer;
import com.example.demo.business.mqconnection.service.SenderService;
import com.example.demo.common.log.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MessageController {


    @Autowired
    private SenderService senderService;

    @Autowired
    private ReceiverServer receiverServer;

    @PostMapping("/createConsumer")
    @ResponseBody
    public String senderMsg(MQConnectionInfo mqConnectionInfo, String clientId) {
        try {
            //判断CliendId是否正确
            if (null == clientId || "".equals(clientId)) {
                LogUtils.logWarn("clientId错误，请刷新页面！！！");
                return "<p class='spanClass'>clientId错误，请刷新页面！！！</p>";
            }
            //判断mq类型是否选择
            if (null == mqConnectionInfo.getMqTypeEnum()) {
                LogUtils.logWarn("请选择mq类型！！！");
                return "<p class='spanClass'>请选择mq类型！！！</p>";
            }
            receiverServer.receive(mqConnectionInfo, clientId);
        } catch (NumberFormatException e) {
            LogUtils.logException(e, "clientId错误，请刷新页面！！！");
            return "<p class='spanClass'>clientId错误，请刷新页面！！！,详细错误信息：</p>" + e.toString();
        } catch (Exception e) {
            LogUtils.logException(e, "创建mq连接失败！！！");
            return "<p class='spanClass'>创建mq连接失败,详细错误信息：</p>" + e.toString();
        }
        return null;
    }


    @GetMapping(path = "/")
    public String welcome() {
        return "index";
    }

    @PostMapping("/sendMessage")
    @ResponseBody
    public String sendMessage(MQConnectionInfo mqConnectionInfo, String clientId) {
        try {
            if (null == clientId || "".equals(clientId)) {
                LogUtils.logWarn("clientId错误，请刷新页面！！！");
                return "<p class='spanClass'>clientId错误，请刷新页面！！！</p>";
            }
            //判断mq类型是否选择
            if (null == mqConnectionInfo.getMqTypeEnum()) {
                LogUtils.logWarn("请选择mq类型！！！");
                return "<p class='spanClass'>请选择mq类型！！！</p>";
            }
            senderService.send(mqConnectionInfo, clientId);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.logException(e, "消息发送失败,请检查参数设置！！！");
            return "<p class='spanClass'>消息发送失败,请检查参数设置！！！,详细错误信息</p>" + e.toString();
        }
        return null;
    }

    @GetMapping(path = "/saySomething")
    public String saySomething() {
        return "show";
    }
//
//    @GetMapping(path = "/consumer")
//    public String consumer() {
//        return "consumer";
//    }
//
//    @GetMapping(path = "/producer")
//    public String producer() {
//        return "producer";
//    }
}








