package com.qingyu.mo.excel.read.v03;

import com.alibaba.excel.analysis.ExcelReadExecutor;
import com.alibaba.excel.analysis.v03.IgnorableXlsRecordHandler;
import com.alibaba.excel.analysis.v03.XlsListSheetListener;
import com.alibaba.excel.analysis.v03.XlsRecordHandler;
import com.alibaba.excel.analysis.v03.handlers.*;
import com.alibaba.excel.context.xls.XlsReadContext;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.exception.ExcelAnalysisStopException;
import com.alibaba.excel.exception.ExcelAnalysisStopSheetException;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.read.metadata.holder.xls.XlsReadWorkbookHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.eventusermodel.*;
import org.apache.poi.hssf.record.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 自定义动态列名处理器
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
@Slf4j
public class MoXlsSaxAnalyser implements HSSFListener, ExcelReadExecutor {
    
    private static final short DUMMY_RECORD_SID = -1;
    private XlsReadContext xlsReadContext;
    private static final Map<Short, XlsRecordHandler> XLS_RECORD_HANDLER_MAP = new HashMap<>(32);

    static {
        XLS_RECORD_HANDLER_MAP.put(BlankRecord.sid, new BlankRecordHandler());
        XLS_RECORD_HANDLER_MAP.put(BOFRecord.sid, new BofRecordHandler());
        XLS_RECORD_HANDLER_MAP.put(BoolErrRecord.sid, new BoolErrRecordHandler());
        XLS_RECORD_HANDLER_MAP.put(BoundSheetRecord.sid, new BoundSheetRecordHandler());
        XLS_RECORD_HANDLER_MAP.put(DUMMY_RECORD_SID, new MyDummyRecordHandler());
        XLS_RECORD_HANDLER_MAP.put(EOFRecord.sid, new EofRecordHandler());
        XLS_RECORD_HANDLER_MAP.put(FormulaRecord.sid, new FormulaRecordHandler());
        XLS_RECORD_HANDLER_MAP.put(HyperlinkRecord.sid, new HyperlinkRecordHandler());
        XLS_RECORD_HANDLER_MAP.put(IndexRecord.sid, new IndexRecordHandler());
        XLS_RECORD_HANDLER_MAP.put(LabelRecord.sid, new LabelRecordHandler());
        XLS_RECORD_HANDLER_MAP.put(LabelSSTRecord.sid, new LabelSstRecordHandler());
        XLS_RECORD_HANDLER_MAP.put(MergeCellsRecord.sid, new MergeCellsRecordHandler());
        XLS_RECORD_HANDLER_MAP.put(NoteRecord.sid, new NoteRecordHandler());
        XLS_RECORD_HANDLER_MAP.put(NumberRecord.sid, new NumberRecordHandler());
        XLS_RECORD_HANDLER_MAP.put(ObjRecord.sid, new ObjRecordHandler());
        XLS_RECORD_HANDLER_MAP.put(RKRecord.sid, new RkRecordHandler());
        XLS_RECORD_HANDLER_MAP.put(SSTRecord.sid, new SstRecordHandler());
        XLS_RECORD_HANDLER_MAP.put(StringRecord.sid, new StringRecordHandler());
        XLS_RECORD_HANDLER_MAP.put(TextObjectRecord.sid, new TextObjectRecordHandler());
    }

    public MoXlsSaxAnalyser(XlsReadContext xlsReadContext) {
        this.xlsReadContext = xlsReadContext;
    }

    @Override
    public List<ReadSheet> sheetList() {
        try {
            if (xlsReadContext.readWorkbookHolder().getActualSheetDataList() == null) {
                new XlsListSheetListener(xlsReadContext).execute();
            }
        } catch (ExcelAnalysisStopException e) {
            if (log.isDebugEnabled()) {
                log.debug("Custom stop!");
            }
        }
        return xlsReadContext.readWorkbookHolder().getActualSheetDataList();
    }

    @Override
    public void execute() {
        XlsReadWorkbookHolder xlsReadWorkbookHolder = xlsReadContext.xlsReadWorkbookHolder();
        MissingRecordAwareHSSFListener listener = new MissingRecordAwareHSSFListener(this);
        xlsReadWorkbookHolder.setFormatTrackingHSSFListener(new FormatTrackingHSSFListener(listener));
        EventWorkbookBuilder.SheetRecordCollectingListener workbookBuildingListener =
            new EventWorkbookBuilder.SheetRecordCollectingListener(
                xlsReadWorkbookHolder.getFormatTrackingHSSFListener());
        xlsReadWorkbookHolder.setHssfWorkbook(workbookBuildingListener.getStubHSSFWorkbook());
        HSSFEventFactory factory = new HSSFEventFactory();
        HSSFRequest request = new HSSFRequest();
        request.addListenerForAllRecords(xlsReadWorkbookHolder.getFormatTrackingHSSFListener());
        try {
            factory.processWorkbookEvents(request, xlsReadWorkbookHolder.getPoifsFileSystem());
        } catch (IOException e) {
            throw new ExcelAnalysisException(e);
        }
    }

    @Override
    public void processRecord(Record record) {
        XlsRecordHandler handler = XLS_RECORD_HANDLER_MAP.get(record.getSid());
        if (handler == null) {
            return;
        }
        boolean ignoreRecord =
                (handler instanceof IgnorableXlsRecordHandler) && xlsReadContext.xlsReadWorkbookHolder().getIgnoreRecord();
        if (ignoreRecord) {
            // No need to read the current sheet
            return;
        }
        if (!handler.support(xlsReadContext, record)) {
            return;
        }

        try {
            handler.processRecord(xlsReadContext, record);
        } catch (ExcelAnalysisStopSheetException e) {
            if (log.isDebugEnabled()) {
                log.debug("Custom stop!", e);
            }
            xlsReadContext.xlsReadWorkbookHolder().setIgnoreRecord(Boolean.TRUE);
            xlsReadContext.xlsReadWorkbookHolder().setCurrentSheetStopped(Boolean.TRUE);
        }
    }

}