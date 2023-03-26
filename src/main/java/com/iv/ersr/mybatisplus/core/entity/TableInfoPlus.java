package com.iv.ersr.mybatisplus.core.entity;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.iv.ersr.mybatisplus.constant.ConstantsPlus;
import com.iv.ersr.mybatisplus.utils.JoinSqlScriptUtils;
import lombok.Getter;

/**
 * <p>
 * 自定义TableInfo扩展
 * </p>
 *
 * @author moxiaoyu
 * @since 2023-03-22
 */
public class TableInfoPlus implements ConstantsPlus {

    /**
     * 主表信息
     */
    @Getter
    private final TableInfo tableInfo;

    public TableInfoPlus(TableInfo tableInfo) {
        this.tableInfo = tableInfo;
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
