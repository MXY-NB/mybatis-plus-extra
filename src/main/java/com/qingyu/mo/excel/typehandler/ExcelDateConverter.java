package com.qingyu.mo.excel.typehandler;

import com.alibaba.excel.converters.AutoConverter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 * 导出日期处理器
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
public class ExcelDateConverter extends AutoConverter {

    @Override
    public WriteCellData<?> convertToExcelData(Object value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if(value instanceof LocalDate){
            LocalDate localDate = (LocalDate) value;
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String tmp = dateTimeFormatter.format(localDate);
            return new WriteCellData<>(tmp);
        }else if(value instanceof LocalDateTime){
            LocalDateTime localDateTime = (LocalDateTime) value;
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String tmp = dateTimeFormatter.format(localDateTime);
            return new WriteCellData<>(tmp);
        }else if(value instanceof LocalTime){
            LocalTime localTime = (LocalTime) value;
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String tmp = dateTimeFormatter.format(localTime);
            return new WriteCellData<>(tmp);
        }
        return new WriteCellData<>("");
    }
}
