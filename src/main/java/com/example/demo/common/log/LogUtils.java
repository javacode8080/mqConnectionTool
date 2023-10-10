/*
 * Copyright © 2018-2025 The Hikvision Company. All Rights Reserved.
 */

package com.example.demo.common.log;


import com.example.demo.common.error.ErrorCode;
import com.example.demo.common.error.IErrorCode;
import log.HikLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.Base64;
import java.util.Iterator;

/**
 * <p>封装错误日志</p>
 *
 * @author zhoushen
 * @version V1.0
 * @date 2019年12月27日 上午10:37:16
 * @modificationHistory=========== logic or function modify history
 * @modify by user: userName date
 * @modify by reason: function:reason
 * @since
 */
public final class LogUtils {

    private static final String LOG_NAME = "LogUtils";

    private LogUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * @param message
     * @function 追踪日志
     * @author zhoushen
     * @date 2019年12月27日 上午11:02:18
     * @modify by user: userName date
     * @modify by reason: function:reason
     * @since
     */
    public static void logTrace(String message) {
        logTrace(message, null, null);
    }

    /**
     * @param message
     * @param logParamNames
     * @param logParamValues
     * @function 追踪日志，包含参数
     * @author zhoushen
     * @date 2019年12月27日 上午11:02:25
     * @modify by user: userName date
     * @modify by reason: function:reason
     * @since
     */
    public static void logTrace(String message, LogParamNames logParamNames, LogParamValues logParamValues) {
        StackTraceElement[] stacks = (new Throwable()).getStackTrace();
        StackTraceElement stack = null;
        Iterator<StackTraceElement> it = Arrays.asList(stacks).iterator();
        while (it.hasNext()) {
            stack = it.next();
            if (null != stack && !stack.getClassName().endsWith(LOG_NAME)) {
                break;
            }
        }
        StringBuffer sb = new StringBuffer();
        if (null != stack) {
            sb.append(stack.getClassName()).append(":").append(stack.getLineNumber());
        }
        Logger logger = LoggerFactory.getLogger(sb.toString());
        logger.trace(HikLog.toLogWithParam(message, null == logParamNames ? null : logParamNames.getParamNames()),
                null == logParamValues ? null : logParamValues.getParamValues());
    }

    /**
     * @param message
     * @function 调试日志
     * @author zhoushen
     * @date 2019年12月27日 上午11:04:17
     * @modify by user: userName date
     * @modify by reason: function:reason
     * @since
     */
    public static void logDebug(String message) {
        logDebug(message, null, null);
    }

    /**
     * @param message
     * @param logParamNames
     * @param logParamValues
     * @function 调试日志，包含参数
     * @author zhoushen
     * @date 2019年12月27日 上午11:05:57
     * @modify by user: userName date
     * @modify by reason: function:reason
     * @since
     */
    public static void logDebug(String message, LogParamNames logParamNames, LogParamValues logParamValues) {
        StackTraceElement[] stacks = (new Throwable()).getStackTrace();
        StackTraceElement stack = null;
        Iterator<StackTraceElement> it = Arrays.asList(stacks).iterator();
        while (it.hasNext()) {
            stack = it.next();
            if (null != stack && !stack.getClassName().endsWith(LOG_NAME)) {
                break;
            }
        }
        StringBuffer sb = new StringBuffer();
        if (null != stack) {
            sb.append(stack.getClassName()).append(":").append(stack.getLineNumber());
        }
        Logger logger = LoggerFactory.getLogger(sb.toString());
        logger.debug(HikLog.toLogWithParam(message, null == logParamNames ? null : logParamNames.getParamNames()),
                null == logParamValues ? null : logParamValues.getParamValues());
    }

    /**
     * @param message
     * @function 运行日志
     * @author zhoushen
     * @date 2019年12月27日 上午11:06:46
     * @modify by user: userName date
     * @modify by reason: function:reason
     * @since
     */
    public static void logInfo(String message) {
        logInfo(message, null, null);
    }

    /**
     * @param message
     * @param logParamNames
     * @param logParamValues
     * @function 运行日志，包含参数
     * @author zhoushen
     * @date 2019年12月27日 上午11:07:05
     * @modify by user: userName date
     * @modify by reason: function:reason
     * @since
     */
    public static void logInfo(String message, LogParamNames logParamNames, LogParamValues logParamValues) {
        StackTraceElement[] stacks = (new Throwable()).getStackTrace();
        StackTraceElement stack = null;
        Iterator<StackTraceElement> it = Arrays.asList(stacks).iterator();
        while (it.hasNext()) {
            stack = it.next();
            if (null != stack && !stack.getClassName().endsWith(LOG_NAME)) {
                break;
            }
        }
        StringBuffer sb = new StringBuffer();
        if (null != stack) {
            sb.append(stack.getClassName()).append(":").append(stack.getLineNumber());
        }
        Logger logger = LoggerFactory.getLogger(sb.toString());
        logger.info(HikLog.toLogWithParam(message, null == logParamNames ? null : logParamNames.getParamNames()),
                null == logParamValues ? null : logParamValues.getParamValues());
    }

    /**
     * @param message
     * @function 警告方法，均使用通用错误码
     * @author zhoushen
     * @date 2019年12月27日 上午10:56:30
     * @modify by user: userName date
     * @modify by reason: function:reason
     * @since
     */
    public static void logWarn(String message) {
        logWarn(message, null, null);
    }

    /**
     * @param message
     * @param logParamNames
     * @param logParamValues
     * @function 警告方法包含参数信息，均使用通用错误码
     * @author zhoushen
     * @date 2019年12月27日 上午10:58:17
     * @modify by user: userName date
     * @modify by reason: function:reason
     * @since
     */
    public static void logWarn(String message, LogParamNames logParamNames, LogParamValues logParamValues) {
        StackTraceElement[] stacks = (new Throwable()).getStackTrace();
        StackTraceElement stack = null;
        Iterator<StackTraceElement> it = Arrays.asList(stacks).iterator();
        while (it.hasNext()) {
            stack = it.next();
            if (null != stack && !stack.getClassName().endsWith(LOG_NAME)) {
                break;
            }
        }
        StringBuffer sb = new StringBuffer();
        if (null != stack) {
            sb.append(stack.getClassName()).append(":").append(stack.getLineNumber());
        }
        Logger logger = LoggerFactory.getLogger(sb.toString());
        logger.warn(
                HikLog.toLog(ErrorCode.ERR_WARN.getCode(),
                        message,
                        null == logParamNames ? null : logParamNames.getParamNames()),
                null == logParamValues ? null : logParamValues.getParamValues());
    }

    /**
     * @param error
     * @function 输出不带附加错误信息的错误日志
     * @author zhoushen
     * @date 2019年12月27日 上午10:36:50
     * @modify by user: userName date
     * @modify by reason: function:reason
     * @since
     */
    public static void logError(IErrorCode error) {
        logError(error, null, null, null);
    }

    /**
     * @param error
     * @param message
     * @function 输出带附加错误信息的错误日志
     * @author zhoushen
     * @date 2019年12月27日 上午10:37:39
     * @modify by user: userName date
     * @modify by reason: function:reason
     * @since
     */
    public static void logError(IErrorCode error, String message) {
        logError(error, message, null, null);
    }

    /**
     * @param error
     * @param logParamNames
     * @param logParamValues
     * @function 输出带附加错误信息，及参数条件的错误日志
     * @author zhoushen
     * @date 2019年12月27日 上午10:44:44
     * @modify by user: userName date
     * @modify by reason: function:reason
     * @since
     */
    public static void logError(IErrorCode error, LogParamNames logParamNames, LogParamValues logParamValues) {
        logError(error, null, logParamNames, logParamValues);
    }

    /**
     * @param error
     * @param message
     * @param logParamNames
     * @param logParamValues
     * @function 基础错误日志打印
     * @author zhoushen
     * @date 2019年12月27日 上午10:43:51
     * @modify by user: userName date
     * @modify by reason: function:reason
     * @since
     */
    public static void logError(IErrorCode error, String message, LogParamNames logParamNames,
                                LogParamValues logParamValues) {
        StackTraceElement[] stacks = (new Throwable()).getStackTrace();
        StackTraceElement stack = null;
        Iterator<StackTraceElement> it = Arrays.asList(stacks).iterator();
        while (it.hasNext()) {
            stack = it.next();
            if (null != stack && !stack.getClassName().endsWith(LOG_NAME)) {
                break;
            }
        }
        StringBuffer sb = new StringBuffer();
        if (null != stack) {
            sb.append(stack.getClassName()).append(":").append(stack.getLineNumber());
        }
        Logger logger = LoggerFactory.getLogger(sb.toString());
        logger.error(
                HikLog.toLog(error.getCode(),
                        StringUtils.isEmpty(message) ? error.getMessage() : message,
                        null == logParamNames ? null : logParamNames.getParamNames()),
                null == logParamValues ? null : logParamValues.getParamValues());
    }

    /**
     * @param e
     * @function 异常日志
     * @author zhoushen
     * @date 2019年12月27日 上午10:50:22
     * @modify by user: userName date
     * @modify by reason: function:reason
     * @since
     */
    public static void logException(Throwable e) {
        if (e instanceof HttpClientErrorException) {
            HttpClientErrorException s = (HttpClientErrorException) e;
            logException(ErrorCode.ERR_ERROR.getCode(),
                    Base64.getEncoder().encodeToString(s.getResponseBodyAsByteArray()),
                    e);
        } else {
            logException(ErrorCode.ERR_ERROR.getCode(), e.getMessage(), e);
        }
    }

    /**
     * @param e
     * @param error
     * @function 扩展异常日志
     * @author zhoushen
     * @date 2020年3月18日 上午6:32:20
     * @modify by user: userName date
     * @modify by reason: function:reason
     * @since
     */
    public static void logException(Throwable e, String error) {
        logException(ErrorCode.ERR_ERROR.getCode(), error, e);
    }

    /**
     * @param errorCode
     * @param message
     * @param e
     * @function 基础异常日志打印
     * @author zhoushen
     * @date 2021年12月22日 下午2:40:56
     * @modify by user: userName date
     * @modify by reason: function:reason
     * @since
     */
    private static void logException(String errorCode, String message, Throwable e) {
        StackTraceElement[] stacks = (new Throwable()).getStackTrace();
        StackTraceElement stack = null;
        Iterator<StackTraceElement> it = Arrays.asList(stacks).iterator();
        while (it.hasNext()) {
            stack = it.next();
            if (null != stack && !stack.getClassName().endsWith(LOG_NAME)) {
                break;
            }
        }
        StringBuffer sb = new StringBuffer();
        if (null != stack) {
            sb.append(stack.getClassName()).append(":").append(stack.getLineNumber());
        }
        Logger logger = LoggerFactory.getLogger(sb.toString());
        logger.error(
                HikLog.toLog(errorCode, StringUtils.isEmpty(message) ? e.toString() : e.toString() + " " + message), e);
    }
}
