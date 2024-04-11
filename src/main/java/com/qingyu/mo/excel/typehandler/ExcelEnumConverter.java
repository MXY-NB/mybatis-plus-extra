package com.qingyu.mo.excel.typehandler;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.excel.converters.AutoConverter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.qingyu.mo.entity.IEnum;
import com.qingyu.mo.excel.annotion.ExcelEnumDesc;

import java.lang.reflect.Field;

/**
 * <p>
 * 枚举转换
 * </p>
 *
 * @author IVI04
 * @since 2021-12-24
 */
public class ExcelEnumConverter extends AutoConverter {

    @Override
    public WriteCellData<String> convertToExcelData(Object value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        Field field = contentProperty.getField();
        if (value.getClass().isEnum() && value instanceof Enum) {
            ExcelEnumDesc enumDesc = field.getDeclaredAnnotation(ExcelEnumDesc.class);
            if (enumDesc == null) {
                if (value instanceof IEnum) {
                    IEnum iEnum = (IEnum) value;
                    return new WriteCellData<>(String.valueOf(iEnum.getDesc()));
                }
            } else {
                Enum<?> enumValue = (Enum<?>) value;
                if (enumValue.ordinal() < enumDesc.value().length) {
                    return new WriteCellData<>((String) ArrayUtil.get(enumDesc.value(), enumValue.ordinal()));
                }
            }
        }
        return new WriteCellData<>("");
    }

    @Override
    public Object convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration){
        Class<?> type = contentProperty.getField().getType();
        return likeValueOf(type, cellData.getStringValue());
    }

    public <E extends IEnum> E likeValueOf(Class<?> enumClass, String value) {
        value = value.trim();
        final Field[] fields = ReflectUtil.getFields(enumClass);
        final Enum<?>[] enums = (Enum<?>[]) enumClass.getEnumConstants();
        String fieldName;
        for (Field field : fields) {
            fieldName = field.getName();
            if (field.getType().isEnum() || "ENUM$VALUES".equals(fieldName) || "ordinal".equals(fieldName)) {
                // 跳过一些特殊字段
                continue;
            }
            for (Enum<?> enumObj : enums) {
                if (ObjectUtil.equal(value, ReflectUtil.getFieldValue(enumObj, field))) {
                    return (E) enumObj;
                }
            }
        }
        return null;
    }
}