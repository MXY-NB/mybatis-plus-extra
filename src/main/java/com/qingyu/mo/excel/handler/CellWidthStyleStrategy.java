package com.qingyu.mo.excel.handler;

import cn.hutool.core.map.BiMap;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 自动列宽
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
@Slf4j
public class CellWidthStyleStrategy extends AbstractColumnWidthStyleStrategy {

    private static final int MIN_COLUMN_WIDTH = 20;

    private final List<Field> collect;

    private final Map<String, Object> map;

    private final Map<Integer, Integer> columnWidthMap;

    private final Map<Integer, Map<Integer, Integer>> cache = MapUtils.newHashMapWithExpectedSize(8);

    public CellWidthStyleStrategy(Class<?> clazz, Map<String, Object> map) {
        Field[] fields = clazz.getDeclaredFields();
        this.collect = Arrays.stream(fields).filter(i -> i.getDeclaredAnnotation(ExcelIgnore.class) == null).collect(Collectors.toList());
        this.map = map;
        this.columnWidthMap = null;
    }

    public CellWidthStyleStrategy(Map<Integer, Integer> columnWidthMap) {
        this.collect = null;
        this.map = null;
        this.columnWidthMap = columnWidthMap;
    }

    @Override
    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell,
                                  Head head,
                                  Integer relativeRowIndex, Boolean isHead) {
        boolean needSetWidth = isHead || CollectionUtils.isNotEmpty(cellDataList);
        if (!needSetWidth) {
            return;
        }
        Map<Integer, Integer> maxColumnWidthMap = cache.computeIfAbsent(writeSheetHolder.getSheetNo(), k -> new HashMap<>(16));
        Integer columnWidth = dataLength(cell, isHead);
        if (columnWidth < 0) {
            return;
        }
        Integer maxColumnWidth = maxColumnWidthMap.get(cell.getColumnIndex());
        if (maxColumnWidth == null || columnWidth > maxColumnWidth) {
            maxColumnWidthMap.put(cell.getColumnIndex(), columnWidth);
            writeSheetHolder.getSheet().setColumnWidth(cell.getColumnIndex(), columnWidth * 256);
        }
    }

    private Integer dataLength(Cell cell, Boolean isHead) {
        if (Boolean.TRUE.equals(isHead)) {
            try {
                if (map == null) {
                    // 如果有映射，就取映射后的值
                    if (columnWidthMap.containsKey(cell.getColumnIndex())) {
                        return columnWidthMap.get(cell.getColumnIndex());
                    }
                } else {
                    BiMap<String, Object> biMap = new BiMap<>(map);
                    // 如果有映射，就取映射后的值
                    if (biMap.containsValue(cell.getStringCellValue())) {
                        String fieldName = biMap.getKey(cell.getStringCellValue());
                        for (Field field : collect) {
                            if (fieldName.equalsIgnoreCase(field.getName())) {
                                return getColumnWidth(field);
                            }
                        }
                    } else {
                        for (Field field : collect) {
                            ExcelProperty declaredAnnotation = field.getDeclaredAnnotation(ExcelProperty.class);
                            if (declaredAnnotation != null && declaredAnnotation.value().length != 0) {
                                String cellName = declaredAnnotation.value()[0];
                                if (cellName.equalsIgnoreCase(cell.getStringCellValue())) {
                                    return getColumnWidth(field);
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                return MIN_COLUMN_WIDTH;
            }
            return MIN_COLUMN_WIDTH;
        } else {
            return -1;
        }
    }

    private int getColumnWidth(Field field){
        ColumnWidth columnWidth = field.getAnnotation(ColumnWidth.class);
        if (columnWidth == null) {
            return MIN_COLUMN_WIDTH;
        }
        return columnWidth.value();
    }
}