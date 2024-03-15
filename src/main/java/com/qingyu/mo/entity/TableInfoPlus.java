package com.qingyu.mo.entity;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.qingyu.mo.constant.ConstantPlus;
import com.qingyu.mo.utils.JoinSqlScriptUtil;
import lombok.Getter;

import java.util.function.Predicate;

import static java.util.stream.Collectors.joining;

/**
 * <p>
 * 自定义TableInfo扩展
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.6.2
 */
@Getter
public class TableInfoPlus implements ConstantPlus {

    /**
     * 主表信息
     */
    private final TableInfo tableInfo;

    public TableInfoPlus(TableInfo tableInfo) {
        this.tableInfo = tableInfo;
    }

    public String getBatchVersionOli(final String alias) {
        TableFieldInfo versionFieldInfo = tableInfo.getVersionFieldInfo();
        final String oli = ConstantPlus.AND_C + versionFieldInfo.getColumn() + EQUALS + SqlScriptUtils.safeParam(JoinSqlScriptUtil.sqBracket(MPE_OPTLOCK_VERSION_ORIGINAL_COLL, SqlScriptUtils.unSafeParam(INDEX)));
        final String ognlStr = JoinSqlScriptUtil.sqBracketWithQuote(alias, versionFieldInfo.getProperty());
        if (versionFieldInfo.isCharSequence()) {
            return SqlScriptUtils.convertIf(oli, String.format(TEST_CONTENT_1, alias) + AND_C + String.format(TEST_CONTENT_2, ognlStr, ognlStr), true);
        } else {
            return SqlScriptUtils.convertIf(oli, String.format(TEST_CONTENT_1, alias) + AND_C + String.format(TEST_CONTENT_1, ognlStr), true);
        }
    }

    public String chooseSelect(Predicate<TableFieldInfo> predicate) {
        String sqlSelect = tableInfo.getKeySqlSelect();
        String fieldsSqlSelect = tableInfo.getFieldList().stream().filter(predicate)
                .map(i -> JoinSqlScriptUtil.getUnSafeAliasColumn(i.getSqlSelect())).collect(joining(COMMA));
        if (CharSequenceUtil.isNotEmpty(fieldsSqlSelect)) {
            return CharSequenceUtil.isEmpty(sqlSelect) ? fieldsSqlSelect : JoinSqlScriptUtil.getUnSafeAliasColumn(sqlSelect) + COMMA + fieldsSqlSelect;
        }
        return sqlSelect;
    }

    public String getLogicDeleteSql(boolean startWithAnd, boolean isWhere) {
        if (this.tableInfo.isWithLogicDelete()) {
            String logicDeleteSql = formatLogicDeleteSql(isWhere);
            if (startWithAnd) {
                logicDeleteSql = AND_C + logicDeleteSql;
            }
            return logicDeleteSql;
        }
        return EMPTY;
    }

    private String formatLogicDeleteSql(boolean isWhere) {
        String logicDeleteColumn = JoinSqlScriptUtil.getUnSafeAliasColumn(this.tableInfo.getLogicDeleteFieldInfo().getColumn());
        final String value = isWhere ? this.tableInfo.getLogicDeleteFieldInfo().getLogicNotDeleteValue() : this.tableInfo.getLogicDeleteFieldInfo().getLogicDeleteValue();
        if (isWhere) {
            if (NULL.equalsIgnoreCase(value)) {
                return logicDeleteColumn + " IS NULL";
            } else {
                return logicDeleteColumn + EQUALS + String.format(this.tableInfo.getLogicDeleteFieldInfo().isCharSequence() ? "'" + VARIABLE + "'" : VARIABLE, value);
            }
        }
        final String targetStr = logicDeleteColumn + EQUALS;
        if (NULL.equalsIgnoreCase(value)) {
            return targetStr + NULL;
        } else {
            return targetStr + (this.tableInfo.getLogicDeleteFieldInfo().isCharSequence() ? "'" + value + "'" : value);
        }
    }
}
