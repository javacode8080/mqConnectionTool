/*
 * Copyright © 2018-2025 The Hikvision Company. All Rights Reserved.
 */

package com.example.demo.common.log;

/**
 * Created by maxueli on 2018/12/7.
 */
public class LogParamValues {
    
    private Object[] paramValues;
    
    public LogParamValues(Object... paramValues) {
        this.paramValues = paramValues;
    }
    
    public Object[] getParamValues() {
        return paramValues.clone();
    }
    
    public void setParamValues(Object[] paramValues) {
        this.paramValues = paramValues.clone();
    }
}
