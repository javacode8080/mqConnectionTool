package com.example.demo.business.mqconnection.model;

import com.rabbitmq.client.Channel;

import javax.jms.MessageConsumer;
import javax.jms.Session;

/**
 * @author :sunjian23
 * @date : 2022/12/18 16:26
 */


public class ConnectionBean {

    private ActivemqConnectionBean activemqConnectionBean;

    private RabbitmqConnectionBean rabbitmqConnectionBean;

    public ActivemqConnectionBean getActivemqConnectionBean() {
        return activemqConnectionBean;
    }

    public void setActivemqConnectionBean(javax.jms.Connection connection, Session session, MessageConsumer consumer) {
        ActivemqConnectionBean activemqConnectionBean = new ActivemqConnectionBean(connection, session, consumer);
        this.activemqConnectionBean = activemqConnectionBean;
    }

    public RabbitmqConnectionBean getRabbitmqConnectionBean() {
        return rabbitmqConnectionBean;
    }

    public void setRabbitmqConnectionBean(com.rabbitmq.client.Connection connection, Channel channel, String exchangeName,String  routingKey) {
        RabbitmqConnectionBean rabbitmqConnectionBean = new RabbitmqConnectionBean(channel, connection,exchangeName,routingKey);
        this.rabbitmqConnectionBean = rabbitmqConnectionBean;
    }
}






