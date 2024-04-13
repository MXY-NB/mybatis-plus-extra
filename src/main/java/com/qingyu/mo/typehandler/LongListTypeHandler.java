package com.qingyu.mo.typehandler;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.List;

/**
 * <p>
 * 数据库数据转换List数组
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
@MappedJdbcTypes(JdbcType.VARBINARY)
@MappedTypes({List.class})
public class LongListTypeHandler extends JacksonListTypeHandler<Long> {

    @Override
    protected TypeReference<List<Long>> specificType() {
        return new TypeReference<List<Long>>() {};
    }
}