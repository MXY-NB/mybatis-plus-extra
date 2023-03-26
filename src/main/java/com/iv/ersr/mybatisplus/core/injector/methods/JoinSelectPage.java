package com.iv.ersr.mybatisplus.core.injector.methods;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.iv.ersr.mybatisplus.core.entity.enums.SqlMethod;
import com.iv.ersr.mybatisplus.core.injector.AbstractMethodPlus;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * <p>
 * 自定义JoinSelectPage方法
 * </p>
 *
 * @author moxiaoyu
 * @since 2023-03-22
 */
public class JoinSelectPage extends AbstractMethodPlus {

    /**
     * 默认方法名
     */
    public JoinSelectPage() {
        this(SqlMethod.JOIN_SELECT_PAGE.getMethod());
    }

    /**
     * 默认方法名
     * @param name 方法名
     */
    public JoinSelectPage(String name) {
        super(name);
    }

    @Override
    @SuppressWarnings("all")
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlMethod sqlMethod = SqlMethod.JOIN_SELECT_PAGE;

        setTableInfo(tableInfo);

        String sql = String.format(sqlMethod.getSql(), sqlFirst(), sqlSelectColumns(true), convertChooseEwSelect(JOIN_SQL_SELECT, null), getJoinTableName(), SqlScriptUtils.unSafeParam(JOIN_SQL_NAME),
                sqlWrapper(), sqlComment());
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addSelectMappedStatementForTable(mapperClass, sqlMethod.getMethod(), sqlSource, tableInfo);
    }
}