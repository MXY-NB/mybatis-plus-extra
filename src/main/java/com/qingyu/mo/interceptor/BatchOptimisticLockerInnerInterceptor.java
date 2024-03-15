package com.qingyu.mo.interceptor;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.qingyu.mo.constant.ConstantPlus;
import com.qingyu.mo.exception.Exceptions;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 乐观锁扩展
 * 支持批量更新
 * @author qingyu-mo
 * @since 2024-01-26
 */
@SuppressWarnings("unchecked")
public class BatchOptimisticLockerInnerInterceptor extends OptimisticLockerInnerInterceptor {
    private RuntimeException exception;

    @Override
    public void setException(RuntimeException exception) {
        this.exception = exception;
    }

    @Override
    public void beforeUpdate(Executor executor, MappedStatement ms, Object parameter) {
        if (SqlCommandType.UPDATE != ms.getSqlCommandType()) {
            return;
        }
        if (parameter instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) parameter;
            doBatchOptimisticLocker(map);
        }
    }

    protected void doBatchOptimisticLocker(Map<String, Object> map) {
        Object obj = map.getOrDefault(Constants.LIST, new ArrayList<>());
        if (Objects.nonNull(obj)) {
            List<?> coll = (List<?>)obj;
            TableFieldInfo fieldInfo = null;
            List<Object> originalVersions = new ArrayList<>();
            for (Object et : coll) {
                // version field
                if (null == fieldInfo) {
                    fieldInfo = this.getVersionFieldInfo(et.getClass());
                    if (null == fieldInfo) {
                        return;
                    }
                }

                try {
                    Field versionField = fieldInfo.getField();
                    // 旧的 version 值
                    Object originalVersion = versionField.get(et);
                    if (originalVersion == null) {
                        if (null != exception) {
                            /**
                             * 自定义异常处理
                             */
                            throw exception;
                        }
                        return;
                    }
                    originalVersions.add(originalVersion);
                    // 新的 version 值
                    ReflectUtil.setFieldValue(et, versionField, getUpdatedVersionVal(fieldInfo.getPropertyType(), originalVersion));
                } catch (IllegalAccessException e) {
                    Exceptions.t(e);
                }
            }
            map.put(ConstantPlus.MPE_OPTLOCK_VERSION_ORIGINAL_COLL, originalVersions);
        }
    }
}