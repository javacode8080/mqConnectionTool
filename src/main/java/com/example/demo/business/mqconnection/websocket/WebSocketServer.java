package com.example.demo.business.mqconnection.websocket;

import com.example.demo.business.mqconnection.model.ActivemqConnectionBean;
import com.example.demo.business.mqconnection.model.RabbitmqConnectionBean;
import com.example.demo.business.mqconnection.model.WebSocketBean;
import com.example.demo.common.log.LogUtils;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ServerEndPoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端，
 * 注解的值将被用于监听用户连接的终端访问URL地址，客户端可以通过这个URL连接到websocket服务器端
 */
@ServerEndpoint("/websocket")
@Component
public class WebSocketServer {
    private static final Map<String, WebSocketBean> webSocketMap;
    /**
     * 仅用用于标识客户端编号
     */
    private static final AtomicInteger clientIdMaker;

    static {
        webSocketMap = new ConcurrentHashMap<>();
        clientIdMaker = new AtomicInteger(0);
    }


    @OnOpen
    public void onOpen(Session session) throws IOException {
        WebSocketBean webSocketBean = new WebSocketBean();
        webSocketBean.setWebSocketSession(session);
        webSocketBean.setClientId(session.getId() + "_" + UUID.randomUUID().toString());
        webSocketMap.put(session.getId(), webSocketBean);
        sendMessage(session, "您的唯一Socket标识为：" + webSocketBean.getClientId());
        LogUtils.logInfo("有新连接加入！当前在线人数为"+ clientIdMaker.incrementAndGet());
        System.out.println("有新连接加入！当前在线人数为" + clientIdMaker.get());
    }

    @OnClose
    public void onClose(Session session) {
        //注：每个socket最好的标识就是他的session
        WebSocketBean webSocketBean = webSocketMap.get(session.getId());
        //1.首先判断当前的socket是否有加入到mq的连接，有的话将其全部关闭
        if (null != webSocketBean.getConnectionBeanlList()) {
            webSocketBean.getConnectionBeanlList().stream().forEach((connectionBean) -> {
                try {
                    //1.获取activemq的连接信息，进行关闭
                    ActivemqConnectionBean activemqConnectionBean = connectionBean.getActivemqConnectionBean();
                    if (null != activemqConnectionBean) {
                        //1.1关闭资源，顺序不能反
                        try {
                            activemqConnectionBean.getConsumer().close();
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
                        try {
                            activemqConnectionBean.getSession().close();
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
                        try {
                            activemqConnectionBean.getConnection().close();
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
                    }
                    //2.获取rabbitmq的连接信息，进行关闭
                    RabbitmqConnectionBean rabbitmqConnectionBean = connectionBean.getRabbitmqConnectionBean();
                    if (null != rabbitmqConnectionBean) {
                        //2.1关闭资源，顺序不能反
                        try {
                            rabbitmqConnectionBean.getChannel().close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            rabbitmqConnectionBean.getConnection().close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        //2.从map中移除这个socket连接信息
        webSocketMap.remove(session.getId());
        //3.打印一下当前的连接人数
        LogUtils.logInfo("有一连接关闭！当前在线人数为" + (clientIdMaker.decrementAndGet()));
        System.out.println("有一连接关闭！当前在线人数为" + (clientIdMaker.get()));
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息：" + message);
        //仅发给自己的消息
        try {
            sendMessage(session, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @OnError
    public void onError(Session session, Throwable throwable) throws IOException {
        //传输过程中出现了错误
        if (session.isOpen()) {
            session.close();
        }
        LogUtils.logException(throwable,"WebSocket信息传输过程中出现异常！！！");
        System.out.println("发生错误！");
        throwable.printStackTrace();
    }

    ////群发消息
    public static synchronized void sendMessMassage(String text) {
        webSocketMap.forEach((key, value) -> {
            try {
                sendMessage(value, text);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    //  利用WebSocketBean发送消息
    public static void sendMessage(WebSocketBean webSocketBean, String message) throws IOException {
        sendMessage(webSocketBean.getWebSocketSession(), message);
    }

    // 根据普通Session发送消息
    public static void sendMessage(Session session, String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }

    //    // 根据ClientId发送消息
//    public static void sendMessage(String clientId, String message) throws IOException {
//        WebSocketBean webSocketBean = webSocketMap.get(clientId.substring(0, clientId.indexOf("_")));
//        if(null!=webSocketBean){
//            sendMessage(webSocketBean,message);
//        }
//    }
    // 根据ClientId发送消息
    public static void sendMessage(String sessionId, String message) throws IOException {
        WebSocketBean webSocketBean = webSocketMap.get(sessionId);
        if (null != webSocketBean) {
            sendMessage(webSocketBean, message);
        }
    }

    //根据ClientId获取webSocketBean
    public static WebSocketBean getSocketBeanByClientId(String clientId) throws IOException {
        return webSocketMap.get(clientId.substring(0, clientId.indexOf("_")));
    }

    public static Map<String, WebSocketBean> getWebSocketMap() {
        return webSocketMap;
    }

    public static AtomicInteger getClientIdMaker() {
        return clientIdMaker;
    }
}
