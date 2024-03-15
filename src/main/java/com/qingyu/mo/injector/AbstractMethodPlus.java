package com.qingyu.mo.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.qingyu.mo.annotaion.UpdateNull;
import com.qingyu.mo.constant.ConstantPlus;
import com.qingyu.mo.entity.TableInfoPlus;
import com.qingyu.mo.utils.JoinSqlScriptUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.function.Predicate;

import static java.util.stream.Collectors.joining;

/**
 * <p>
 * 抽象的注入方法类
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.6.2
 */
public abstract class AbstractMethodPlus extends AbstractMethod implements ConstantPlus {

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
        return String.format(AS_C, table.getTableInfo().getTableName(), SqlScriptUtils.unSafeParam(TABLE_ALIAS_NAME));
    }

    protected String sqlSelectColumns() {
        /* 假设存在用户自定义的 resultMap 映射返回 */
        String selectColumns = ASTERISK;
        if (table.getTableInfo().getResultMap() == null || table.getTableInfo().isAutoInitResultMap()) {
            /* 未设置 resultMap 或者 resultMap 是自动构建的,视为属于mp的规则范围内 */
            selectColumns = getAllSqlSelect(table);
        }
        return JoinSqlScriptUtil.convertChooseEwSelect(Q_WRAPPER_SQL_SELECT, selectColumns);
    }

    public String getAllSqlSelect(TableInfoPlus tableInfoPlus) {
        if (allSqlSelect != null) {
            return allSqlSelect;
        }
        allSqlSelect = tableInfoPlus.chooseSelect(TableFieldInfo::isSelect);
        return allSqlSelect;
    }

    protected String sqlSet(Predicate<TableFieldInfo> predicate, TableInfo table, boolean logic, boolean judgeAliasNull,
                            final String alias, final String prefix, final boolean updateNull) {
        String sqlScript = getAllSqlSet(predicate, table, logic, prefix, updateNull);
        if (judgeAliasNull) {
            sqlScript = SqlScriptUtils.convertIf(sqlScript, String.format(TEST_CONTENT_1, alias), true);
        }
        sqlScript = SqlScriptUtils.convertSet(sqlScript);
        return NEWLINE + sqlScript;
    }

    public String getAllSqlSet(Predicate<TableFieldInfo> predicate, TableInfo table, boolean ignoreLogicDelFiled,
                               final String prefix, final boolean updateNull) {
        final String newPrefix = prefix == null ? EMPTY : prefix;
        return table.getFieldList().stream()
                .filter(predicate)
                .filter(i -> {
                    if (ignoreLogicDelFiled) {
                        return !(table.isWithLogicDelete() && i.isLogicDelete());
                    }
                    return true;
                }).map(
                        i -> i.getSqlSet(updateNull && i.getField().getDeclaredAnnotation(UpdateNull.class) != null, newPrefix)
                ).filter(Objects::nonNull).collect(joining(NEWLINE));
    }

    protected String sqlWrapper() {
        if (table.getTableInfo().isWithLogicDelete()) {
            String sqlScript = "";
            sqlScript += (NEWLINE + table.getLogicDeleteSql(true, true) + NEWLINE);
            String normalSqlScript = SqlScriptUtils.convertIf(AND_C + SqlScriptUtils.unSafeParam(WRAPPER_SQLSEGMENT),
                    String.format(TEST_CONTENT_3, WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT,
                            WRAPPER_NONEMPTYOFNORMAL), true);
            normalSqlScript += NEWLINE;
            normalSqlScript += SqlScriptUtils.convertIf(SqlScriptUtils.unSafeParam(WRAPPER_SQLSEGMENT),
                    String.format(TEST_CONTENT_3, WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT,
                            WRAPPER_EMPTYOFNORMAL), true);
            sqlScript += normalSqlScript;
            sqlScript = SqlScriptUtils.convertChoose(String.format(TEST_CONTENT_1, WRAPPER), sqlScript,
                    table.getLogicDeleteSql(false, true));
            sqlScript = SqlScriptUtils.convertWhere(sqlScript);
            return NEWLINE + sqlScript;
        } else {
            return sqlWrapperNoDeleted();
        }
    }

    protected String sqlWrapperNoDeleted() {
        String sqlScript = "";
        sqlScript += SqlScriptUtils.convertIf(SqlScriptUtils.convertIf(AND_C, WRAPPER_NONEMPTYOFENTITY + AND_C + WRAPPER_NONEMPTYOFNORMAL, false) + SPACE + SqlScriptUtils.unSafeParam(WRAPPER_SQLSEGMENT),
                String.format(TEST_CONTENT_3, WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT,
                        WRAPPER_NONEMPTYOFWHERE), true);
        sqlScript = SqlScriptUtils.convertWhere(sqlScript) + NEWLINE;
        sqlScript += SqlScriptUtils.convertIf(SqlScriptUtils.unSafeParam(WRAPPER_SQLSEGMENT),
                String.format(TEST_CONTENT_3, WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT,
                        WRAPPER_EMPTYOFWHERE), true);
        sqlScript = SqlScriptUtils.convertIf(sqlScript, String.format(TEST_CONTENT_1, WRAPPER), true);
        return NEWLINE + sqlScript;
    }
}
