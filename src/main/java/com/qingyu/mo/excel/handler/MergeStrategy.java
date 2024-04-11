package com.qingyu.mo.excel.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.handler.context.RowWriteHandlerContext;
import com.qingyu.mo.excel.annotion.ExcelMerge;
import lombok.Builder;
import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The regions of the loop merge
 *
 * @author Jiaju Zhuang
 */
public class MergeStrategy implements RowWriteHandler {

    /**
     * Each row
     */
    private Integer columnIndex;

    /**
     * Each row
     */
    private MergeObj mergeObj;

    private final Map<String, Integer> map;

    private String checkFieldName;

    @Data
    @Builder
    private static class MergeObj {
        private String cellValue;
        private int startRowIndex;
        private int endRowIndex;
    }

    public MergeStrategy(Class<?> clazz) {
        List<Field> fields = Arrays.stream(clazz.getDeclaredFields()).filter(i -> i.getDeclaredAnnotation(ExcelMerge.class) != null && i.getDeclaredAnnotation(ExcelProperty.class) != null).collect(Collectors.toList());
        if (CollUtil.isEmpty(fields)) {
            map = new HashMap<>(0);
        } else {
            map = new HashMap<>(fields.size());
            for (Field field : fields) {
                String name = Arrays.stream(field.getDeclaredAnnotation(ExcelProperty.class).value()).findFirst().orElse(null);
                if (name != null) {
                    map.put(name, null);
                    if (field.getDeclaredAnnotation(ExcelMerge.class).value()) {
                        checkFieldName = name;
                    }
                }
            }
        }
    }

    @Override
    public void afterRowDispose(RowWriteHandlerContext context) {
        if (checkFieldName != null) {
            if (BooleanUtil.isTrue(context.getHead())) {
                for (Cell cell : context.getRow()) {
                    if (cell.getCellType().equals(CellType.STRING)) {
                        String cellValue = cell.getStringCellValue();
                        if (map.containsKey(cellValue)) {
                            map.put(cellValue, cell.getColumnIndex());
                        }
                        if (checkFieldName.equalsIgnoreCase(cellValue)) {
                            // 单据编号在第几列
                            columnIndex = cell.getColumnIndex();
                        }
                    }
                }
            } else if (MapUtil.isNotEmpty(map)) {
                if (columnIndex != null) {
                    Cell cell = context.getRow().getCell(columnIndex);
                    if (cell.getCellType().equals(CellType.STRING)) {
                        String cellValue = cell.getStringCellValue();
                        if (mergeObj == null) {
                            mergeObj = MergeObj.builder().cellValue(cellValue)
                                    .startRowIndex(cell.getRowIndex())
                                    .endRowIndex(cell.getRowIndex())
                                    .build();
                        } else {
                            // 如果一样
                            if (mergeObj.getCellValue().equalsIgnoreCase(cellValue)) {
                                mergeObj.setEndRowIndex(cell.getRowIndex());
                            } else {
                                // 不一样 合并
                                if (mergeObj.getStartRowIndex() != mergeObj.getEndRowIndex()) {
                                    for (Integer value : map.values()) {
                                        if (value != null) {
                                            CellRangeAddress cellRangeAddress = new CellRangeAddress(mergeObj.getStartRowIndex(),
                                                    mergeObj.getEndRowIndex(), value, value);
                                            context.getWriteSheetHolder().getSheet().addMergedRegionUnsafe(cellRangeAddress);
                                        }
                                    }
                                }
                                mergeObj = MergeObj.builder().cellValue(cellValue)
                                        .startRowIndex(cell.getRowIndex())
                                        .endRowIndex(cell.getRowIndex())
                                        .build();
                            }
                        }
                    }
                }
            }
        }
    }
}
