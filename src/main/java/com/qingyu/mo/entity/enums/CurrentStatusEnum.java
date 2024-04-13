package com.qingyu.mo.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.qingyu.mo.entity.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 通用状态 0-编辑中 1-审核中 2-已通过 3-已拒绝
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum CurrentStatusEnum implements IEnum {

    /**
     * 编辑中 草稿
     */
    EDITING(0, "编辑中"),

    /**
     * 审核中
     */
    UNDER_REVIEW(1, "审核中"),

    /**
     * 已完成
     */
    FINISHED(2, "已完成"),

    /**
     * 已拒绝
     */
    REJECTED(3, "已拒绝");

    @EnumValue
    @JsonValue
    private int value;

    private String desc;
}
