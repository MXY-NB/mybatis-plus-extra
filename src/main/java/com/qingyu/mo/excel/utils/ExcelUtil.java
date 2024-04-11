package com.qingyu.mo.excel.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.style.DefaultStyle;
import com.alibaba.excel.write.style.row.SimpleRowHeightStyleStrategy;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.qingyu.mo.entity.IEnum;
import com.qingyu.mo.excel.annotion.ExcelName;
import com.qingyu.mo.constant.Constant;
import com.qingyu.mo.excel.handler.CellWidthStyleStrategy;
import com.qingyu.mo.excel.handler.FailCustomStyleStrategy;
import com.qingyu.mo.excel.read.builder.MyExcelReaderBuilder;
import com.qingyu.mo.excel.write.annotation.ExcelSelected;
import com.qingyu.mo.excel.write.entity.ExcelSelectedResolve;
import com.qingyu.mo.excel.write.handler.SelectedSheetWriteHandler;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * EasyExcel导出工具类
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
@Slf4j
public abstract class ExcelUtil<Children extends ExcelUtil<Children>> {

    protected final Children typedThis = (Children) this;

    /**
     * 获取自定义内容
     * @param dataTypes 数据类型
     * @return String[]
     */
    protected abstract String[] getSource(Class<? extends IEnum>[] dataTypes);

    //---------------------------------- 写excel -------------------------------------- //

    /**
     * 输出错误文件
     * @param head 导出的表头信息和配置
     * @param errorList 错误信息列表
     * @param byteArrayOutputStream 输出流
     * @param <T> 泛型
     */
    public <T> void writeErrorSheet(Class<T> head, List<T> errorList, ByteArrayOutputStream byteArrayOutputStream) {
        write(byteArrayOutputStream, head, "导入的错误信息", null)
                .doWrite(errorList);
    }

    /**
     * 输出错误文件
     * @param head 导出的表头信息和配置
     * @param headMap 表头信息
     * @param dataMaps 表格信息
     * @param byteArrayOutputStream 输出流
     * @param fieldDataMap 映射
     * @param <T> 泛型
     */
    public <T> void writeErrorSheet(Class<T> head, Map<Integer, String> headMap, List<Map<Integer, String>> dataMaps, ByteArrayOutputStream byteArrayOutputStream, Map<String, Object> fieldDataMap) {
        write(byteArrayOutputStream, head, convertToStringList(headMap), "导入的错误信息", null)
                .registerWriteHandler(new CellWidthStyleStrategy(head, fieldDataMap))
                .registerWriteHandler(new FailCustomStyleStrategy(head, fieldDataMap))
                .registerWriteHandler(new SimpleRowHeightStyleStrategy((short) 30, (short)22))
                .doWrite(dataMaps);
    }

    /**
     * 输出导入文件
     * @param response 响应流
     * @param head 导出的表头信息和配置
     * @param <T> 泛型
     */
    public <T> void writeImportSheet(HttpServletResponse response, Class<T> head) throws IOException {
        write(response, head, "导入模板", null)
                .registerWriteHandler(new SelectedSheetWriteHandler(resolveSelectedAnnotation(head)))
                .doWrite(new ArrayList<>());
    }

    /**
     * 输出导出文件
     * @param response 响应流
     * @param head 导出的表头信息和配置
     * @param list 数据
     * @param <T> 泛型
     */
    public <T> void writeExportSheet(HttpServletResponse response, Class<T> head, List<T> list) throws IOException {
        writeExportSheet(response, head).doWrite(list);
    }

    /**
     * 输出导出文件
     * @param response 响应流
     * @param head 导出的表头信息和配置
     * @param list 数据
     * @param writeHandler 自定义处理器
     * @param <T> 泛型
     */
    public <T> void writeExportSheet(HttpServletResponse response, Class<T> head, List<T> list, WriteHandler writeHandler) throws IOException {
        writeExportSheet(response, head).registerWriteHandler(writeHandler).registerWriteHandler(new DefaultStyle()).doWrite(list);
    }

    /**
     * 输出导出文件
     * @param response 响应流
     * @param head 导出的表头信息和配置
     * @param <T> 泛型
     * @return ExcelWriterSheetBuilder
     */
    private <T> ExcelWriterSheetBuilder writeExportSheet(HttpServletResponse response, Class<T> head) throws IOException {
        return write(response, head, "导出记录", null);
    }

    //---------------------------------- 读excel -------------------------------------- //

    /**
     * Build excel the read
     * @param inputStream 文件流
     * @param readListener 处理器
     * @return Excel reader builder.
     */
    public static MyExcelReaderBuilder read(InputStream inputStream, ReadListener<?> readListener) {
        return read(inputStream, null, readListener);
    }

    /**
     * Build excel the read
     * @param inputStream 文件流
     * @param head 文件列表实体类
     * @param readListener 处理器
     * @return Excel reader builder.
     */
    public static MyExcelReaderBuilder read(InputStream inputStream, Class<?> head, ReadListener<?> readListener) {
        MyExcelReaderBuilder excelReaderBuilder = new MyExcelReaderBuilder();
        excelReaderBuilder.file(inputStream);
        if (head != null) {
            excelReaderBuilder.head(head);
        }
        excelReaderBuilder.registerReadListener(readListener);
        return excelReaderBuilder;
    }

    // ------------------------------------------------------------------------------- //

    /**
     * 输出导出文件
     * @param outputStream 流
     * @param head 导出的表头信息和配置
     * @param excelName 导出文件名
     * @param name 自定义名称
     * @param <T> 泛型
     */
    private <T> ExcelWriterSheetBuilder write(ByteArrayOutputStream outputStream, Class<T> head, String excelName, String name) {
        return EasyExcelFactory.write(outputStream, head).sheet(getName(head, excelName, name));
    }

    /**
     * 输出导出文件
     * @param outputStream 流
     * @param head 导出的表头信息和配置
     * @param headList 导出的表头信息和配置
     * @param excelName 导出文件名
     * @param name 自定义名称
     * @param <T> 泛型
     */
    private <T> ExcelWriterSheetBuilder write(ByteArrayOutputStream outputStream, Class<T> head, List<List<String>> headList, String excelName, String name) {
        return EasyExcelFactory.write(outputStream).head(headList).sheet(getName(head, excelName, name));
    }

    /**
     * 输出导出文件
     * @param response 响应流
     * @param head 导出的表头信息和配置
     * @param <T> 泛型
     * @return ExcelWriterSheetBuilder
     */
    private <T> ExcelWriterSheetBuilder write(HttpServletResponse response, Class<T> head, String excelName, String name) throws IOException {
        excelName = getName(head, excelName, name);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding(CharsetUtil.UTF_8);
        String fileName = URLEncoder.encode(excelName, CharsetUtil.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        return EasyExcelFactory.write(response.getOutputStream(), head).sheet(excelName);
    }

    /**
     * 获取名称
     * @param head 文件class
     * @param suffix excel名称后缀
     * @param name 自定义名称
     * @param <T> 泛型
     * @return String
     */
    private <T> String getName(Class<T> head, String suffix, String name) {
        String excelName = StringPool.EMPTY;
        if (name == null) {
            ExcelName annotation = head.getAnnotation(ExcelName.class);
            if (annotation != null) {
                excelName = annotation.value() + suffix;
            }
        } else {
            excelName = name + suffix;
        }
        return excelName;
    }

    private List<List<String>> convertToStringList(Map<Integer, String> headMap) {
        List<List<String>> res = new ArrayList<>();
        if (!(headMap.containsKey(0) && Constant.ERROR_EXCEL_HEAD.equalsIgnoreCase(headMap.get(0)))) {
            res.add(CollUtil.newArrayList(Constant.ERROR_EXCEL_HEAD));
        }
        for (String value : headMap.values()) {
            if (ObjectUtil.isNotEmpty(value)) {
                res.add(CollUtil.newArrayList(value));
            }
        }
        return res;
    }

    /**
     * 解析表头类中的下拉注解
     * @param head 表头类
     * @param <T> 泛型
     * @return Map<下拉框列索引, 下拉框内容> map
     */
    protected <T> Map<Integer, ExcelSelectedResolve> resolveSelectedAnnotation(Class<T> head) {
        Map<Integer, ExcelSelectedResolve> selectedMap = new HashMap<>(16);

        // getDeclaredFields(): 返回全部声明的属性；getFields(): 返回public类型的属性
        Field[] fields = head.getDeclaredFields();
        for (int i = 0; i < fields.length; i++){
            Field field = fields[i];
            // 解析注解信息
            ExcelSelected selected = field.getAnnotation(ExcelSelected.class);
            ExcelProperty property = field.getAnnotation(ExcelProperty.class);
            if (selected != null) {
                ExcelSelectedResolve excelSelectedResolve = new ExcelSelectedResolve();
                String[] source = resolveSelectedSource(field, selected);
                if (source != null && source.length > 0){
                    excelSelectedResolve.setSource(source);
                    excelSelectedResolve.setInput(selected.input());
                    excelSelectedResolve.setFirstRow(selected.firstRow());
                    excelSelectedResolve.setLastRow(selected.lastRow());
                    if (property != null && property.index() >= 0){
                        selectedMap.put(property.index(), excelSelectedResolve);
                    } else {
                        selectedMap.put(i, excelSelectedResolve);
                    }
                }
            }
        }
        return selectedMap;
    }

    /**
     * 解析选择框内容
     * @param field 字段
     * @param excelSelected 选择框
     * @return String[]
     */
    @SuppressWarnings("unchecked")
    protected String[] resolveSelectedSource(Field field, ExcelSelected excelSelected) {
        if (excelSelected == null) {
            return new String[0];
        }

        // 获取固定下拉框的内容
        String[] sources = excelSelected.source();
        if (sources.length > 0) {
            return sources;
        }

        // 获取动态下拉框的内容
        String[] dynamicSources = new String[0];
        if (ArrayUtil.isNotEmpty(excelSelected.dataType())) {
            dynamicSources = ArrayUtil.addAll(dynamicSources, getSource(excelSelected.dataType()));
        }
        if (ArrayUtil.isEmpty(excelSelected.enumClass())) {
            if (ClassUtil.isAssignable(IEnum.class, field.getType())) {
                dynamicSources = ArrayUtil.addAll(dynamicSources, getEnumSource(new Class[]{field.getType()}));
            }
        } else {
            dynamicSources = ArrayUtil.addAll(dynamicSources, getEnumSource(excelSelected.enumClass()));
        }
        return dynamicSources;
    }

    private String[] getEnumSource(Class<? extends IEnum>[] dataTypes) {
        List<String> list = new ArrayList<>();
        for (Class<? extends IEnum> dataType : dataTypes) {
            final IEnum[] enums = dataType.getEnumConstants();
            if (null == enums) {
                return new String[0];
            }
            for (IEnum e : enums) {
                list.add(e.getDesc());
            }
        }
        return ArrayUtil.toArray(list, String.class);
    }
}