package com.qingyu.mo.injector.methods;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.qingyu.mo.entity.enums.SqlMethod;
import com.qingyu.mo.injector.AbstractMethodPlus;
import com.qingyu.mo.utils.JoinSqlScriptUtil;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * <p>
 * 自定义JoinSelectPage方法
 * </p>
 *
 * @author qingyu-mo
 * @since 2023-12-19
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

        String sql = String.format(sqlMethod.getSql(),
                sqlFirst(),
                sqlSelectColumns(),
                JoinSqlScriptUtil.convertChooseEwSelect(SQL_JOIN_SELECT, null),
                getJoinTableName(),
                SqlScriptUtils.unSafeParam(SQL_JOIN),
                sqlWrapper(),
                sqlComment()
        );
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addSelectMappedStatementForTable(mapperClass, methodName, sqlSource, tableInfo);
    }
}