package com.iv.ersr.mybatisplus.core.injector;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.iv.ersr.mybatisplus.constant.ConstantsPlus;
import com.iv.ersr.mybatisplus.core.entity.TableInfoPlus;
import com.iv.ersr.mybatisplus.core.entity.enums.SqlExcerpt;
import com.iv.ersr.mybatisplus.utils.JoinSqlScriptUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Predicate;

import static java.util.stream.Collectors.joining;

/**
 * @author moxiaoyu
 * @Title: JoinAbstractMethod
 * @time 8/27/21 3:53 PM
 */
public abstract class AbstractMethodPlus extends AbstractMethod implements ConstantsPlus {

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private String allSqlSelect;

    @Getter
    private TableInfoPlus table;

    protected void setTableInfo(TableInfo tableInfo) {
        this.table = new TableInfoPlus(tableInfo);
    }

    protected AbstractMethodPlus(String methodName) {
        super(methodName);
    }

    protected String getJoinTableName() {
        return String.format(SqlExcerpt.TABLE_AS.getSql(), table.getTableInfo().getTableName(), JoinSqlScriptUtils.unSafeParam(TABLE_ALIAS_NAME));
    }

    protected String sqlSelectColumns(boolean queryWrapper) {
        /* 假设存在用户自定义的 resultMap 映射返回 */
        String selectColumns = ASTERISK;
        if (table.getTableInfo().getResultMap() == null || table.getTableInfo().isAutoInitResultMap()) {
            /* 未设置 resultMap 或者 resultMap 是自动构建的,视为属于mp的规则范围内 */
            selectColumns = getAllSqlSelect(table.getTableInfo());
        }
        if (!queryWrapper) {
            return selectColumns;
        }
        return convertChooseEwSelect(Q_WRAPPER_SQL_SELECT, selectColumns) + convertChooseEwSelect(JOIN_SQL_SELECT, null);
    }

    public String getAllSqlSelect(TableInfo tableInfo) {
        if (allSqlSelect != null) {
            return allSqlSelect;
        }
        allSqlSelect = chooseSelect(tableInfo, TableFieldInfo::isSelect);
        return allSqlSelect;
    }

    public String chooseSelect(TableInfo tableInfo, Predicate<TableFieldInfo> predicate) {
        String sqlSelect = tableInfo.getKeySqlSelect();
        String fieldsSqlSelect = tableInfo.getFieldList().stream().filter(predicate)
                .map(i-> JoinSqlScriptUtils.getUnSafeAliasColumn(i.getSqlSelect())).collect(joining(COMMA));
        if (CharSequenceUtil.isNotEmpty(fieldsSqlSelect)) {
            return CharSequenceUtil.isEmpty(sqlSelect)?fieldsSqlSelect:JoinSqlScriptUtils.getUnSafeAliasColumn(sqlSelect) + COMMA + fieldsSqlSelect;
        }
        return sqlSelect;
    }

    protected String convertChooseEwSelect(final String param, final String otherwise) {
        return convertChoose(String.format(TEST_DEFAULT_CONTENT, WRAPPER, param),
                SqlScriptUtils.unSafeParam(param), otherwise);
    }

    public static String convertChoose(final String whenTest, final String whenSqlScript, final String otherwise) {
        String sql = "<choose>" + NEWLINE
                + "<when test=\"" + whenTest + QUOTE + RIGHT_CHEV + NEWLINE
                + whenSqlScript + NEWLINE + "</when>" + NEWLINE;
        if (otherwise != null) {
            sql += "<otherwise>" + otherwise + "</otherwise>" + NEWLINE;
        }
        sql += "</choose>";
        return sql;
    }

    protected String sqlWrapper(boolean newLine) {
        String sqlScript = "";
        if (table.getTableInfo().isWithLogicDelete()) {
            sqlScript += (NEWLINE + table.getLogicDeleteSql(true, true) + NEWLINE);
            String normalSqlScript = SqlScriptUtils.convertIf(String.format("AND ${%s}", WRAPPER_SQLSEGMENT),
                    String.format(TEST_CONTENT_3, WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT,
                            WRAPPER_NONEMPTYOFNORMAL), true);
            normalSqlScript += NEWLINE;
            normalSqlScript += SqlScriptUtils.convertIf(String.format(" ${%s}", WRAPPER_SQLSEGMENT),
                    String.format(TEST_CONTENT_3, WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT,
                            WRAPPER_EMPTYOFNORMAL), true);
            sqlScript += normalSqlScript;
            sqlScript = SqlScriptUtils.convertChoose(String.format(TEST_CONTENT_1, WRAPPER), sqlScript,
                    table.getLogicDeleteSql(false, true));
            sqlScript = SqlScriptUtils.convertWhere(sqlScript);
        } else {
            sqlScript += NEWLINE;
            sqlScript += SqlScriptUtils.convertIf(String.format(SqlScriptUtils.convertIf(" AND", String.format("%s and %s", WRAPPER_NONEMPTYOFENTITY, WRAPPER_NONEMPTYOFNORMAL), false) + " ${%s}", WRAPPER_SQLSEGMENT),
                    String.format(TEST_CONTENT_3, WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT,
                            WRAPPER_NONEMPTYOFWHERE), true);
            sqlScript = SqlScriptUtils.convertWhere(sqlScript) + NEWLINE;
            sqlScript += SqlScriptUtils.convertIf(String.format(" ${%s}", WRAPPER_SQLSEGMENT),
                    String.format(TEST_CONTENT_3, WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT,
                            WRAPPER_EMPTYOFWHERE), true);
            sqlScript = SqlScriptUtils.convertIf(sqlScript, String.format(TEST_CONTENT_1, WRAPPER), true);
        }
        return newLine ? NEWLINE + sqlScript : sqlScript;
    }
}
