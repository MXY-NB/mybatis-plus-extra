package com.qingyu.mo.mybatisplus.annotaion;

import java.lang.annotation.*;

/**
 * <p>
 * 自定义注解 批量插入时忽略标注属性
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.6.2
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface IgnoreInsert {
}