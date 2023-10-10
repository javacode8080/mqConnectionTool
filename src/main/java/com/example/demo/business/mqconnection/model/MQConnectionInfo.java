package com.example.demo.business.mqconnection.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author :sunjian23
 * @date : 2022/12/17 14:36
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MQConnectionInfo {
    /****** 使用组件信息 *****/
    private String componentId;
    private String segmentId;
    /** 连接标识：请遵循规范填写 */
    private String clientId;
    /**********************/
    private String ip = "127.0.0.1";
    private String port;
    /***
     * ssl 加密传输端口
     * ssl端口有值则默认使用ssl端口,无则使用port
     */
    private String sslPort;
    private String userName;
    private String password;
    /**
     * 使用MQ枚举值：使用方根据配置中key值mqType 进行判断
     */
    private MqTypeEnum mqTypeEnum;

    /**
     * 启动时需要重连连接次数（非运行期，运行期默认无限重连）
     *
     * 默认是-1 无限重连
     */
    private Integer startReconnectTime = -1;

    //队列名称
    private String  queueName;

    //队列类型
    private String  queueType ="topic";

    /**  rabbitmq 选型专属连接信息 **/
    //虚拟Host
    private String vHost = "/";

    //交换机名称
    private String exchangeName;

    //路由
    private String  routingKey;

    //交换机种类
    private String exchangeType = "fanout";

    //信息(生产者专用)
    private String message;

    public void setMqTypeEnum(String mqTypeEnum) {
        if("1".equals(mqTypeEnum)){
            this.mqTypeEnum = MqTypeEnum.AMQ;
        }else if("2".equals(mqTypeEnum)){
            this.mqTypeEnum = MqTypeEnum.RMQ;
        }
    }
}
