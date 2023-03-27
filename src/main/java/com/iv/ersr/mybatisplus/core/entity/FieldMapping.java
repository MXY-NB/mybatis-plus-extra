package com.iv.ersr.mybatisplus.core.entity;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.*;

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
    private SFunction<?, ?> column;

    /**
     * 字段名
     */
    private String columnName;

    /**
     * 参数名
     */
    private SFunction<?, ?> param;

    /**
     * 参数名
     */
    private String paramName;

    public static class FieldMappingBuilder {
        @Generated
        public <J> FieldMapping.FieldMappingBuilder column(final SFunction<J, ?> column) {
            this.column = column;
            return this;
        }

        @Generated
        public <J> FieldMapping.FieldMappingBuilder param(final SFunction<J, ?> param) {
            this.param = param;
            return this;
        }
    }
}
