package com.qingyu.mo.typehandler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 数据库数据转换List数组
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
public abstract class FastjsonListTypeHandler<T> extends AbstractJsonTypeHandler<List<T>> {

    @Override
    protected List<T> parse(String json) {
        return this.getListByJsonArrayString(json);
    }

    @Override
    protected String toJson(List<T> obj) {
        return CollUtil.isEmpty(obj) ? null : JSON.toJSONString(obj);
    }

    private List<T> getListByJsonArrayString(String content) {
        return CharSequenceUtil.isBlank(content) ? new ArrayList<>() : JSON.parseObject(content, this.specificType());
    }

    /**
     * 具体类型，由子类提供
     * @return 具体类型
     */
    protected abstract TypeReference<List<T>> specificType();
}