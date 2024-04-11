package com.qingyu.mo.annotaion;

import java.lang.annotation.*;

/**
 * <p>
 * 自定义注解
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface Min {
    long value() default 0;

    String message() default "请填写正确的数量！";

    boolean isEquals() default true;

    int groupId() default 0;
}