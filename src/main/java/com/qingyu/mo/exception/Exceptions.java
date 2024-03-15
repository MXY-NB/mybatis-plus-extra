package com.qingyu.mo.exception;

/**
 * <p>
 * 自定义异常工具类
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.6.2
 */
public final class Exceptions {

    private Exceptions() {}

    /**
     * 返回一个新的异常，统一构建，方便统一处理
     * @param t 异常信息
     */
    public static void t(Throwable t) {
        throw new MybatisPlusJoinException(t);
    }

    /**
     * 返回一个新的异常，统一构建，方便统一处理
     * @param msg 消息
     * @param t 异常信息
     * @param params 参数
     */
    public static void t(String msg, Throwable t, Object... params) {
        throw new MybatisPlusJoinException(String.format(msg, params), t);
    }

    /**
     * 重载的方法
     * @param msg 消息
     * @param params 参数
     */
    public static void t(String msg, Object... params) {
        throw new MybatisPlusJoinException(String.format(msg, params));
    }

    /**
     * 重载的方法
     * @param msg 消息
     */
    public static void t(boolean flag, String msg, Object... params) {
        if (flag) {
            throw new MybatisPlusJoinException(String.format(msg, params));
        }
    }

    /**
     * 重载的方法
     * @param msg 消息
     */
    public static void t(boolean flag, String msg) {
        if (flag) {
            throw new MybatisPlusJoinException(msg);
        }
    }
}
