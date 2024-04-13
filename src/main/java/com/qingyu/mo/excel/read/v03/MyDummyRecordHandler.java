package com.qingyu.mo.excel.read.v03;

import com.alibaba.excel.analysis.v03.IgnorableXlsRecordHandler;
import com.alibaba.excel.analysis.v03.handlers.AbstractXlsRecordHandler;
import com.alibaba.excel.context.xls.XlsReadContext;
import com.alibaba.excel.enums.RowTypeEnum;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import com.alibaba.excel.read.metadata.holder.xls.XlsReadSheetHolder;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.record.Record;

import java.util.LinkedHashMap;

/**
 * <p>
 * 自定义动态列名处理器
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
public class MyDummyRecordHandler extends AbstractXlsRecordHandler implements IgnorableXlsRecordHandler {

    @Override
    public void processRecord(XlsReadContext xlsReadContext, Record record) {
        XlsReadSheetHolder xlsReadSheetHolder = xlsReadContext.xlsReadSheetHolder();
        if (record instanceof LastCellOfRowDummyRecord) {
            // End of this row
            LastCellOfRowDummyRecord lcrdr = (LastCellOfRowDummyRecord)record;
            xlsReadSheetHolder.setRowIndex(lcrdr.getRow());
            xlsReadContext.readRowHolder(new ReadRowHolder(lcrdr.getRow(), xlsReadSheetHolder.getTempRowType(),
                xlsReadContext.readSheetHolder().getGlobalConfiguration(), xlsReadSheetHolder.getCellMap()));
            xlsReadContext.analysisEventProcessor().endRow(xlsReadContext);
            xlsReadSheetHolder.setCellMap(new LinkedHashMap<>());
            xlsReadSheetHolder.setTempRowType(RowTypeEnum.EMPTY);
        } else if (record instanceof MissingCellDummyRecord) {
            MissingCellDummyRecord mcdr = (MissingCellDummyRecord)record;
            xlsReadSheetHolder.getCellMap().put(mcdr.getColumn(),
                ReadCellData.newEmptyInstance(mcdr.getRow(), mcdr.getColumn()));
        }
    }
}