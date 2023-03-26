package com.iv.ersr.game.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.iv.ersr.common.entity.CommonEntity;
import com.iv.ersr.common.typehandler.StringListTypeHandler;
import com.iv.ersr.game.entity.enums.PlayModeEnum;
import com.iv.ersr.game.typehandler.PlayModeEnumListTypeHandler;
import lombok.*;

import java.util.List;

/**
* <p>
* 游戏租赁信息
* </p>
*
* @author moxiaoyu
* @since 2022-05-12
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "game_rental_info", autoResultMap = true)
public class GameRentalInfo extends CommonEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 游戏中文名
     */
    private String chineseName;

    /**
     * 游戏官方名
     */
    private String officialName;

    /**
     * 游戏主题类型 如 动作,冒险,策略等,由配置表维护标签名称
     */
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> gameTheme;

    /**
     * 游玩模式 多选
     */
    @TableField(exist = false, typeHandler = PlayModeEnumListTypeHandler.class)
    private List<PlayModeEnum> playModes;

    /**
     * 游玩模式 多选
     */
    @TableField(exist = false)
    private List<GameRentalDetail> gameRentalDetails;
}