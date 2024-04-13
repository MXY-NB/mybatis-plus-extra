package com.qingyu.mo.excel.read.builder;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.analysis.ExcelAnalyser;
import com.alibaba.excel.exception.ExcelGenerateException;
import com.alibaba.excel.read.builder.AbstractExcelReaderParameterBuilder;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.read.metadata.ReadWorkbook;
import com.qingyu.mo.excel.read.MyExcelAnalyser;

import java.io.InputStream;
import java.util.Map;

/**
 * <p>
 * 自定义excel解析
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
public class MyExcelReaderBuilder extends AbstractExcelReaderParameterBuilder<MyExcelReaderBuilder, ReadWorkbook> {

    private ReadWorkbook readWorkbook;

    private Map<String, Object> classFieldValueMap;

    private ExcelAnalyser excelAnalyser;

    public MyExcelReaderBuilder() {
        this.readWorkbook = new ReadWorkbook();
    }

    /**
     * Read InputStream
     * <p>
     * If 'inputStream' and 'file' all not empty, file first
     */
    public MyExcelReaderBuilder file(InputStream inputStream) {
        readWorkbook.setInputStream(inputStream);
        return this;
    }

    public MyExcelReaderBuilder sheet() {
        excelAnalyser = new MyExcelAnalyser(classFieldValueMap, readWorkbook);
        return this;
    }

    /**
     * Sax read
     */
    public void doRead() {
        if (excelAnalyser == null) {
            throw new ExcelGenerateException("Must use 'EasyExcelFactory.read().sheet()' to call this method");
        }
        excelAnalyser.analysis(CollUtil.newArrayList(new ReadSheet()), Boolean.FALSE);
        excelAnalyser.finish();
    }

    public MyExcelReaderBuilder map(Map<String, Object> classFieldValueMap) {
        this.classFieldValueMap = classFieldValueMap;
        return this;
    }

    @Override
    protected ReadWorkbook parameter() {
        return readWorkbook;
    }
}
