package com.iv.ersr.mybatisplus.core.injector.methods;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.iv.ersr.mybatisplus.core.entity.enums.SqlMethod;
import com.iv.ersr.mybatisplus.core.injector.AbstractMethodPlus;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

public class JoinSelectList extends AbstractMethodPlus {

    /**
     * 默认方法名
     */
    public JoinSelectList() {
        this(SqlMethod.JOIN_SELECT_LIST.getMethod());
    }

    /**
     * 默认方法名
     * @param name 方法名
     */
    public JoinSelectList(String name) {
        super(name);
    }

    @Override
    @SuppressWarnings("all")
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlMethod sqlMethod = SqlMethod.JOIN_SELECT_LIST;

        setTableInfo(tableInfo);

        String sql = String.format(sqlMethod.getSql(), sqlFirst(), sqlSelectColumns(true), getJoinTableName(), SqlScriptUtils.unSafeParam(JOIN_SQL_NAME),
                sqlWrapper(true), sqlComment());
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addSelectMappedStatementForTable(mapperClass, sqlMethod.getMethod(), sqlSource, tableInfo);
    }
}