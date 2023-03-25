package com.iv.ersr.reptile.entity;

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
@NoArgsConstructor
@AllArgsConstructor
public class GameOtherAttribute {

    /**
     * 是否有实体卡
     */
    private Integer entityCardFlag;

    /**
     * 游戏通关时间
     */
    private BigDecimal customsClearanceTime;
}
