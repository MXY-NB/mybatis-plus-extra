package com.iv.ersr.mybatisplus.core.entity;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.iv.ersr.mybatisplus.core.conditions.query.JoinLambdaQueryWrapper;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
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
     * 子查询
     */
    private JoinLambdaQueryWrapper<?> wrapper;

    /**
     * 子查询的id
     */
    private String id;

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
        /**
         * 字段名
         */
        private FieldMapping.FieldMappingBuilder fieldMappingBuilder = FieldMapping.builder();

        @Generated
        public <J> CollectionResultMapBuilder property(final SFunction<J, ?> property) {
            this.property = property;
            return this;
        }

        @Generated
        public CollectionResultMapBuilder fieldMappings(FieldMapping... fieldMappings) {
            this.fieldMappings = Arrays.asList(fieldMappings);
            return this;
        }

        @Generated
        public <J> CollectionResultMapBuilder column(final SFunction<J, ?> column) {
            this.fieldMappingBuilder.column(column);
            return this;
        }

        @Generated
        public CollectionResultMapBuilder columnName(final String columnName) {
            this.fieldMappingBuilder.columnName(columnName);
            return this;
        }

        @Generated
        public <J> CollectionResultMapBuilder param(final SFunction<J, ?> param) {
            this.fieldMappingBuilder.param(param);
            return this;
        }

        @Generated
        public CollectionResultMapBuilder paramName(final String paramName) {
            this.fieldMappingBuilder.paramName(paramName);
            return this;
        }

        @Generated
        public CollectionResultMapBuilder end() {
            if (this.fieldMappings == null) {
                this.fieldMappings = new ArrayList<>();
            }
            this.fieldMappings.add(this.fieldMappingBuilder.build());
            this.fieldMappingBuilder = FieldMapping.builder();
            return this;
        }
    }
}
