package com.iv.ersr.mybatisplus.core.entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * mapper resultMap 传参映射
 * </p>
 *
 * @author moxiaoyu
 * @since 2023-03-22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldMapping {

    /**
     * 字段名
     */
    private String column;

    /**
     * 属性名
     */
    private String paramName;
}
