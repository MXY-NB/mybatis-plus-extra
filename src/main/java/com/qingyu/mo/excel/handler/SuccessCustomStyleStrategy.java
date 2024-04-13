package com.qingyu.mo.excel.handler;

import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.util.BooleanUtils;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.handler.impl.FillStyleCellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;

import java.util.List;

/**
 * <p>
 * 自定义失败样式处理器
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
@Slf4j
public class SuccessCustomStyleStrategy extends FillStyleCellWriteHandler {

    private final List<Integer> errorRowIndex;

    public SuccessCustomStyleStrategy(List<Integer> errorRowIndex) {
        this.errorRowIndex = errorRowIndex;
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
        if (Boolean.FALSE.equals(context.getHead()) && errorRowIndex.contains(context.getRowIndex())) {
            WriteCellStyle writeCellStyle = cellData.getOrCreateStyle();
            CellStyle originCellStyle = cellData.getOriginCellStyle();

            writeCellStyle.setBorderBottom(BorderStyle.THIN);
            writeCellStyle.setBorderLeft(BorderStyle.THIN);
            writeCellStyle.setBorderRight(BorderStyle.THIN);
            writeCellStyle.setBorderTop(BorderStyle.THIN);
            // 自定义背景色
            writeCellStyle.setFillForegroundColor(IndexedColors.TAN.index);
            writeCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);

            WriteWorkbookHolder writeWorkbookHolder = context.getWriteWorkbookHolder();
            writeCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            writeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            context.getCell().setCellStyle(writeWorkbookHolder.createCellStyle(writeCellStyle, originCellStyle));
        }
    }
}