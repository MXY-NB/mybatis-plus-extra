package com.iv.ersr.game.entity;

import com.iv.ersr.common.entity.enums.YesNoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author MXY
 * @Description 游戏其它属性
 * @date 2022/3/3 14:09
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameOtherAttribute {

    /**
     * 是否有实体卡
     */
    private YesNoEnum entityCardFlag;

    /**
     * 游戏通关时间
     */
    private BigDecimal customsClearanceTime;
}
