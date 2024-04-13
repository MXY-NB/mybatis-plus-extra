package com.qingyu.mo.mybatisplus.annotaion;

import java.lang.annotation.*;

/**
 * <p>
 * 自定义注解 批量更新时允许标注属性值为null
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.6.2
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface UpdateNull {
}