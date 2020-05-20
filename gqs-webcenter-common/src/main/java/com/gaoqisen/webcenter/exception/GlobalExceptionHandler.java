package com.gaoqisen.webcenter.exception;

import com.gaoqisen.webcenter.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.SocketTimeoutException;
import java.sql.SQLSyntaxErrorException;

/**
 * @ClassName GlobalExceptionHandler
 * @Author gaoqisen
 * @Date 2019-06-28
 * @Version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * validation 全局异常捕获
     * @param exception
     * @return
     */
    @ExceptionHandler(org.springframework.validation.BindException.class)
    public Result BindException(org.springframework.validation.BindException exception){
        String message = null;
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            message= error.getField()+error.getDefaultMessage();
        }
        return Result.error(message);
    }
    /**
     * Exception异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(ClassCastException.class)
    public Result classCastException(ClassCastException e){
        String str = "数据类型转换异常";
        logger.error(str, e);
        return Result.error(str);
    }
    @ExceptionHandler(Exception.class)
    public Result expHandle(Exception e){
        logger.error("Exception异常:", e);
        return Result.error( "网络超时!");
    }
    @ExceptionHandler(SQLSyntaxErrorException.class)
    public Result SQLSyntaxErrorException(SQLSyntaxErrorException e){
        logger.error("数据库异常:", e);
        return Result.error("数据库网络异常");
    }
    @ExceptionHandler(SocketTimeoutException.class)
    public Result socketTimeoutException(SocketTimeoutException e){
        logger.error("网络超时：", e.getMessage());
        return  Result.error("网络波动！");
    }
    /**
     * 自定义 AppException 异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(AppException.class)
    @ResponseBody
    public Result appException(Exception e){
        logger.error("自定义系统异常:"+ e.getMessage());
        return  Result.error(e.getMessage());
    }
}
