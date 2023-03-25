package com.iv.ersr.common.entity.enums;

/**
 * <p>
 * 登录返回状态码枚举
 * </p>
 *
 * @author IVI04
 * @since 2021-12-24
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
    DISABLE,
    /**
     * 用户不存在
     */
    LOGIN_ACCOUNT_UNDEFINE,
    /**
     * 电话不存在
     */
    LOGIN_PHONE_UNDEFINE
}
