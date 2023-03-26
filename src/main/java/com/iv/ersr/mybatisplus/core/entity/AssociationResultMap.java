package com.iv.ersr.mybatisplus.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * mapper resultMap 一对一写法
 * </p>
 *
 * @author moxiaoyu
 * @since 2023-03-22
 */
@Data
@Builder
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
