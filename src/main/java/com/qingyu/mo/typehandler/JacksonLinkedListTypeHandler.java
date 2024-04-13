package com.qingyu.mo.typehandler;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qingyu.mo.exception.HandlerException;

import java.io.IOException;
import java.util.LinkedList;

/**
 * <p>
 * 数据库数据转换LinkedList数组
 * </p>
 *
 * @author IVI04
 * @since 2021-12-24
 */
public abstract class JacksonLinkedListTypeHandler<T> extends AbstractJsonTypeHandler<LinkedList<T>> {

    private static ObjectMapper objectMapper;

    @Override
    protected LinkedList<T> parse(String json) {
        try {
            return getObjectMapper().readValue(json, specificType());
        } catch (IOException e) {
            throw new HandlerException(e.getMessage());
        }
    }

    @Override
    protected String toJson(LinkedList<T> obj) {
        try {
            return getObjectMapper().writeValueAsString(obj);
        } catch (IOException e) {
            throw new HandlerException(e.getMessage());
        }
    }

    public static ObjectMapper getObjectMapper() {
        if (null == objectMapper) {
            objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        return objectMapper;
    }

    /**
     * 具体类型，由子类提供
     *
     * @return 具体类型
     */
    protected abstract TypeReference<LinkedList<T>> specificType();
}