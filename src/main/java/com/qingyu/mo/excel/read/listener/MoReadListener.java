package com.qingyu.mo.excel.read.listener;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ConverterUtils;
import com.qingyu.mo.constant.Constant;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Interface to listen for read results
 *
 * @author Jiaju Zhuang
 */
public abstract class MoReadListener<T> implements ReadListener<T> {

    /**
     * 自定义列名map
     */
    protected final Map<Integer, Map<String, String>> customFieldIndexMap = new ConcurrentHashMap<>(16);

    /**
     * 返回的元数据集合
     */
    protected List<Map<Integer, String>> dataMaps;

    /**
     * 列名map
     */
    protected Map<Integer, String> headMap;

    /**
     * 是否发生错误
     */
    protected Boolean isError = Boolean.FALSE;

    /**
     * 时间
     */
    protected LocalDateTime now;

    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    protected static final int BATCH_COUNT = 3000;

    @Override
    public void invoke(T data, AnalysisContext context){}

    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        invokeHeadMap(ConverterUtils.convertToStringMap(headMap, context), context);
    }

    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {}

    // ----------------------- 自定义方法 ---------------------- //

    public void invokeDataMap(T data, Map<Integer, ReadCellData<?>> cellDataMap, AnalysisContext context){
        invoke(data, ConverterUtils.convertToStringMap(cellDataMap, context), context);
    }

    public void invoke(T data, Map<Integer, String> cellDataMap, AnalysisContext context){}

    public void invokeHeadMap(Map<Integer, Map<String, String>> customFieldMap, Map<Integer, ReadCellData<?>> cellDataMap, AnalysisContext context){
        invokeHead(customFieldMap, ConverterUtils.convertToStringMap(cellDataMap, context));
    }

    public void invokeHead(Map<Integer, Map<String, String>> customFieldMap, Map<Integer, String> headMap) {
        headMap.entrySet().removeIf(entry -> Objects.isNull(entry.getValue()));
        this.headMap.putAll(headMap);
        this.customFieldIndexMap.putAll(customFieldMap);
    }

    protected void addErrorEntity(Map<Integer, String> rowData, String errorMsg) {
        if (errorMsg != null) {
            isError = true;
        }
        if (headMap.containsKey(0) && Constant.ERROR_EXCEL_HEAD.equalsIgnoreCase(headMap.get(0))) {
            rowData.put(0, errorMsg);
        } else {
            Map<Integer, String> copyMap = new HashMap<>(rowData);
            rowData.clear();
            rowData.put(0, errorMsg);
            copyMap.forEach((k,v)->rowData.put(k+1, v));
        }
    }

    protected LocalDate parseDate(String dateStr, String message, Map<Integer, String> newRowData) {
        try {
            return LocalDateTimeUtil.parseDate(dateStr, "yyyy-MM-dd");
        } catch (DateTimeParseException e) {
            try {
                return LocalDateTimeUtil.parseDate(dateStr, "yyyy/MM/dd");
            } catch (DateTimeParseException ex) {
                try {
                    return LocalDateTimeUtil.parseDate(dateStr, "yyyy/M/dd");
                } catch (DateTimeParseException ex2) {
                    try {
                        return LocalDateTimeUtil.parseDate(dateStr, "yyyy/M/d");
                    } catch (DateTimeParseException ex3) {
                        try {
                            return LocalDateTimeUtil.parseDate(dateStr, "yyyy年MM月dd日");
                        } catch (DateTimeParseException ex4) {
                            try {
                                return LocalDateTimeUtil.parseDate(dateStr, "yyyy年M月dd日");
                            } catch (DateTimeParseException ex5) {
                                try {
                                    return LocalDateTimeUtil.parseDate(dateStr, "yyyy年M月d日");
                                } catch (DateTimeParseException ex6) {
                                    addErrorEntity(newRowData, "请填写正确的" + message + "！格式示例：yyyy-mm-dd 或 yyyy/mm/dd");
                                    return null;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected LocalDate parseDate(T t, String dateStr) {
        return parseDate(t, dateStr, "业务日期");
    }

    protected LocalDate parseDate(T t, String dateStr, String message) {
        try {
            return LocalDateTimeUtil.parseDate(dateStr, "yyyy-MM-dd");
        } catch (DateTimeParseException e) {
            try {
                return LocalDateTimeUtil.parseDate(dateStr, "yyyy/MM/dd");
            } catch (DateTimeParseException ex) {
                try {
                    return LocalDateTimeUtil.parseDate(dateStr, "yyyy/M/dd");
                } catch (DateTimeParseException ex2) {
                    try {
                        return LocalDateTimeUtil.parseDate(dateStr, "yyyy/M/d");
                    } catch (DateTimeParseException ex3) {
                        addErrorEntity(t, "请填写正确的" + message + "！格式示例：yyyy-mm-dd 或 yyyy/mm/dd");
                        return null;
                    }
                }
            }
        }
    }

    protected boolean checkBigDecimal(Map<Integer, String> rowData, String str, String message) {
        if (str == null) {
            return false;
        }
        try {
            BigDecimal bigDecimal = new BigDecimal(str);
            return false;
        } catch (NumberFormatException e) {
            addErrorEntity(rowData, message + "请填写正确的数字格式！");
            return true;
        }
    }

    protected void addErrorEntity(T t, String errMsg) {}
}