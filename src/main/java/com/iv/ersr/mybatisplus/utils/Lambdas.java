package com.iv.ersr.mybatisplus.utils;

import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import icu.mhb.mybatisplus.plugln.exception.Exceptions;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * <p>
 * Lambdas工具类
 * </p>
 *
 * @author moxiaoyu
 * @since 2023-03-26
 */
public class Lambdas {

    /**
     * 可序列化
     */
    private static final int FLAG_SERIALIZABLE = 1;

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
     * @param fieldType 字段类型
     * @param fieldName 字段名称
     * @param <T> 实体类型
     * @return 字段get方法的的sfunction
     */
    public static <T> SFunction<T, ?> getSFunction(Class<T> model, Class<?> fieldType, String fieldName) {
        final MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType methodType = MethodType.methodType(fieldType, model);
        final CallSite site;
        SFunction<T, ?> func = null;
        try {
            //方法名叫做:getSecretLevel  转换为 SFunction function interface对象
            site = LambdaMetafactory.altMetafactory(lookup,
                                                    "invoke",
                                                    MethodType.methodType(SFunction.class),
                                                    methodType,
                                                    lookup.findVirtual(model, "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1),
                                                                       MethodType.methodType(fieldType)),
                                                    methodType, FLAG_SERIALIZABLE);
            func = (SFunction<T, ?>) site.getTarget().invokeExact();
        } catch (Throwable e) {
            throw Exceptions.mpje(e);
        }
        return func;
    }

}
