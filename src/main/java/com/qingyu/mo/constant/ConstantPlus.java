package com.qingyu.mo.constant;

import com.baomidou.mybatisplus.core.toolkit.Constants;

/**
 * <p>
 * 自定义常量扩展
 * </p>
 *
 * @author qingyu-mo
 * @since 2023-12-19
 */
public interface ConstantPlus extends Constants {
    String VARIABLE = "%s";

    String ADD = " + ";

    String SUB = " - ";

    String OR = "OR";

    String COUNT = "COUNT" + LEFT_BRACKET + VARIABLE + RIGHT_BRACKET;

    String COUNT_AS = COUNT + AS + VARIABLE;

    String AND_C = SPACE + AND + SPACE;

    String WHERE_C = SPACE + WHERE + SPACE;

    String OR_C = SPACE + OR + SPACE;

    String EQUALS_C = SPACE + EQUALS + SPACE;

    String AS_C = VARIABLE + AS + VARIABLE;

    String SUM = "sum"+ LEFT_BRACKET + VARIABLE + RIGHT_BRACKET;

    String SUM_IF_NULL = "IFNULL" + LEFT_BRACKET + SUM + COMMA + "0" + RIGHT_BRACKET;

    String SUM_AS = SUM + AS + VARIABLE;

    String SUM_AS_IF_NULL = SUM_IF_NULL + AS + VARIABLE;

    String SUM_ADD = SUM + ADD + SUM;

    String SUM_ADD_AS = LEFT_BRACKET + SUM_ADD + RIGHT_BRACKET + AS + VARIABLE;

    String SUM_ADD_IF_NULL = SUM_IF_NULL + ADD + SUM_IF_NULL;

    String SUM_ADD_AS_IF_NULL = SUM_ADD_IF_NULL + AS + VARIABLE;

    String SUM_SUB = SUM + SUB + SUM;

    String SUM_SUB_AS = LEFT_BRACKET + SUM_SUB + RIGHT_BRACKET + AS + VARIABLE;

    String SUM_SUB_IF_NULL = SUM_IF_NULL + SUB + SUM_IF_NULL;

    String SUM_SUB_AS_IF_NULL = SUM_SUB_IF_NULL + AS + VARIABLE;

    String MAX = "max"+ LEFT_BRACKET + VARIABLE + RIGHT_BRACKET;

    String MAX_AS = MAX + AS + VARIABLE;

    String SQL_JOIN =  WRAPPER_DOT + "sqlJoin";

    String SQL_JOIN_SELECT =  WRAPPER_DOT + "sqlJoinSelect";

    String TABLE_ALIAS_NAME =  WRAPPER_DOT + "masterTableAlias";

    String DEFAULT_TABLE_ALIAS_NAME = "t1";

    String TEST_CONTENT_1 = VARIABLE + SPACE + EXCLAMATION_MARK + EQUALS + SPACE + NULL;

    String TEST_CONTENT_2 = TEST_CONTENT_1 + AND_C + VARIABLE + SPACE + EXCLAMATION_MARK + EQUALS + SPACE + SINGLE_QUOTE + SINGLE_QUOTE;

    String TEST_CONTENT_3 = TEST_CONTENT_2 + AND_C + VARIABLE;

    String TEST_DEFAULT_CONTENT =  TEST_CONTENT_1 + AND_C + TEST_CONTENT_1;

    String JOIN_SQL_PART = VARIABLE + SPACE + VARIABLE + SPACE + ON + SPACE;

    String LEFT_JOIN = "LEFT JOIN" + SPACE + JOIN_SQL_PART;

    String RIGHT_JOIN = "RIGHT JOIN" + SPACE + JOIN_SQL_PART;

    String INNER_JOIN = "INNER JOIN" + SPACE + JOIN_SQL_PART;

    String IF_NULL = "IFNULL" + LEFT_BRACKET + VARIABLE + COMMA + VARIABLE + RIGHT_BRACKET + AS + VARIABLE;

    String IF = "IF" + LEFT_BRACKET + VARIABLE + COMMA + VARIABLE + COMMA + VARIABLE + RIGHT_BRACKET + AS + VARIABLE;

    String CHILD_SELECT = "SELECT" + SPACE + VARIABLE + SPACE + "FROM" + SPACE + AS_C;

    String JSON_CONTAIN = "JSON_CONTAINS" + LEFT_BRACKET + VARIABLE + COMMA + "JSON_ARRAY";

    String JSON_OVERLAPS = "JSON_OVERLAPS" + LEFT_BRACKET + VARIABLE + COMMA + "JSON_ARRAY";
}
