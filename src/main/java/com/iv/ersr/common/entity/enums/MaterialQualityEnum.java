package com.iv.ersr.common.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author MXY
 * @Description 物料状态 枚举
 * @date 2021/12/21 9:07
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum MaterialQualityEnum {

    /**
     * 次品
     */
    DEFECTIVE_MATERIAL(0, "次品", 3),

    /**
     * 良品
     */
    GOOD_MATERIAL(1, "良品", 4),

    /**
     * 报废品
     */
    SCRAP_MATERIAL(2, "报废品", 5),

    /**
     * 次品锁定物料
     */
    DEFECTIVE_LOCK_MATERIAL(3, "次品锁定物料", null),

    /**
     * 良品锁定物料
     */
    GOOD_LOCK_MATERIAL(4, "良品锁定物料", null),

    /**
     * 报废品锁定物料
     */
    SCRAP_LOCK_MATERIAL(5, "报废品锁定物料", null);

    @EnumValue
    @JsonValue
    private int value;

    private String desc;

    private Integer localValue;
}
