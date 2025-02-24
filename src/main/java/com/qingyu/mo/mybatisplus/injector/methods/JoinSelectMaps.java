package com.qingyu.mo.mybatisplus.injector.methods;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.qingyu.mo.mybatisplus.entity.enums.SqlMethod;
import com.qingyu.mo.mybatisplus.injector.AbstractMethodPlus;
import com.qingyu.mo.mybatisplus.utils.JoinSqlScriptUtil;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.Map;

/**
 * <p>
 * 自定义JoinSelectMaps方法
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.6.2
 */
public class JoinSelectMaps extends AbstractMethodPlus {

    /**
     * 默认方法名
     */
    public JoinSelectMaps() {
        this(SqlMethod.JOIN_SELECT_MAPS.getMethod());
    }

    /**
     * 默认方法名
     * @param name 方法名
     */
    public JoinSelectMaps(String name) {
        super(name);
    }

    @Override
    @SuppressWarnings("all")
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlMethod sqlMethod = SqlMethod.JOIN_SELECT_MAPS;

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
        return this.addSelectMappedStatementForOther(mapperClass, methodName, sqlSource, Map.class);
    }
}