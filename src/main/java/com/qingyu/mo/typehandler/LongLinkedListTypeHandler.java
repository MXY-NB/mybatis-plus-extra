package com.qingyu.mo.typehandler;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 数据库数据转换List数组
 * </p>
 *
 * @author IVI04
 * @since 2021-12-24
 */
@MappedJdbcTypes(JdbcType.VARBINARY)
@MappedTypes({List.class})
public class LongLinkedListTypeHandler extends JacksonLinkedListTypeHandler<Long> {

    @Override
    protected TypeReference<LinkedList<Long>> specificType() {
        return new TypeReference<LinkedList<Long>>() {};
    }
}