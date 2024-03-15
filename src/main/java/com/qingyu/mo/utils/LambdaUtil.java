package com.qingyu.mo.utils;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.qingyu.mo.exception.Exceptions;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

/**
 * <p>
 * Lambdas工具类
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.6.2
 */
public final class LambdaUtil {

    private static final int ID = 1;

    private LambdaUtil(){}

    /**
     * 获取一个sfunction的字段名称
     * @param sFunction 表达式
     * @param <T> 实体类型
     * @return String
     */
    public static <T> String toUnderlinePropertyName(SFunction<T, ?> sFunction) {
        return CharSequenceUtil.toUnderlineCase(toPropertyName(sFunction));
    }

    /**
     * 获取一个sfunction的字段名称
     * @param sFunction 表达式
     * @param <T> 实体类型
     * @return String
     */
    public static <T> String toPropertyName(SFunction<T, ?> sFunction) {
        return PropertyNamer.methodToProperty(LambdaUtils.extract(sFunction).getImplMethodName());
    }

    /**
     * 获取一个字段的sfunction
     * @param model 实体类
     * @param fieldName 字段名称
     * @param <T> 实体类型
     * @return 字段get方法的的sfunction
     */
    @SuppressWarnings("unchecked")
    public static <T> SFunction<T, ?> getSFunction(Class<T> model, String fieldName) {
        final MethodHandles.Lookup lookup = MethodHandles.lookup();
        Field field = ReflectUtil.getField(model, fieldName);
        if (field == null) {
            return null;
        }
        TableField tableField = field.getDeclaredAnnotation(TableField.class);
        if (tableField != null && !tableField.exist()) {
            return null;
        }
        MethodType methodType = MethodType.methodType(field.getType(), model);
        final CallSite site;
        SFunction<T, ?> func = null;
        try {
            //方法名叫做:getSecretLevel  转换为 SFunction function interface对象
            site = LambdaMetafactory.altMetafactory(lookup,
                                                    "invoke",
                                                    MethodType.methodType(SFunction.class),
                                                    methodType,
                                                    lookup.findVirtual(model, "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1),
                                                                       MethodType.methodType(field.getType())),
                                                    methodType, ID);
            func = (SFunction<T, ?>) site.getTarget().invokeExact();
        } catch (Throwable e) {
            Exceptions.t(e);
        }
        return func;
    }

}
