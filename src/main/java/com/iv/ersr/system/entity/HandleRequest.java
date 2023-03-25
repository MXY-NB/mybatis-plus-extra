package com.iv.ersr.system.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 自定义Request对象
 * </p>
 *
 * @author IVI04
 * @since 2021-12-24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HandleRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 访问地址
     */
    private String uri;

    /**
     * 操作时间
     */
    private LocalDateTime operateTime;

    /**
     * 请求域名
     */
    private String origin;

    /**
     * 来源ip
     */
    private String sourceIp;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 平台名称
     */
    private String platform;

    /**
     * 操作系统
     */
    private String system;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 用户手机号 + username
     */
    private String userAccount;

    /**
     * 客户端key
     */
    private String key;

    /**
     * 时间戳
     */
    private String timestamp;

    /**
     * 版本
     */
    private String version;

    /**
     * 签名
     */
    private String sign;
}