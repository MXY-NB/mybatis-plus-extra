package com.iv.ersr.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 腾讯COS配置
 * </p>
 *
 * @author IVI04
 * @since 2022-03-28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TencentCosKey implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * appId 用户维度唯一资源标识
     */
    private String appId;

    /**
     * secretId 身份识别 ID
     */
    private String secretId;

    /**
     * secretKey 身份密钥
     */
    private String secretKey;

    /**
     * region 地域信息
     */
    private String region;

    /**
     * bucketName 存储桶名称 命名规则：BucketName-APPID
     */
    private String bucketName;

    /**
     * baseUrl 默认域名
     */
    private String baseUrl;
}
