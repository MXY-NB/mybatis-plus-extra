package com.iv.ersr.common.annotaion;

import java.lang.annotation.*;

/**
 * <p>
 * 自定义注解
 * </p>
 *
 * @author moxiaoyu
 * @since 2023-03-22
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface Min {
    long value() default 0;

    String message() default "请填写正确的数量！";

    boolean isEquals() default true;

    boolean isAudit() default false;
}