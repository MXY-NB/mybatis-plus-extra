package com.iv.ersr.game.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 联机模式
 * </p>
 *
 * @author MXY
 * @since 2021-12-24
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum OnlineModeEnum {

    /**
     * 本地面联
     */
    LOCAL(0),

    /**
     * 线上联机(需会员)
     */
    ONLINE(1);

    @JsonValue
    @EnumValue
    private int value;
}
