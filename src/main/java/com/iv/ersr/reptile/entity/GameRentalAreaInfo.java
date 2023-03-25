package com.iv.ersr.reptile.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.iv.ersr.common.entity.BaseEntity;
import com.iv.ersr.common.entity.enums.YesNoEnum;
import com.iv.ersr.common.typehandler.StringListTypeHandler;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 游戏地区信息
 * </p>
 *
 * @author IVI04
 * @since 2021-12-24
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "game_rental_area_info", autoResultMap = true)
public class GameRentalAreaInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 游戏id
     */
    private Long gameId;

    /**
     * 游戏本地名称
     */
    private String gameLocalName;

    /**
     * 发布地区,如中国香港,巴西。地区配置由配置表维护
     */
    private String releaseArea;

    /**
     * 语言,如中文,日文,英文。语言配置由配置表维护
     */
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> languages;

    /**
     * 是否支持中文 0-否 1-是
     */
    private YesNoEnum isSupportChinese;

    /**
     * 原价
     */
    private BigDecimal price;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime modTime;
}
