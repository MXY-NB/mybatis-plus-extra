package com.qingyu.mo.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.qingyu.mo.entity.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 通用0和1
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum YesNoEnum implements IEnum {

    /**
     * 否
     */
    NO(0, "否"),

    /**
     * 是
     */
    YES(1, "是");

    @JsonValue
    @EnumValue
    private int value;

    private String desc;
}
