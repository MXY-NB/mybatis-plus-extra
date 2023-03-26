package com.iv.ersr.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.iv.ersr.common.annotaion.Min;
import com.iv.ersr.common.annotaion.NotNull;
import com.iv.ersr.common.entity.BaseEntity;
import com.iv.ersr.common.exception.HandlerException;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工具类
 * </p>
 *
 * @author moxiaoyu
 * @since 2021-12-24
 */
@Slf4j
public class MyUtil {

    private MyUtil(){}

    public static <T extends BaseEntity> boolean isEmpty(T t) {
        return t == null || t.getId() == null;
    }

    public static <T extends BaseEntity> boolean isNotEmpty(T t) {
        return !isEmpty(t);
    }

    public static <T> T nullToDefault(T v, T defaultV) {
        return v == null ? defaultV : v;
    }

    public static <T> T doIfTrue(boolean codition, T trueValue, T falseValue) {
        return codition ? trueValue : falseValue;
    }

    public static <O> void checkOrderData(List<O> orderDetails, Class<O> beanClass) {
        checkData(orderDetails, beanClass, false);
    }

    public static <O> void checkOrderAuditData(List<O> orderDetails, Class<O> beanClass) {
        checkData(orderDetails, beanClass, true);
    }

    public static <O> void checkData(List<O> orderDetails, Class<O> beanClass, boolean isAudit) {
        Assert.notNull(beanClass);
        Field[] fields = ReflectUtil.getFields(beanClass);
        List<String> fieldNames = new ArrayList<>();
        Map<String, NotNull> notNullList = new HashMap<>(16);
        Map<String, Min> minList = new HashMap<>(16);
        for (Field field : fields) {
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                if (annotation.annotationType().equals(NotNull.class)) {
                    NotNull notNull = (NotNull) annotation;
                    if (notNull.isAudit() == isAudit) {
                        notNullList.put(field.getName(), notNull);
                        fieldNames.add(field.getName());
                    }
                }
                if (annotation.annotationType().equals(Min.class) && field.getDeclaringClass().equals(BigDecimal.class)) {
                    Min min = (Min) annotation;
                    if (min.isAudit() == isAudit) {
                        minList.put(field.getName(), min);
                        fieldNames.add(field.getName());
                    }
                }
            }
        }
        if (CollUtil.isNotEmpty(fieldNames)) {
            for (O orderDetail : orderDetails) {
                Map<String, Object> fieldMap = new HashMap<>(16);
                for (String fieldName : fieldNames) {
                    Object fieldValue = ReflectUtil.getFieldValue(orderDetail, fieldName);
                    fieldMap.put(fieldName, fieldValue);
                }
                notNullList.forEach((fieldName, notNull)->{
                    if (fieldMap.containsKey(fieldName) && ObjectUtil.isEmpty(fieldMap.get(fieldName))) {
                        throw new HandlerException(notNull.value());
                    }
                });
                minList.forEach((fieldName, min)->{
                    if (fieldMap.containsKey(fieldName)) {
                        if (min.isEquals()) {
                            if (ObjectUtil.isNotEmpty(fieldMap.get(fieldName)) && NumberUtil.isLess((BigDecimal) fieldMap.get(fieldName), BigDecimal.valueOf(min.value()))) {
                                throw new HandlerException(min.message());
                            }
                        } else {
                            if (ObjectUtil.isNotEmpty(fieldMap.get(fieldName)) && NumberUtil.isLessOrEqual((BigDecimal) fieldMap.get(fieldName), BigDecimal.valueOf(min.value()))) {
                                throw new HandlerException(min.message());
                            }
                        }

                    }
                });
            }
        }
    }
}