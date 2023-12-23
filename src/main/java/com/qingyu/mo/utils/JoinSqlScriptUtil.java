package com.qingyu.mo.utils;

import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.qingyu.mo.constant.ConstantPlus;

/**
 * <p>
 * sql 脚本工具类
 * </p>
 *
 * @author qingyu-mo
 * @since 2023-03-26
 */
public class JoinSqlScriptUtil extends SqlScriptUtils implements ConstantPlus {

    /**
     * column -> ${ew.masterTableAlias}.column
     * @param column 字段
     * @return String
     */
    public static String getUnSafeAliasColumn(String column) {
        return SqlScriptUtils.unSafeParam(TABLE_ALIAS_NAME) + DOT + column;
    }

    /**
     * column -> #{ew.masterTableAlias}.column
     * @param column 字段
     * @return String
     */
    public static String getSafeAliasColumn(String column) {
        return SqlScriptUtils.safeParam(TABLE_ALIAS_NAME) + DOT + column;
    }

    /**
     * column -> DATE_FORMAT(column,'%Y-%m')
     * @param column 字段
     * @return String
     */
    public static String getDateFormatSql(String column) {
        return "DATE_FORMAT(" + column + ",'%Y-%m')";
    }

    /**
     * param -> (param)
     * @param param 值
     * @return String
     */
    public static String bracket(String param) {
        return LEFT_BRACKET + param + RIGHT_BRACKET;
    }

    public static String convertChooseEwSelect(final String param, final String otherwise) {
        return convertChoose(String.format(TEST_DEFAULT_CONTENT, WRAPPER, param),
                SqlScriptUtils.unSafeParam(param), otherwise);
    }

    public static String convertChoose(final String whenTest, final String whenSqlScript, final String otherwise) {
        String sql = "<choose>" + NEWLINE
                + "<when test=\"" + whenTest + QUOTE + RIGHT_CHEV + NEWLINE
                + whenSqlScript + NEWLINE + "</when>" + NEWLINE;
        if (otherwise != null) {
            sql += "<otherwise>\n" + otherwise + "\n</otherwise>" + NEWLINE;
        }
        sql += "</choose>";
        return sql;
    }
}