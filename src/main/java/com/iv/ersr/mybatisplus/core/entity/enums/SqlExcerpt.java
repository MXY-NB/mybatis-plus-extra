package com.iv.ersr.mybatisplus.core.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum SqlExcerpt {

    /**
     * 左联SQL
     */
    LEFT_JOIN("LEFT JOIN %s %s ON %s = %s"),

    /**
     * 右联SQL
     */
    RIGHT_JOIN("RIGHT JOIN %s %s ON %s = %s"),

    /**
     * 内联SQL
     */
    INNER_JOIN("INNER JOIN %s %s ON %s.%s = %s.%s"),

    /**
     * AS
     */
    TABLE_AS("%s AS %s");

    private String sql;
}
