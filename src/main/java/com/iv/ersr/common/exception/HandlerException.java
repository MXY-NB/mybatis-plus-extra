package com.iv.ersr.common.exception;

import lombok.NoArgsConstructor;

/**
 * <p>
 * 自定义异常类
 * </p>
 *
 * @author moxiaoyu
 * @since 2021-12-24
 */
@NoArgsConstructor
public class HandlerException extends RuntimeException {
    public HandlerException(String message){
        super(message);
    }
}

