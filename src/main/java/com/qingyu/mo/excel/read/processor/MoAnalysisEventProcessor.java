package com.qingyu.mo.excel.read.processor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.HeadKindEnum;
import com.alibaba.excel.enums.RowTypeEnum;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.exception.ExcelAnalysisStopException;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ModelBuildEventListener;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import com.alibaba.excel.read.metadata.property.ExcelReadHeadProperty;
import com.alibaba.excel.read.processor.AnalysisEventProcessor;
import com.alibaba.excel.util.ConverterUtils;
import com.alibaba.excel.util.StringUtils;
import com.qingyu.mo.excel.read.listener.MoReadListener;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 自定义excel解析
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
@Slf4j
@NoArgsConstructor
public class MoAnalysisEventProcessor implements AnalysisEventProcessor {

    private Map<String, Object> classFieldValueMap;

    public MoAnalysisEventProcessor(Map<String, Object> classFieldValueMap) {
        this.classFieldValueMap = classFieldValueMap;
    }

    @Override
    public void extra(AnalysisContext analysisContext) {
        dealExtra(analysisContext);
    }

    @Override
    public void endRow(AnalysisContext analysisContext) {
        if (RowTypeEnum.EMPTY.equals(analysisContext.readRowHolder().getRowType())) {
            if (log.isDebugEnabled()) {
                log.warn("Empty row!");
            }
            if (Boolean.TRUE.equals(analysisContext.readWorkbookHolder().getIgnoreEmptyRow())) {
                return;
            }
        }
        dealData(classFieldValueMap, analysisContext);
    }

    @Override
    public void endSheet(AnalysisContext analysisContext) {
        for (ReadListener<?> readListener : analysisContext.currentReadHolder().readListenerList()) {
            readListener.doAfterAllAnalysed(analysisContext);
        }
    }

    private void dealExtra(AnalysisContext analysisContext) {
        for (ReadListener<?> readListener : analysisContext.currentReadHolder().readListenerList()) {
            try {
                readListener.extra(analysisContext.readSheetHolder().getCellExtra(), analysisContext);
            } catch (Exception e) {
                onException(analysisContext, e);
                break;
            }
            if (!readListener.hasNext(analysisContext)) {
                throw new ExcelAnalysisStopException();
            }
        }
    }

    private void onException(AnalysisContext analysisContext, Exception e) {
        for (ReadListener<?> readListenerException : analysisContext.currentReadHolder().readListenerList()) {
            try {
                readListenerException.onException(e, analysisContext);
            } catch (RuntimeException re) {
                throw re;
            } catch (Exception e1) {
                throw new ExcelAnalysisException(e1.getMessage(), e1);
            }
        }
    }

    private void dealData(Map<String, Object> classFieldValueMap, AnalysisContext analysisContext) {
        ReadRowHolder readRowHolder = analysisContext.readRowHolder();
        Map<Integer, ReadCellData<?>> cellDataMap = (Map) readRowHolder.getCellMap();
        readRowHolder.setCurrentRowAnalysisResult(cellDataMap);
        int rowIndex = readRowHolder.getRowIndex();
        int currentHeadRowNumber = analysisContext.readSheetHolder().getHeadRowNumber();

        boolean isData = rowIndex >= currentHeadRowNumber;

        Map<Integer, Map<String, String>> customFieldMap = new HashMap<>(16);
        // Last head column
        if (!isData && currentHeadRowNumber == rowIndex + 1) {
            buildHead(customFieldMap, classFieldValueMap, analysisContext, cellDataMap);
        }
        // Now is data
        for (ReadListener readListener : analysisContext.currentReadHolder().readListenerList()) {
            try {
                if (isData) {
                    Object currentRowAnalysisResult = readRowHolder.getCurrentRowAnalysisResult();
                    if (readListener instanceof ModelBuildEventListener) {
                        readListener.invoke(currentRowAnalysisResult, analysisContext);
                    } else if (readListener instanceof MoReadListener){
                        MoReadListener MoReadListener = (MoReadListener)readListener;
                        MoReadListener.invokeDataMap(currentRowAnalysisResult, cellDataMap, analysisContext);
                    }
                } else {
                    if (readListener instanceof ModelBuildEventListener) {
                        readListener.invokeHead(cellDataMap, analysisContext);
                    } else if (readListener instanceof MoReadListener){
                        MoReadListener MoReadListener = (MoReadListener)readListener;
                        MoReadListener.invokeHeadMap(customFieldMap, cellDataMap, analysisContext);
                    }
                }
            } catch (Exception e) {
                onException(analysisContext, e);
                break;
            }
            if (!readListener.hasNext(analysisContext)) {
                throw new ExcelAnalysisStopException();
            }
        }
    }

    private void buildHead(Map<Integer, Map<String, String>> customFieldMap, Map<String, Object> classFieldValueMap, AnalysisContext analysisContext, Map<Integer, ReadCellData<?>> cellDataMap) {
//        log.error("进入自定义解析头---------------------------------------------");
        if (MapUtil.isEmpty(classFieldValueMap)) {
            log.error("excel自定义解析的字段映射map为空！");
            return;
        }
        if (!HeadKindEnum.CLASS.equals(analysisContext.currentReadHolder().excelReadHeadProperty().getHeadKind())) {
            return;
        }
        Map<Integer, String> dataMap = ConverterUtils.convertToStringMap(cellDataMap, analysisContext);
//        log.error("文件头map:{}", dataMap);
        ExcelReadHeadProperty excelHeadPropertyData = analysisContext.readSheetHolder().excelReadHeadProperty();
        Map<Integer, Head> headMapData = excelHeadPropertyData.getHeadMap();
        Map<Integer, Head> tmpHeadMap = new HashMap<>(headMapData.size() * 4 / 3 + 1);
        for (Map.Entry<Integer, Head> entry : headMapData.entrySet()) {
            Head headData = entry.getValue();
            if (Boolean.TRUE.equals(headData.getForceIndex()) || Boolean.TRUE.equals(!headData.getForceName())) {
                tmpHeadMap.put(entry.getKey(), headData);
                continue;
            }
            // 字段名称
            String headName = headData.getFieldName();
            // 如果有映射，就取映射后的值
            if (classFieldValueMap.containsKey(headName)) {
                String classHeadName = (String) classFieldValueMap.get(headName);
                classFieldValueMap.remove(headName);
                headData.setHeadNameList(CollUtil.newArrayList(classHeadName));
                headName = classHeadName;
            } else {
                List<String> headNameList = headData.getHeadNameList();
                headName = headNameList.get(headNameList.size() - 1);
            }
            headName = CharSequenceUtil.removeAllLineBreaks(headName.trim());
            for (Map.Entry<Integer, String> stringEntry : dataMap.entrySet()) {
                if (stringEntry == null) {
                    continue;
                }
                String headString = stringEntry.getValue();
                Integer stringKey = stringEntry.getKey();
                if (StringUtils.isEmpty(headString)) {
                    continue;
                }
                if (Boolean.TRUE.equals(analysisContext.currentReadHolder().globalConfiguration().getAutoTrim())) {
                    headString = CharSequenceUtil.cleanBlank(CharSequenceUtil.removeAllLineBreaks(headString.trim()));
                }
                if (headName.equals(headString)) {
                    headData.setColumnIndex(stringKey);
                    tmpHeadMap.put(stringKey, headData);
                    break;
                }
            }
        }
        if (MapUtil.isNotEmpty(classFieldValueMap)) {
            for (Map.Entry<String, Object> entry : classFieldValueMap.entrySet()) {
                String headName = (String) entry.getValue();
                for (Map.Entry<Integer, String> stringEntry : dataMap.entrySet()) {
                    if (stringEntry == null) {
                        continue;
                    }
                    String headString = stringEntry.getValue();
                    Integer stringKey = stringEntry.getKey();
                    if (StringUtils.isEmpty(headString)) {
                        continue;
                    }
                    if (Boolean.TRUE.equals(analysisContext.currentReadHolder().globalConfiguration().getAutoTrim())) {
                        headString = CharSequenceUtil.cleanBlank(CharSequenceUtil.removeAllLineBreaks(headString.trim()));
                    }
                    if (headName.equals(headString)) {
                        customFieldMap.put(stringKey, MapUtil.builder("key", entry.getKey()).put("label", headName).build());
                        break;
                    }
                }
            }
        }
        excelHeadPropertyData.setHeadMap(tmpHeadMap);
    }
}