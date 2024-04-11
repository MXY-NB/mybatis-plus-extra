package com.qingyu.mo.mybatisplus.exception;

/**
 * <p>
 * 自定义异常
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.6.2
 */
public class MybatisPlusJoinException extends RuntimeException {

    public MybatisPlusJoinException(String message) {
        super(message);
    }

    public MybatisPlusJoinException(Throwable throwable) {
        super(throwable);
    }

    public MybatisPlusJoinException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
