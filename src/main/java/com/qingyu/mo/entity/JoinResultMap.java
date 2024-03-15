package com.qingyu.mo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.ibatis.type.TypeHandler;

/**
 * <p>
 * mapper resultMap 一对多写法
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.6.2
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class JoinResultMap {

    /**
     * 属性名
     */
    private String property;

    /**
     * 字段名
     */
    private String column;

    /**
     * 处理
     */
    private TypeHandler<?> typeHandler;
}
