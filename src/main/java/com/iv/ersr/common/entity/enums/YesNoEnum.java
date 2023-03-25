package com.iv.ersr.common.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Description: 通用枚举 表示 是和否 开启/关闭  启用/禁用 等等
 * @Author: liweimin
 * @Date: 2020/10/12
 * @Time: 11:50
**/
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum YesNoEnum {

    /**
     * 否
     */
    NO(0, "否", "禁用"),

    /**
     * 是
     */
    YES(1, "是", "启用");

    @JsonValue
    @EnumValue
    private int value;

    private String desc;
    private String desc2;
}
