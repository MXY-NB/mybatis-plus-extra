package com.qingyu.mo.excel.read;

import com.alibaba.excel.analysis.ExcelAnalyser;
import com.alibaba.excel.analysis.ExcelReadExecutor;
import com.alibaba.excel.analysis.csv.CsvExcelReadExecutor;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.context.csv.CsvReadContext;
import com.alibaba.excel.context.csv.DefaultCsvReadContext;
import com.alibaba.excel.context.xls.XlsReadContext;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.exception.ExcelAnalysisStopException;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.read.metadata.ReadWorkbook;
import com.alibaba.excel.read.metadata.holder.ReadWorkbookHolder;
import com.alibaba.excel.read.metadata.holder.xls.XlsReadWorkbookHolder;
import com.alibaba.excel.read.metadata.holder.xlsx.XlsxReadWorkbookHolder;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.DateUtils;
import com.alibaba.excel.util.FileUtils;
import com.alibaba.excel.util.NumberDataFormatterUtils;
import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.qingyu.mo.excel.content.MoXlsReadContext;
import com.qingyu.mo.excel.content.MoXlsxReadContext;
import com.qingyu.mo.excel.read.v03.MoXlsSaxAnalyser;
import com.qingyu.mo.excel.read.v07.MoXlsxSaxAnalyser;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.filesystem.DocumentFactoryHelper;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.util.IOUtils;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 自定义Excel解析器
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
@Slf4j
public class MyExcelAnalyser implements ExcelAnalyser {

    private AnalysisContext analysisContext;

    private ExcelReadExecutor excelReadExecutor;

    /**
     * Prevent multiple shutdowns
     */
    private boolean finished = false;

    public MyExcelAnalyser(Map<String, Object> classFieldValueMap, ReadWorkbook readWorkbook) {
        try {
            choiceExcelExecutor(classFieldValueMap, readWorkbook);
        } catch (RuntimeException e) {
            finish();
            throw e;
        } catch (Exception e) {
            finish();
            throw new ExcelAnalysisException(e);
        }
    }

    private void choiceExcelExecutor(Map<String, Object> classFieldValueMap, ReadWorkbook readWorkbook) throws Exception {
        ExcelTypeEnum excelType = ExcelTypeEnum.valueOf(readWorkbook);
        switch (excelType) {
            case XLS:
                POIFSFileSystem poifsFileSystem;
                if (readWorkbook.getFile() != null) {
                    poifsFileSystem = new POIFSFileSystem(readWorkbook.getFile());
                } else {
                    poifsFileSystem = new POIFSFileSystem(readWorkbook.getInputStream());
                }
                // So in encrypted excel, it looks like XLS but it's actually XLSX
                if (poifsFileSystem.getRoot().hasEntry(Decryptor.DEFAULT_POIFS_ENTRY)) {
                    InputStream decryptedStream = null;
                    try {
                        decryptedStream = DocumentFactoryHelper
                            .getDecryptedStream(poifsFileSystem.getRoot().getFileSystem(), readWorkbook.getPassword());
                        MoXlsxReadContext xlsxReadContext = new MoXlsxReadContext(classFieldValueMap, readWorkbook, ExcelTypeEnum.XLSX);
                        analysisContext = xlsxReadContext;
                        excelReadExecutor = new MoXlsxSaxAnalyser(xlsxReadContext, decryptedStream);
                        break;
                    } finally {
                        IOUtils.closeQuietly(decryptedStream);
                        // as we processed the full stream already, we can close the filesystem here
                        // otherwise file handles are leaked
                        poifsFileSystem.close();
                    }
                }
                if (readWorkbook.getPassword() != null) {
                    Biff8EncryptionKey.setCurrentUserPassword(readWorkbook.getPassword());
                }
                XlsReadContext xlsReadContext = new MoXlsReadContext(classFieldValueMap, readWorkbook, ExcelTypeEnum.XLS);
                xlsReadContext.xlsReadWorkbookHolder().setPoifsFileSystem(poifsFileSystem);
                analysisContext = xlsReadContext;
                excelReadExecutor = new MoXlsSaxAnalyser(xlsReadContext);
                break;
            case XLSX:
                MoXlsxReadContext xlsxReadContext = new MoXlsxReadContext(classFieldValueMap, readWorkbook, ExcelTypeEnum.XLSX);
                analysisContext = xlsxReadContext;
                excelReadExecutor = new MoXlsxSaxAnalyser(xlsxReadContext, null);
                break;
            case CSV:
                CsvReadContext csvReadContext = new DefaultCsvReadContext(readWorkbook, ExcelTypeEnum.CSV);
                analysisContext = csvReadContext;
                excelReadExecutor = new CsvExcelReadExecutor(csvReadContext);
                break;
            default:
                break;
        }
    }

    @Override
    public void analysis(List<ReadSheet> readSheetList, Boolean readAll) {
        try {
            if (Boolean.TRUE.equals(!readAll) && CollectionUtils.isEmpty(readSheetList)) {
                throw new IllegalArgumentException("Specify at least one read sheet.");
            }
            analysisContext.readWorkbookHolder().setParameterSheetDataList(readSheetList);
            analysisContext.readWorkbookHolder().setReadAll(readAll);
            try {
                excelReadExecutor.execute();
            } catch (ExcelAnalysisStopException e) {
                if (log.isDebugEnabled()) {
                    log.debug("Custom stop!");
                }
            }
        } catch (RuntimeException e) {
            finish();
            throw e;
        } catch (Throwable e) {
            finish();
            throw new ExcelAnalysisException(e);
        }
    }

    @Override
    public void finish() {
        if (finished) {
            return;
        }
        finished = true;
        if (analysisContext == null || analysisContext.readWorkbookHolder() == null) {
            return;
        }
        ReadWorkbookHolder readWorkbookHolder = analysisContext.readWorkbookHolder();

        Throwable throwable = null;

        try {
            if (readWorkbookHolder.getReadCache() != null) {
                readWorkbookHolder.getReadCache().destroy();
            }
        } catch (Throwable t) {
            throwable = t;
        }
        try {
            if ((readWorkbookHolder instanceof XlsxReadWorkbookHolder)
                && ((XlsxReadWorkbookHolder)readWorkbookHolder).getOpcPackage() != null) {
                ((XlsxReadWorkbookHolder)readWorkbookHolder).getOpcPackage().revert();
            }
        } catch (Throwable t) {
            throwable = t;
        }
        try {
            if ((readWorkbookHolder instanceof XlsReadWorkbookHolder)
                && ((XlsReadWorkbookHolder)readWorkbookHolder).getPoifsFileSystem() != null) {
                ((XlsReadWorkbookHolder)readWorkbookHolder).getPoifsFileSystem().close();
            }
        } catch (Throwable t) {
            throwable = t;
        }
        try {
            if (Boolean.TRUE.equals(analysisContext.readWorkbookHolder().getAutoCloseStream())
                && readWorkbookHolder.getInputStream() != null) {
                readWorkbookHolder.getInputStream().close();
            }
        } catch (Throwable t) {
            throwable = t;
        }
        try {
            if (readWorkbookHolder.getTempFile() != null) {
                FileUtils.delete(readWorkbookHolder.getTempFile());
            }
        } catch (Throwable t) {
            throwable = t;
        }

        clearEncrypt03();

        removeThreadLocalCache();

        if (throwable != null) {
            throw new ExcelAnalysisException("Can not close IO.", throwable);
        }
    }

    private void removeThreadLocalCache() {
        NumberDataFormatterUtils.removeThreadLocalCache();
        DateUtils.removeThreadLocalCache();
    }

    private void clearEncrypt03() {
        if (StringUtils.isEmpty(analysisContext.readWorkbookHolder().getPassword())
            || !ExcelTypeEnum.XLS.equals(analysisContext.readWorkbookHolder().getExcelType())) {
            return;
        }
        Biff8EncryptionKey.setCurrentUserPassword(null);
    }
    @Override
    public ExcelReadExecutor excelExecutor() {
        return excelReadExecutor;
    }

    @Override
    public AnalysisContext analysisContext() {
        return analysisContext;
    }
}
