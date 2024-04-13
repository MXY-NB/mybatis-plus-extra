package com.qingyu.mo.mybatisplus.utils;

import cn.hutool.core.lang.reflect.MethodHandleUtil;
import com.qingyu.mo.mybatisplus.exception.Exceptions;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * <p>
 * Class工具类
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.6.2
 */
public final class ClassUtil {

    private ClassUtil(){}

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<T> clazz, Object... params) {
        try {
            if (params.length == 0) {
                Constructor<T> constructor = clazz.getDeclaredConstructor();
                return constructor.newInstance();
            } else {
                Class<?>[] paramTypes = getClasses(params);
                MethodHandle constructor = MethodHandleUtil.findConstructor(clazz, paramTypes);
                if (constructor == null) {
                    throw new InstantiationException("No constructor matched parameter types.");
                } else {
                    return (T) constructor.invokeWithArguments(params);
                }
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Exceptions.t("实例化对象时出现错误,请尝试给 %s 添加对应的构造方法", e, clazz.getName());
        } catch (Throwable e) {
            Exceptions.t("实例化对象时出现错误", e, clazz.getName());
        }
        return null;
    }

    public static Class<?>[] getClasses(Object... objects) {
        if (objects.length == 0) {
            return new Class[0];
        } else {
            Class<?>[] result = new Class[objects.length];
            for(int i = 0; i < objects.length; ++i) {
                if (objects[i] != null) {
                    result[i] = objects[i].getClass();
                }
            }
            return result;
        }
    }
}
