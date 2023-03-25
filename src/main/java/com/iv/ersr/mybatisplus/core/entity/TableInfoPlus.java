package com.iv.ersr.mybatisplus.core.entity;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.iv.ersr.mybatisplus.constant.ConstantsPlus;
import com.iv.ersr.mybatisplus.utils.JoinSqlScriptUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

import static java.util.stream.Collectors.joining;

@Getter
public class TableInfoPlus implements ConstantsPlus {

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private String allSqlSelect;

    /**
     * 主表信息
     */
    private TableInfo tableInfo;

    public TableInfoPlus(TableInfo tableInfo) {
        this.tableInfo = tableInfo;
    }

    public String getAllSqlWhere(TableInfo table, boolean ignoreLogicDelFiled, boolean withId, final String prefix) {
        final String newPrefix = prefix == null ? EMPTY : prefix;
        String filedSqlScript = table.getFieldList().stream()
                .filter(i -> {
                    if (ignoreLogicDelFiled) {
                        return !(table.isWithLogicDelete() && i.isLogicDelete());
                    }
                    return true;
                })
                .map(i -> getSqlWhere(i, newPrefix)).filter(Objects::nonNull).collect(joining(NEWLINE));
        if (!withId || StringUtils.isBlank(table.getKeyProperty())) {
            return filedSqlScript;
        }
        String newKeyProperty = newPrefix + table.getKeyProperty();
        String keySqlScript = JoinSqlScriptUtils.getUnSafeAliasColumn(table.getKeyColumn()) + EQUALS + JoinSqlScriptUtils.safeParam(newKeyProperty);
        return JoinSqlScriptUtils.convertIf(keySqlScript, String.format(TEST_CONTENT_1, newKeyProperty), false)
                + NEWLINE + filedSqlScript;
    }

    public String getSqlWhere(TableFieldInfo tableFieldInfo, final String prefix) {
        final String newPrefix = prefix == null ? EMPTY : prefix;
        // 默认:  AND column=#{prefix + el}
        String sqlScript = " AND " + String.format(tableFieldInfo.getCondition(), JoinSqlScriptUtils.getUnSafeAliasColumn(tableFieldInfo.getColumn()), newPrefix + tableFieldInfo.getEl());
        // 查询的时候只判非空
        return convertIf(tableFieldInfo, sqlScript, convertIfProperty(newPrefix, tableFieldInfo.getProperty()), tableFieldInfo.getWhereStrategy());
    }

    private String convertIfProperty(String prefix, String property) {
        return CharSequenceUtil.isBlank(prefix) ? property : prefix.substring(0, prefix.length() - 1) + "['" + property + "']";
    }

    private String convertIf(TableFieldInfo tableFieldInfo, final String sqlScript, final String property, final FieldStrategy fieldStrategy) {
        if (fieldStrategy == FieldStrategy.NEVER) {
            return null;
        }
        if (tableFieldInfo.isPrimitive() || fieldStrategy == FieldStrategy.IGNORED) {
            return sqlScript;
        }
        if (fieldStrategy == FieldStrategy.NOT_EMPTY && tableFieldInfo.isCharSequence()) {
            return JoinSqlScriptUtils.convertIf(sqlScript, String.format(TEST_CONTENT_2, property, property),
                    false);
        }
        return JoinSqlScriptUtils.convertIf(sqlScript, String.format(TEST_CONTENT_1, property), false);
    }

    public String getLogicDeleteSql(boolean startWithAnd, boolean isWhere) {
        if (this.tableInfo.isWithLogicDelete()) {
            String logicDeleteSql = formatLogicDeleteSql(isWhere);
            if (startWithAnd) {
                logicDeleteSql = " AND " + logicDeleteSql;
            }
            return logicDeleteSql;
        }
        return EMPTY;
    }

    protected String formatLogicDeleteSql(boolean isWhere) {
        String logicDeleteColumn = JoinSqlScriptUtils.getUnSafeAliasColumn(this.tableInfo.getLogicDeleteFieldInfo().getColumn());
        final String value = isWhere ? this.tableInfo.getLogicDeleteFieldInfo().getLogicNotDeleteValue() : this.tableInfo.getLogicDeleteFieldInfo().getLogicDeleteValue();
        if (isWhere) {
            if (NULL.equalsIgnoreCase(value)) {
                return logicDeleteColumn + " IS NULL";
            } else {
                return logicDeleteColumn + EQUALS + String.format(this.tableInfo.getLogicDeleteFieldInfo().isCharSequence() ? "'%s'" : "%s", value);
            }
        }
        final String targetStr = logicDeleteColumn + EQUALS;
        if (NULL.equalsIgnoreCase(value)) {
            return targetStr + NULL;
        } else {
            return targetStr + String.format(this.tableInfo.getLogicDeleteFieldInfo().isCharSequence() ? "'%s'" : "%s", value);
        }
    }
}
