package com.example.demo.common.error;


import com.example.demo.common.dto.BaseResponse;
import com.example.demo.common.util.ResponseUtil;
import log.HikLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

/**
 * Description: 异常处理器。该类会处理所有在执行标有@RequestMapping注解的方法时发生的异常
 */
@RestControllerAdvice
@SuppressWarnings("rawtypes")
public class GlobalExceptionHandler {
    
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * get请求的参数校验失败
     *
     * @param exception
     * @return
     */
    @ExceptionHandler
    public BaseResponse handle(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        if (!CollectionUtils.isEmpty(violations)) {
            String message = violations.iterator().next().getMessage();
            String code = ErrorCode.ERR_PARAM_INVALID.getCode();
            logger.error(HikLog.toLog(code, HikLog.message("validate error", "message")), message);
            return ResponseUtil.build(ErrorCode.ERR_PARAM_INVALID, message);
        }
        return ResponseUtil.build(ErrorCode.ERR_ERROR);
    }
    
    /**
     * post请求校验失败
     *
     * @param exception
     * @return
     */
    @ExceptionHandler
    public BaseResponse handle(MethodArgumentNotValidException exception) {
        List<ObjectError> errors = exception.getBindingResult().getAllErrors();
        String code = ErrorCode.ERR_PARAM_INVALID.getCode();
        String firstMsg = null;
        if (!CollectionUtils.isEmpty(errors)) {
            int first = 0;
            for (int i = 0; i < errors.size(); i++) {
                ObjectError error = errors.get(i);
                String message = error.getDefaultMessage();
                String field = null;
                if (error instanceof FieldError) {
                    field = ((FieldError)error).getField();
                }
                if (i == first) {
                    //取一个错误信息传给前端
                    firstMsg = message;
                }
                logger.error(HikLog.toLog(code, HikLog.message("validate error", "field", "message")), field, message);
            }
        }
        return ResponseUtil.build(ErrorCode.ERR_PARAM_INVALID, firstMsg);
    }
    
    /**
     * 其他没有处理的错误
     *
     * @param e
     * @return
     */
    @ExceptionHandler
    public BaseResponse handle(Exception e) {
        if (!(e instanceof HttpMediaTypeNotAcceptableException)) {
            logger.error(HikLog.toLog(ErrorCode.ERR_ERROR.getCode(), "unknown system error"), e);
        }
        return ResponseUtil.build(ErrorCode.ERR_ERROR);
    }
}
