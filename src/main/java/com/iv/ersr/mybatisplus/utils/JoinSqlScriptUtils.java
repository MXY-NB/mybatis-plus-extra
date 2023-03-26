package com.iv.ersr.mybatisplus.utils;

import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.iv.ersr.mybatisplus.constant.ConstantsPlus;

/**
 * <p>
 * sql 脚本工具类
 * </p>
 *
 * @author moxiaoyu
 * @since 2023-03-26
 */
public class JoinSqlScriptUtils extends SqlScriptUtils implements ConstantsPlus  {

    public static String getSafeAliasColumn(String column) {
        return SqlScriptUtils.safeParam(TABLE_ALIAS_NAME) + DOT + column;
    }

    public static String getUnSafeAliasColumn(String column) {
        return SqlScriptUtils.unSafeParam(TABLE_ALIAS_NAME) + DOT + column;
    }
}