package com.example.demo.business.mqconnection.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.websocket.Session;
import java.util.List;

/**
 * @author :sunjian23
 * @date : 2022/12/17 21:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketBean {
    private Session webSocketSession;
    private String clientId;
    //rabbit的通道信息
    private List<ConnectionBean> connectionBeanlList;
}
