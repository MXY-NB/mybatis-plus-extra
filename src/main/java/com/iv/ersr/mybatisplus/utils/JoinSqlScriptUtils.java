package com.iv.ersr.mybatisplus.utils;

import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.iv.ersr.mybatisplus.constant.ConstantsPlus;

public class JoinSqlScriptUtils extends SqlScriptUtils implements ConstantsPlus  {

    public static String getSafeAliasColumn(String column) {
        return com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils.safeParam(TABLE_ALIAS_NAME) + DOT + column;
    }

    public static String getUnSafeAliasColumn(String column) {
        return com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils.unSafeParam(TABLE_ALIAS_NAME) + DOT + column;
    }
}