package com.iv.ersr.common.util;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.iv.ersr.common.config.SnowflakeConfig;

import java.util.Locale;

/**
 * <p>
 * 随机id,code生成器
 * </p>
 *
 * @author moxiaoyu
 * @since 2021-12-24
 */
public class Sequence {

    private static final SnowflakeConfig SNOWFLAKE_CONFIG;

    static {
        SNOWFLAKE_CONFIG = ApplicationUtil.getBean(SnowflakeConfig.class);
    }

    private Sequence(){}

    public static Long generateSequenceId(){
        return IdUtil.getSnowflake(SNOWFLAKE_CONFIG.getMachineId(),SNOWFLAKE_CONFIG.getDatacenterId()).nextId();
    }

    public static String generateSequenceIdStr(){
        // 改为分布式唯一
        return generateSequenceId().toString();
    }

    public static String createCodeNum(){
        return RandomUtil.randomString(12).toUpperCase(Locale.ROOT);
    }

}
