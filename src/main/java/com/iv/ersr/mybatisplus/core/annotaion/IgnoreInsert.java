package com.iv.ersr.mybatisplus.core.annotaion;

import java.lang.annotation.*;

/**
 * <p>
 * mybatis自定义注解 批量插入
 * </p>
 *
 * @author moxiaoyu
 * @since 2023-03-22
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface IgnoreInsert {
}