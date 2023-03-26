package com.iv.ersr.mybatisplus.core.entity;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.*;

import java.util.List;

/**
 * <p>
 * mapper resultMap 一对多写法
 * </p>
 *
 * @author moxiaoyu
 * @since 2023-03-22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectionResultMap {

    /**
     * 字段名
     */
    private String propertyName;

    /**
     * 字段名
     */
    private SFunction<?, ?> property;

    /**
     * 传参映射
     */
    private List<FieldMapping> fieldMappings;

    public static class CollectionResultMapBuilder {
        @Generated
        private String propertyName;
        @Generated
        private SFunction<?, ?> property;
        @Generated
        private List<FieldMapping> fieldMappings;

        @Generated
        public <J> CollectionResultMapBuilder property(final SFunction<J, ?> property) {
            this.property = property;
            return this;
        }

        @Generated
        public CollectionResultMap build() {
            return new CollectionResultMap(this.propertyName, this.property, this.fieldMappings);
        }
    }
}
