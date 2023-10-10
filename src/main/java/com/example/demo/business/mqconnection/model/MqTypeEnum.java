package com.example.demo.business.mqconnection.model;

/**
 * @author :sunjian23
 * @date : 2022/12/17 14:39
 */
public enum MqTypeEnum {

    AMQ(1,"amq"),
    RMQ(2,"rmq"),
    ;

    /**
     * mq 类型
     */
    private int mqType;

    /**
     * 选型描述
     */
    private String desc;

    MqTypeEnum(int mqType, String desc) {
        this.mqType = mqType;
        this.desc = desc;
    }
    public int getMqType() {
        return mqType;
    }

    public void setMqType(int mqType) {
        this.mqType = mqType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
