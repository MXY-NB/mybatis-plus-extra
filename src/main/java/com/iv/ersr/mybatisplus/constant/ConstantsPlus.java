package com.iv.ersr.mybatisplus.constant;

import com.baomidou.mybatisplus.core.toolkit.Constants;

/**
 * 常用常量
 *
 * @author moxiaoyu
 * @since 2023-03-22
 */
public interface ConstantsPlus extends Constants {

    /**
     * join SQL片段xml名称
     */
    String JOIN_SQL_NAME =  WRAPPER_DOT + "sqlJoin";

    String JOIN_SQL_SELECT =  WRAPPER_DOT + "joinSqlSelect";

    String TABLE_ALIAS_NAME =  WRAPPER_DOT + "masterTableAlias";

    String DEFAULT_TABLE_ALIAS_NAME = "t1";

    String TEST_CONTENT_1 =  "%s != null";

    String TEST_CONTENT_2 =  TEST_CONTENT_1 + " and %s != ''";

    String TEST_CONTENT_3 =  TEST_CONTENT_2 + " and %s";

    String TEST_DEFAULT_CONTENT =  TEST_CONTENT_1 + " and " + TEST_CONTENT_1;
}
