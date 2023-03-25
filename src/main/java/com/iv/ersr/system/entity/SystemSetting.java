package com.iv.ersr.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.iv.ersr.common.entity.BaseEntity;
import lombok.*;

import java.time.LocalDateTime;

/**
* <p>
* 系统配置
* </p>
*
* @author moxiaoyu
* @since 2022-05-18
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "platform_system_setting", autoResultMap = true)
public class SystemSetting extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    private String settingName;

    /**
     * 配置内容
     */
    private String settingValue;

    /**
     * 修改时间
     */
    private LocalDateTime modTime;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 配置开关 0-关闭 1-开启
     */
    private Boolean switchStatus;

    /**
     * 0-系统配置 1-业务配置
     */
    private Boolean businessType;

    /**
     * 配置简述
     */
    private String settingTitle;

    /**
     * 配置详细
     */
    private String settingDesc;
}