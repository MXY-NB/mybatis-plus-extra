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
public @interface NotNull {
    String value();

    boolean isAudit() default false;
}