package com.qingyu.mo.excel.write.annotation;


import com.qingyu.mo.entity.IEnum;

import java.lang.annotation.*;

/**
 * <p>
 * excel下拉框自定义注解
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelSelected {

    /**
     * 自定义固定下拉内容
     */
    String[] source() default {};

    /**
     * 自定义动态下拉内容
     */
    Class<? extends IEnum>[] dataType() default {};

    /**
     * 固定下拉内容
     */
    Class<? extends IEnum>[] enumClass() default {};

    /**
     * 设置下拉框的起始行，默认为第二行
     */
    int firstRow() default 1;

    /**
     * 设置下拉框的结束行，默认为最后一行
     */
    int lastRow() default 0x10000;

    /**
     * 设置下拉框是否可以自定义输入
     */
    boolean input() default false;
}