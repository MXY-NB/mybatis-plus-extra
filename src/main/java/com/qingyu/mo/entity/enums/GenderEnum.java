package com.qingyu.mo.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.qingyu.mo.entity.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 性别
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum GenderEnum implements IEnum {

    /**
     * 女
     */
    FEMALE(0, "女"),
    /**
     * 男
     */
    MALE(1, "男");

    @EnumValue
    @JsonValue
    private int value;

    private String desc;
}
