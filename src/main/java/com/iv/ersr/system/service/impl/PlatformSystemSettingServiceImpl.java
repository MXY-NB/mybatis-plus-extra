package com.iv.ersr.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iv.ersr.common.util.SystemCacheUtil;
import com.iv.ersr.system.entity.SystemSetting;
import com.iv.ersr.system.mapper.SystemSettingMapper;
import com.iv.ersr.system.service.ISystemSettingService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统配置 服务实现类
 * </p>
 *
 * @author moxiaoyu
 * @since 2022-05-18
 */
@Service
public class PlatformSystemSettingServiceImpl extends ServiceImpl<SystemSettingMapper, SystemSetting> implements ISystemSettingService {

    /**
     * 获取配置表信息
     * @author ZC
     * @date 2021/10/12
     * @param key 配置名称
     * @param cache 是否放入缓存
     * @return SystemSetting
     */
    @Override
    public SystemSetting getSettingByKey(String key, boolean cache) {
        SystemSetting setting;
        if (cache) {
            setting = (SystemSetting) SystemCacheUtil.getDayCache(key);
            if (setting == null) {
                setting = getOne(Wrappers.<SystemSetting>lambdaQuery().eq(SystemSetting::getSettingName, key));
                // 缓存
                SystemCacheUtil.putDayCache(key, setting);
            }
        } else {
            setting = getOne(Wrappers.<SystemSetting>lambdaQuery().eq(SystemSetting::getSettingName, key));
        }
        return setting;
    }
}
