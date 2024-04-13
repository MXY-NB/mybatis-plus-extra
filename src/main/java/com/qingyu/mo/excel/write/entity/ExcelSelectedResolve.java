package com.qingyu.mo.excel.write.entity;

import lombok.Data;

/**
 * <p>
 * excel下拉框自定义注解解析
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
@Data
public class ExcelSelectedResolve {

    /**
     * 下拉内容
     */
    private String[] source;
 
    /**
     * 设置下拉框的起始行，默认为第二行
     */
    private int firstRow;
 
    /**
     * 设置下拉框的结束行，默认为最后一行
     */
    private int lastRow;

    /**
     * 设置下拉框是否可以自定义输入
     */
    private boolean input;
}