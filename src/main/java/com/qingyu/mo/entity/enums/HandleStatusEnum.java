package com.qingyu.mo.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.qingyu.mo.entity.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 处理状态 0-未处理 1-处理中 2-处理成功 3-处理失败
 * </p>
 *
 * @author liweimin
 * @since 2021-10-11
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum HandleStatusEnum implements IEnum {

    /**
     * 未处理
     */
    NO(0, "未处理"),

    /**
     * 处理中
     */
    HANDLING(1, "处理中"),

    /**
     * 处理成功
     */
    SUCCESS(2, "处理成功"),

    /**
     * 处理失败
     */
    FAIL(3, "处理失败");

    @EnumValue
    @JsonValue
    private int value;

    private String desc;
}
