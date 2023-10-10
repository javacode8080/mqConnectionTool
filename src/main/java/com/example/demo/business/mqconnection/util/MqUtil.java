package com.example.demo.business.mqconnection.util;

import com.example.demo.business.mqconnection.model.ConnectionBean;
import com.example.demo.business.mqconnection.model.MQConnectionInfo;
import com.example.demo.business.mqconnection.model.MqTypeEnum;
import com.example.demo.business.mqconnection.model.WebSocketBean;
import com.example.demo.business.mqconnection.websocket.WebSocketServer;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author :sunjian23
 * @date : 2022/12/17 14:31
 */
public class MqUtil {


    /**
     * @param mqConnectionInfo:
     * @param sessionId:
     * @return void
     * @author sunjian23
     * @description TODO:创建一个消费者
     * @date 2022/12/19 8:34
     */
    public static void createConsumer(MQConnectionInfo mqConnectionInfo, String sessionId) throws Exception {
        //1.如果是rabbitmq，就创建rabbitmq的消费者
        if (MqTypeEnum.RMQ.equals(mqConnectionInfo.getMqTypeEnum())) {
            Connection connection = null;
            Channel channel = null;
            try {
                //1.1创建连接工厂，设置工厂属性
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost(mqConnectionInfo.getIp().trim());
                if (null == mqConnectionInfo.getPort().trim() || "".equals(mqConnectionInfo.getPort().trim())) {
                    throw new Exception();
                }
                factory.setPort(Integer.valueOf(mqConnectionInfo.getPort().trim()));
                factory.setUsername(mqConnectionInfo.getUserName().trim());
                factory.setPassword(mqConnectionInfo.getPassword().trim());
                //1.2创建一个新的连接
                connection = factory.newConnection();
                //1.3创建一个新的channel
                channel = connection.createChannel();
                //1.4channel绑定相应信息
                channel.queueBind(mqConnectionInfo.getQueueName(),
                        mqConnectionInfo.getExchangeName(),
                        mqConnectionInfo.getRoutingKey());
                System.out.println("Waiting message.......");
//            //隐藏功能：设置不公平分发
//            int prefetchCount = 1;
//            channel.basicQos(1);
                //1.5创建队列消费者 设置一个监听器监听消费消息
                final Consumer consumerB = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        String message = new String(body, "UTF-8");
                        //打印message
                        System.out.println("Received [" + envelope.getRoutingKey() + "] " + message);
                        //向socket连接传递消息
                        WebSocketServer.sendMessage(sessionId, "-----------------------------------------(" + mqConnectionInfo.getQueueName() + ")接收消息：---------------------------------------------");
                        WebSocketServer.sendMessage(sessionId, "<p class='spanClass5'>" + message + "</p>");
                    }

                    @Override
                    public void handleCancel(String consumerTag) throws IOException {
                        System.out.println("取消消费者:" + consumerTag);
                        //向所有socket连接传递消息
                        WebSocketServer.sendMessage(sessionId, "取消消费者:" + consumerTag);
                    }
                };
                //1.6消费者自动确认：autoAck参数为true
                channel.basicConsume(mqConnectionInfo.getQueueName(), true, consumerB);
                //1.7获取socket对应的socket连接，将channel、connection信息传递给这个socket，以便实现socket关闭时可以立即关闭我认为添加的这个消费者，不影响正常业务的运行
                Map<String, WebSocketBean> webSocketMap = WebSocketServer.getWebSocketMap();
                WebSocketBean webSocketBean = webSocketMap.get(sessionId);
                List<ConnectionBean> connectionBeanlList = webSocketBean.getConnectionBeanlList();
                if (null == connectionBeanlList) {
                    connectionBeanlList = new ArrayList<ConnectionBean>();
                }
                ConnectionBean connectionBean = new ConnectionBean();
                connectionBean.setRabbitmqConnectionBean(connection, channel, mqConnectionInfo.getExchangeName(),
                        mqConnectionInfo.getRoutingKey());
                connectionBeanlList.add(connectionBean);
                //设置channelList，同一个socket可连接多个channel
                webSocketBean.setConnectionBeanlList(connectionBeanlList);
            } catch (Exception e) {
                //关闭连接
                if (null != channel) {
                    try {
                        channel.close();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
                if (null != connection) {
                    try {
                        connection.close();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
                throw e;
            }
        } else {//默认其他情况都认为是activemq
            javax.jms.Connection connection = null;
            Session session = null;
            MessageConsumer consumer = null;
            try {
                //2.1创建连接工厂,按照给定的url地址，采用默认的用户名和密码
                ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://" + mqConnectionInfo.getIp().trim() + ":" + mqConnectionInfo.getPort().trim());
                factory.setUserName(mqConnectionInfo.getUserName().trim());
                factory.setPassword(mqConnectionInfo.getPassword().trim());
                //2.2通过连接工厂，获取链接connection
                connection = factory.createConnection();
                connection.start();//开启链接
                //2.3创建会话session:两个参数：事务：不开启false.签收：自动
                session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                //2.4创建目的地(distination)：目的地有队列queue和主题topic两种
                if ("queue".equals(mqConnectionInfo.getQueueType())) {
                    Queue queue = session.createQueue(mqConnectionInfo.getQueueName());
                    //2.5创建消息的消费者
                    consumer = session.createConsumer(queue);
                } else {
                    Topic topic = session.createTopic(mqConnectionInfo.getQueueName());
                    //2.5创建消息的消费者
                    consumer = session.createConsumer(topic);
                }
                //2.6通过监听器方式来消费消息
                consumer.setMessageListener(msg -> {
                    if (msg != null && msg instanceof TextMessage) {//判断msg是否是TextMessage的一个实例？是的话返回true
                        TextMessage textMessage = (TextMessage) msg;
                        try {
                            System.out.println("消费者获得的消息" + textMessage.getText());
                            //向所有socket连接传递消息
                            WebSocketServer.sendMessage(sessionId, "-----------------------------------------(" + mqConnectionInfo.getQueueName() + ")接收消息：---------------------------------------------");
                            WebSocketServer.sendMessage(sessionId, "<p class='spanClass5'>" + textMessage.getText() + "</p>");
                        } catch (JMSException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                //1.7获取socket对应的socket连接，将channel、connection信息传递给这个socket，以便实现socket关闭时可以立即关闭我认为添加的这个消费者，不影响正常业务的运行
                Map<String, WebSocketBean> webSocketMap = WebSocketServer.getWebSocketMap();
                WebSocketBean webSocketBean = webSocketMap.get(sessionId);
                List<ConnectionBean> connectionBeanlList = webSocketBean.getConnectionBeanlList();
                if (null == connectionBeanlList) {
                    connectionBeanlList = new ArrayList<ConnectionBean>();
                }
                ConnectionBean connectionBean = new ConnectionBean();
                connectionBean.setActivemqConnectionBean(connection, session, consumer);
                connectionBeanlList.add(connectionBean);
                //设置channelList，同一个socket可连接多个channel
                webSocketBean.setConnectionBeanlList(connectionBeanlList);
            } catch (JMSException e) {
                //2.8关闭资源
                if (null != consumer) {
                    try {
                        consumer.close();
                    } catch (JMSException e1) {
                        e1.printStackTrace();
                    }
                }

                if (null != session) {
                    try {
                        session.close();
                    } catch (JMSException e1) {
                        e1.printStackTrace();
                    }
                }
                if (null != connection) {
                    try {
                        connection.close();
                    } catch (JMSException e1) {
                        e1.printStackTrace();
                    }
                }
                throw e;
            }
        }
        //创建mq成功之后返回socket信息
        WebSocketServer.sendMessage(sessionId, "<p class='spanClass2'>创建mq连接成功！！！</p>");
    }


    /**
     * @param mqConnectionInfo:
     * @param sessionId:
     * @return void
     * @author sunjian23
     * @description TODO:创建一个生产者
     * @date 2022/12/19 8:34
     */
    public static void createProducer(MQConnectionInfo mqConnectionInfo, String sessionId) throws Exception {
        //1.如果是rabbitmq，就创建rabbitmq的消费者
        if (MqTypeEnum.RMQ.equals(mqConnectionInfo.getMqTypeEnum())) {
            //1.1创建连接工厂，设置工厂属性
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(mqConnectionInfo.getIp().trim());
            factory.setPort(Integer.valueOf(mqConnectionInfo.getPort().trim()));
            factory.setUsername(mqConnectionInfo.getUserName().trim());
            factory.setPassword(mqConnectionInfo.getPassword().trim());
            Connection connection = null;
            Channel channel = null;
            try {
                //1.2创建一个新的连接
                connection = factory.newConnection();
                //1.3创建一个新的channel
                channel = connection.createChannel();
                //1.4channel绑定相应信息
                //非持久性(参数durable：如果我们声明持久交换（交换将在服务器重启后继续存在），则为真)
                channel.exchangeDeclare(mqConnectionInfo.getExchangeName(), mqConnectionInfo.getExchangeType(), false);
                //信道发布消息
                channel.basicPublish(mqConnectionInfo.getExchangeName(), mqConnectionInfo.getRoutingKey(), null, mqConnectionInfo.getMessage().getBytes());
            } catch (IOException e) {
                //1.3创建一个新的channel
                channel = connection.createChannel();
                //持久性订阅(参数durable：如果我们声明持久交换（交换将在服务器重启后继续存在），则为真)
                channel.exchangeDeclare(mqConnectionInfo.getExchangeName(), mqConnectionInfo.getExchangeType(), true);
                //信道发布消息
                channel.basicPublish(mqConnectionInfo.getExchangeName(), mqConnectionInfo.getRoutingKey(), null, mqConnectionInfo.getMessage().getBytes());
            } finally {
                //关闭连接
                if (null != channel) {
                    try {
                        channel.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (null != connection) {
                    try {
                        connection.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {//默认其他情况都认为是activemq
            //2.1创建连接工厂,按照给定的url地址，采用默认的用户名和密码
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://" + mqConnectionInfo.getIp().trim() + ":" + mqConnectionInfo.getPort().trim());
            factory.setUserName(mqConnectionInfo.getUserName().trim());
            factory.setPassword(mqConnectionInfo.getPassword().trim());
            javax.jms.Connection connection = null;
            Session session = null;
            MessageProducer producer = null;
            try {
                //2.2通过连接工厂，获取链接connection
                connection = factory.createConnection();
                connection.start();//开启链接
                //2.3创建会话session:两个参数：事务：不开启false.签收：自动
                session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                //2.4创建目的地(distination)：目的地有队列queue和主题topic两种
                producer = null;
                if ("queue".equals(mqConnectionInfo.getQueueType())) {
                    Queue queue = session.createQueue(mqConnectionInfo.getQueueName());
                    //2.5创建消息的消费者
                    producer = session.createProducer(queue);
                } else {
                    Topic topic = session.createTopic(mqConnectionInfo.getQueueName());
                    //2.5创建消息的消费者
                    producer = session.createProducer(topic);
                }
                //2.6使用消息生产者生产消息发送到MQ的队列里面
                TextMessage textMessage = session.createTextMessage(mqConnectionInfo.getMessage());
                //2.7通过消息生产者发布到MQ
                producer.send(textMessage);
            } catch (JMSException e) {
//                LogUtils.logException(e, "消息发送失败,请检查参数设置！！！");
                throw e;
            } finally {
                //2.8关闭资源
                if (null != producer) {
                    try {
                        producer.close();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }

                if (null != session) {
                    try {
                        session.close();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
                if (null != connection) {
                    try {
                        connection.close();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //创建mq成功之后返回socket信息
        WebSocketServer.sendMessage(sessionId, "<p class='spanClass2'>消息发送成功！！！</p>");
    }
}
