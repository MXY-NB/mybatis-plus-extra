package com.qingyu.mo.excel.content;

import com.alibaba.excel.context.xlsx.XlsxReadContext;
import com.alibaba.excel.read.metadata.ReadWorkbook;
import com.alibaba.excel.read.metadata.holder.xlsx.XlsxReadSheetHolder;
import com.alibaba.excel.read.metadata.holder.xlsx.XlsxReadWorkbookHolder;
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
public class MoXlsxReadContext extends MoAnalysisContext implements XlsxReadContext {

    public MoXlsxReadContext(Map<String, Object> classFieldValueMap, ReadWorkbook readWorkbook, ExcelTypeEnum actualExcelType) {
        super(classFieldValueMap, readWorkbook, actualExcelType);
    }

    @Override
    public XlsxReadWorkbookHolder xlsxReadWorkbookHolder() {
        return (XlsxReadWorkbookHolder)readWorkbookHolder();
    }

    @Override
    public XlsxReadSheetHolder xlsxReadSheetHolder() {
        return (XlsxReadSheetHolder)readSheetHolder();
    }
}