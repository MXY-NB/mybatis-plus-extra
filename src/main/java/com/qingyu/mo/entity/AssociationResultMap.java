package com.qingyu.mo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * mapper resultMap 一对一写法
 * </p>
 *
 * @author qingyu-mo
 * @since 2023-12-19
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AssociationResultMap {

    /**
     * 字段名
     */
    private String field;

    /**
     * 所属类
     */
    private Class<?> clz;
}
