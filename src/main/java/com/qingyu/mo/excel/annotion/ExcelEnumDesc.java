package com.qingyu.mo.excel.annotion;

import java.lang.annotation.*;

/**
 * <p>
 * 自定义注解
 * </p>
 *
 * @author IVI00
 * @since 2022-06-22
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ExcelEnumDesc {
    String[] value();
}