package com.qingyu.mo.injector.methods;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.qingyu.mo.entity.enums.SqlMethod;
import com.qingyu.mo.injector.AbstractMethodPlus;
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
public class PhysicalDeleteById extends AbstractMethodPlus {

    public PhysicalDeleteById() {
        this(SqlMethod.PHYSICAL_DELETE_BY_ID.getMethod());
    }

    /**
     * @param name 方法名
     */
    public PhysicalDeleteById(String name) {
        super(name);
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlMethod sqlMethod = SqlMethod.PHYSICAL_DELETE_BY_ID;

        setTableInfo(tableInfo);

        String sql = String.format(sqlMethod.getSql(),
                getJoinTableName(),
                tableInfo.getKeyColumn(),
                tableInfo.getKeyProperty());
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addDeleteMappedStatement(mapperClass, methodName, sqlSource);
    }
}