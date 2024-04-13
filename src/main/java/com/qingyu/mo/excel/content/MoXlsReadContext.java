package com.qingyu.mo.excel.content;

import com.alibaba.excel.context.xls.XlsReadContext;
import com.alibaba.excel.read.metadata.ReadWorkbook;
import com.alibaba.excel.read.metadata.holder.xls.XlsReadSheetHolder;
import com.alibaba.excel.read.metadata.holder.xls.XlsReadWorkbookHolder;
import com.alibaba.excel.support.ExcelTypeEnum;

import java.util.Map;

/**
 * <p>
 * 自定义excel解析
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
public class MoXlsReadContext extends MoAnalysisContext implements XlsReadContext {

    public MoXlsReadContext(Map<String, Object> classFieldValueMap, ReadWorkbook readWorkbook, ExcelTypeEnum actualExcelType) {
        super(classFieldValueMap, readWorkbook, actualExcelType);
    }

    @Override
    public XlsReadWorkbookHolder xlsReadWorkbookHolder() {
        return (XlsReadWorkbookHolder)readWorkbookHolder();
    }

    @Override
    public XlsReadSheetHolder xlsReadSheetHolder() {
        return (XlsReadSheetHolder)readSheetHolder();
    }
}