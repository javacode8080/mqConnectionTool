package com.example.demo.business.mqconnection.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.jms.Connection;
import javax.jms.MessageConsumer;
import javax.jms.Session;

/**
 * @author :sunjian23
 * @date : 2022/12/18 16:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivemqConnectionBean {
    private Connection connection;

    private Session session;

    private MessageConsumer consumer;

}
