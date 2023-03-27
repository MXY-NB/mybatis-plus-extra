package com.iv.ersr.game.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.iv.ersr.common.entity.CommonTimeEntity;
import com.iv.ersr.game.entity.enums.PlayModeEnum;
import com.iv.ersr.game.typehandler.PlayModeEnumListTypeHandler;
import lombok.*;

import java.util.List;

/**
 * <p>
 * 游戏详细信息
 * </p>
 *
 * @author MXY
 * @since 2021-12-24
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "game_rental_detail", autoResultMap = true)
public class GameRentalDetail extends CommonTimeEntity {

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
     * 游玩模式 多选
     */
    @TableField(typeHandler = PlayModeEnumListTypeHandler.class)
    private List<PlayModeEnum> playModes;
}
