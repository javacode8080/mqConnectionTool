package com.example.demo.business.mqconnection.model;

import com.rabbitmq.client.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author :sunjian23
 * @date : 2022/12/18 16:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RabbitmqConnectionBean {

    private Channel channel;

    private com.rabbitmq.client.Connection connection;

    //交换机名称
    private String exchangeName;

    //路由
    private String  routingKey;

}
