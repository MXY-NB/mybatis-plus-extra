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
public @interface NotNull {
    String value();

    int groupId() default 0;
}