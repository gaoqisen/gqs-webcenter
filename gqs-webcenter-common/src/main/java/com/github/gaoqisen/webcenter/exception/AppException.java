package com.github.gaoqisen.webcenter.exception;

/**
 * @ClassName AppException
 * @Author gaoqisen
 * @Date 2019-06-24
 * @Version 1.0
 */
public class AppException extends RuntimeException{
    public AppException() {
    }
    /**
     * @param message
     */
    public AppException(String message) {
        super(message);
    }
    /**
     * @param cause
     */
    public AppException(Throwable cause) {
        super(cause);
    }
    /**
     * @param message
     * @param cause
     */
    public AppException(String message, Throwable cause) {
        super(message, cause);
    }
    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public AppException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    public AppException(String string, String format) {
        super(format);
    }
}
