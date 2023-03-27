package com.iv.ersr.game.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.iv.ersr.common.entity.CommonEntity;
import com.iv.ersr.common.entity.enums.YesNoEnum;
import com.iv.ersr.common.typehandler.StringListTypeHandler;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 游戏租赁信息
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
@TableName(autoResultMap = true)
public class GameRentalInfo extends CommonEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 游戏中文名
     */
    @NotBlank(message = "游戏中文名不能为空！")
    private String chineseName;

    /**
     * 游戏官方名
     */
    @NotBlank(message = "游戏官方名不能为空！")
    private String officialName;

    /**
     * 游戏主题类型 如 动作,冒险,策略等,由配置表维护标签名称
     */
    @NotNull(message = "游戏主题类型不能为空！")
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> gameTheme;

    /**
     * 是否已删除 0-否 1-是
     */
    @TableLogic
    private YesNoEnum deleted;

    /**
     * 游戏详情信息
     */
    @TableField(exist = false)
    private GameRentalDetail gameRentalDetail;

    /**
     * 游戏详情信息
     */
    @TableField(exist = false)
    private List<GameRentalDetail> gameRentalDetails;
}
