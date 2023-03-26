package com.iv.ersr.common.util;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;

/**
 * <p>
 * 全局缓存对象
 * </p>
 *
 * @author moxiaoyu
 * @since 2021-12-24
 */
public class SystemCacheUtil {

    /**
     * 5分钟 验证码
     */
    public static final TimedCache<String, Object> CODE_CACHE = CacheUtil.newTimedCache(300000);

    /**
     * 24小时 系统配置
     */
    public static final TimedCache<String, Object> DAY_CACHE = CacheUtil.newTimedCache(86400000);

    /**
     * 1分钟
     */
    public static final TimedCache<String, Object> LOGIN_TIMEOUT_CACHE = CacheUtil.newTimedCache(60000);

    /**
     * 1秒
     */
    public static final TimedCache<String, Object> URL_CACHE = CacheUtil.newTimedCache(1000);

    static {
        // 定时清理过期对象
        CODE_CACHE.schedulePrune(30000);
        DAY_CACHE.schedulePrune(3600000);
        LOGIN_TIMEOUT_CACHE.schedulePrune(6000);
        URL_CACHE.schedulePrune(1000);
    }

    private SystemCacheUtil(){}

    public static void putCodeCache(String key, Object val) {
        CODE_CACHE.put(key, val);
    }

    public static Object getCodeCache(String key) {
        return CODE_CACHE.get(key, false);
    }

    public static void removeCodeCache(String key) {
        CODE_CACHE.remove(key);
    }

    public static void putDayCache(String key, Object val) {
        DAY_CACHE.put(key, val);
    }

    public static Object getDayCache(String key) {
        return DAY_CACHE.get(key, false);
    }

    public static void removeDayCache(String key) {
        DAY_CACHE.remove(key);
    }

    public static void putLoginTimeoutCache(String key, Object val) {
        LOGIN_TIMEOUT_CACHE.put(key, val);
    }

    public static Object getLoginTimeoutCache(String key) {
        return LOGIN_TIMEOUT_CACHE.get(key, false);
    }

    public static void removeLoginTimeoutCache(String key) {
        LOGIN_TIMEOUT_CACHE.remove(key);
    }

    public static void putUrlCache(String key, Object val) {
        URL_CACHE.put(key, val);
    }

    public static Object getUrlCache(String key) {
        return URL_CACHE.get(key, false);
    }

    public static void removeUrlCache(String key) {
        URL_CACHE.remove(key);
    }
}
