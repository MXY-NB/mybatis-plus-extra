package com.iv.ersr.reptile.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.iv.ersr.common.entity.BaseEntity;
import com.iv.ersr.common.entity.enums.YesNoEnum;
import com.iv.ersr.reptile.entity.enums.OnlineModeEnum;
import com.iv.ersr.reptile.entity.enums.PlayModeEnum;
import com.iv.ersr.reptile.typehandler.OnlineModeEnumListTypeHandler;
import com.iv.ersr.reptile.typehandler.PlayModeEnumListTypeHandler;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 游戏详细信息
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
@TableName(autoResultMap = true)
public class GameRentalDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 游戏id
     */
    private Long gameId;

    /**
     * 游戏大小(单位：GB)
     */
    private String gameSize;

    /**
     * 玩家人数如  1-4
     */
    private String players;

    /**
     * 游戏其他属性
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private GameOtherAttribute otherAttribute;

    /**
     * 是否史低 0-否 1-是
     */
    private YesNoEnum lowestPrice;

    /**
     * 游玩模式 多选
     */
    @TableField(typeHandler = PlayModeEnumListTypeHandler.class)
    private List<PlayModeEnum> playModes;

    /**
     * 是否拥有试玩 0-否 1-是
     */
    private YesNoEnum isHaveDemo;

    /**
     * 联机模式 多选
     */
    @TableField(typeHandler = OnlineModeEnumListTypeHandler.class)
    private List<OnlineModeEnum> onlineModes;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime modTime;
}
