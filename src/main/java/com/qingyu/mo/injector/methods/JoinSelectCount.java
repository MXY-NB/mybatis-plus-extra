package com.qingyu.mo.injector.methods;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.qingyu.mo.entity.enums.SqlMethod;
import com.qingyu.mo.injector.AbstractMethodPlus;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * <p>
 * 自定义JoinSelectCount方法
 * </p>
 *
 * @author qingyu-mo
 * @since 2023-12-19
 */
public class JoinSelectCount extends AbstractMethodPlus {

    /**
     * 默认方法名
     */
    public JoinSelectCount() {
        this(SqlMethod.JOIN_SELECT_COUNT.getMethod());
    }

    /**
     * 默认方法名
     * @param name 方法名
     */
    public JoinSelectCount(String name) {
        super(name);
    }

    @Override
    @SuppressWarnings("all")
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlMethod sqlMethod = SqlMethod.JOIN_SELECT_COUNT;

        setTableInfo(tableInfo);

        String sql = String.format(sqlMethod.getSql(), sqlFirst(), getJoinTableName(), SqlScriptUtils.unSafeParam(SQL_JOIN),
                sqlWrapper(), sqlComment());
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addSelectMappedStatementForOther(mapperClass, methodName, sqlSource, Long.class);
    }
}