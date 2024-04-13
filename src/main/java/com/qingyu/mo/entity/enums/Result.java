package com.qingyu.mo.entity.enums;

/**
 * <p>
 * 登录返回状态码枚举
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
public enum Result {
    /**
     * 失败
     */
    FAIL,
    /**
     * 成功
     */
    SUCCESS,
    /**
     * 未授权
     */
    UNAUTHORIZED,
    /**
     * 登陆超时
     */
    LOGIN_TIMEOUT,
    /**
     * 授权码超时
     */
    AUTH_CODE_TIMEOUT,
    /**
     * 禁用
     */
    DISABLE
}
