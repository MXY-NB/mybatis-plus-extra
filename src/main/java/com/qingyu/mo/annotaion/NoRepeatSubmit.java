package com.qingyu.mo.annotaion;

import java.lang.annotation.*;

/**
 * <p>
 * 检查重复提交标识
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NoRepeatSubmit {
}
