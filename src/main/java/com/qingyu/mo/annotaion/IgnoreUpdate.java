package com.qingyu.mo.annotaion;

import java.lang.annotation.*;

/**
 * <p>
 * 自定义注解 批量更新时忽略标注属性
 * </p>
 *
 * @author qingyu-mo
 * @since 2023-12-19
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface IgnoreUpdate {
}