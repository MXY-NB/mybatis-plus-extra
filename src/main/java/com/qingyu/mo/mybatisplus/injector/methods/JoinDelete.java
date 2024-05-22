package com.qingyu.mo.mybatisplus.injector.methods;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.qingyu.mo.mybatisplus.entity.enums.SqlMethod;
import com.qingyu.mo.mybatisplus.injector.AbstractMethodPlus;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 自定义PhysicalDelete方法
 * <p>
 * 物理删除
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.6.2
 */
public class JoinDelete extends AbstractMethodPlus {

    public JoinDelete() {
        this(SqlMethod.JOIN_LOGIC_DELETE.getMethod());
    }

    /**
     * @param name 方法名
     */
    public JoinDelete(String name) {
        super(name);
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlMethod sqlMethod = SqlMethod.JOIN_LOGIC_DELETE;

        setTableInfo(tableInfo);

        String sql;
        if (tableInfo.isWithLogicDelete()) {
            sql = String.format(sqlMethod.getSql(),
                    getJoinTableName(),
                    sqlLogicSet(tableInfo),
                    sqlWrapper(true),
                    sqlComment());
            SqlSource sqlSource = super.createSqlSource(configuration, sql, modelClass);
            return addUpdateMappedStatement(mapperClass, modelClass, methodName, sqlSource);
        } else {
            sqlMethod = SqlMethod.PHYSICAL_DELETE;
            sql = String.format(sqlMethod.getSql(),
                    getJoinTableName(),
                    sqlWrapper(false),
                    sqlComment());
            SqlSource sqlSource = super.createSqlSource(configuration, sql, modelClass);
            return addDeleteMappedStatement(mapperClass, methodName, sqlSource);
        }
    }
}