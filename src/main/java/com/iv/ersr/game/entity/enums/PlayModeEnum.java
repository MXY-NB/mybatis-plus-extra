package com.iv.ersr.game.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 游玩模式
 * </p>
 *
 * @author moxiaoyu
 * @since 2021-12-24
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum PlayModeEnum {

    /**
     * 电视模式
     */
    TV(0, "电视模式"),

    /**
     * 桌面模式
     */
    DESKTOP(1, "桌面模式"),

    /**
     * 手持模式
     */
    HAND(2, "手持模式");

    @JsonValue
    @EnumValue
    private int value;

    private String desc;
}
