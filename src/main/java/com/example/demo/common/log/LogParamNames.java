/*
 * Copyright © 2018-2025 The Hikvision Company. All Rights Reserved.
 */

package com.example.demo.common.log;

/**
 * Created by maxueli on 2018/12/7.
 */
public class LogParamNames {
    
    private String[] paramNames;
    
    public LogParamNames(String... paramNames) {
        this.paramNames = paramNames;
    }
    
    public String[] getParamNames() {
        return paramNames.clone();
    }
    
    public void setParamNames(String[] paramNames) {
        this.paramNames = paramNames.clone();
    }
}
