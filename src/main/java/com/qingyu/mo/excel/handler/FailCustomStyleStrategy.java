package com.qingyu.mo.excel.handler;

import cn.hutool.core.map.BiMap;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentFontStyle;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.util.BooleanUtils;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.handler.impl.FillStyleCellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.qingyu.mo.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 自定义失败样式处理器
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
@Slf4j
public class FailCustomStyleStrategy extends FillStyleCellWriteHandler {

    private final List<Field> collect;

    private final Map<String, Object> map;

    private final Map<Integer, Short> colorCache =  MapUtils.newHashMapWithExpectedSize(8);

    public FailCustomStyleStrategy(Class<?> clazz, Map<String, Object> map) {
        Field[] fields = clazz.getDeclaredFields();
        this.collect = Arrays.stream(fields).filter(i -> i.getDeclaredAnnotation(ExcelIgnore.class) == null).collect(Collectors.toList());
        this.map = map;
    }

    @Override
    public void afterCellDispose(CellWriteHandlerContext context) {
        if (BooleanUtils.isTrue(context.getIgnoreFillStyle())) {
            return;
        }
        WriteCellData<?> cellData = context.getFirstCellData();
        if (cellData == null) {
            return;
        }
        WriteCellStyle writeCellStyle = cellData.getOrCreateStyle();
        CellStyle originCellStyle = cellData.getOriginCellStyle();
        if (context.getRowIndex() == 0) {
            String cellValue = context.getCell().getStringCellValue();
            if (Constant.ERROR_EXCEL_HEAD.equalsIgnoreCase(cellValue)) {
                colorCache.put(context.getCell().getColumnIndex(), Font.COLOR_RED);
            } else {
                BiMap<String, Object> biMap = new BiMap<>(map);
                // 如果有映射，就取映射后的值
                if (biMap.containsValue(cellValue)) {
                    String fieldName = biMap.getKey(cellValue);
                    for (Field field : collect) {
                        if (fieldName.equalsIgnoreCase(field.getName())) {
                            Short columnStyle = getColumnStyle(field);
                            if (columnStyle != null) {
                                colorCache.put(context.getCell().getColumnIndex(), columnStyle);
                            }
                        }
                    }
                } else {
                    for (Field field : collect) {
                        ExcelProperty declaredAnnotation = field.getDeclaredAnnotation(ExcelProperty.class);
                        if (declaredAnnotation != null && declaredAnnotation.value().length != 0) {
                            String cellName = declaredAnnotation.value()[0];
                            if (cellName.equalsIgnoreCase(cellValue)) {
                                Short columnStyle = getColumnStyle(field);
                                if (columnStyle != null) {
                                    colorCache.put(context.getCell().getColumnIndex(), columnStyle);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (context.getCell().getRowIndex() != 0 && colorCache.containsKey(context.getCell().getColumnIndex())) {
            WriteFont writeFont = writeCellStyle.getWriteFont();
            if (writeFont == null) {
                writeFont = new WriteFont();
                writeCellStyle.setWriteFont(writeFont);
            }
            writeFont.setColor(colorCache.get(context.getCell().getColumnIndex()));
        }
        WriteWorkbookHolder writeWorkbookHolder = context.getWriteWorkbookHolder();
        writeCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        writeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        context.getCell().setCellStyle(writeWorkbookHolder.createCellStyle(writeCellStyle, originCellStyle));
    }

    private Short getColumnStyle(Field field){
        ContentFontStyle contentFontStyle = field.getAnnotation(ContentFontStyle.class);
        if (contentFontStyle == null) {
            return null;
        }
        return contentFontStyle.color();
    }
}