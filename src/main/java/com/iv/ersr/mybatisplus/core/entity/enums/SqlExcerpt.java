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
    LEFT_JOIN("LEFT JOIN %s %s ON %s = %s", "左联SQL"),
    /**
     * 右联SQL
     */
    RIGHT_JOIN("RIGHT JOIN %s %s ON %s = %s", "右联SQL"),
    /**
     * 内联SQL
     */
    INNER_JOIN("INNER JOIN %s %s ON %s.%s = %s.%s", "内联SQL"),
    /**
     * AS
     */
    COLUMNS_AS("%s AS %s", "AS"),
    /**
     * AS
     */
    TABLE_AS(" %s AS %s ", "AS"),
    /**
     * AND
     */
    AND(" AND %s = %s ", "AND");


    private String sql;

    private String desc;

}
