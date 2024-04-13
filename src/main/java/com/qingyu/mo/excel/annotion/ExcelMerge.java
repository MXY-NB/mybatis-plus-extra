package com.qingyu.mo.excel.annotion;

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
@Target({ElementType.FIELD})
public @interface ExcelMerge {
    boolean value() default false;
}