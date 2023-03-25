package com.iv.ersr.system.service;

import com.iv.ersr.system.entity.SystemSetting;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统配置 服务类
 * </p>
 *
 * @author moxiaoyu
 * @since 2022-05-18
 */
public interface ISystemSettingService extends IService<SystemSetting> {

    /**
     * 获取配置表信息
     * @author ZC
     * @date 2021/10/12
     * @param key 配置名称
     * @param cache 是否放入缓存
     * @return SystemSetting
     */
    SystemSetting getSettingByKey(String key, boolean cache);
}
