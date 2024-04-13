package com.qingyu.mo.exception;

import lombok.NoArgsConstructor;

/**
 * <p>
 * 自定义异常类
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
@NoArgsConstructor
public class HandlerException extends RuntimeException {
    public HandlerException(String message){
        super(message);
    }
}
