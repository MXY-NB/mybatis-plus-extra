package com.iv.ersr.mybatisplus.exception;

/**
 * <p>
 * 自定义异常工具类
 * </p>
 *
 * @author moxiaoyu
 * @since 2023-03-26
 */
public final class Exceptions {

    private Exceptions() {
    }

    /**
     * 返回一个新的异常，统一构建，方便统一处理
     *
     * @param msg 消息
     * @param t   异常信息
     * @return 返回异常
     */
    public static MybatisPlusJoinException t(String msg, Throwable t, Object... params) {
        return new MybatisPlusJoinException(String.format(msg, params), t);
    }

    /**
     * 重载的方法
     *
     * @param msg 消息
     * @return 返回异常
     */
    public static MybatisPlusJoinException t(String msg, Object... params) {
        return new MybatisPlusJoinException(String.format(msg, params));
    }
}
