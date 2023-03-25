package com.iv.ersr.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 分布式系统全局唯一id配置
 * </p>
 *
 * @author moxiaoyu
 * @since 2022-04-11
 */
@Data
@Component
@ConfigurationProperties(prefix="idgenerator")
public class SnowflakeConfig {

    /**
     * 终端ID
     */
    private long machineId;

    /**
     * 数据中心ID
     */
    private long datacenterId;
}