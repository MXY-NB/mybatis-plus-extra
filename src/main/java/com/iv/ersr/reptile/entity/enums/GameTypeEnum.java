package com.iv.ersr.reptile.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author MXY
 * @Description 游戏类型 枚举
 * @date 2022/2/15 9:45
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum GameTypeEnum {
    /**
     * 0-Switch
     */
    SWITCH(0),
    /**
     * 1-Steam
     */
    STEAM(1),
    /**
     * 2-PS4
     */
    PS4(2),
    /**
     * 3-PS5
     */
    PS5(3),
    /**
     * 4-Xbox
     */
    XBOX(4);

    @JsonValue
    @EnumValue
    private int value;
}
