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
 * @since 2023-12-19
 */
public class PhysicalDelete extends AbstractMethodPlus {

    public PhysicalDelete() {
        this(SqlMethod.PHYSICAL_DELETE.getMethod());
    }

    /**
     * @param name 方法名
     * @since 3.5.0
     */
    public PhysicalDelete(String name) {
        super(name);
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlMethod sqlMethod = SqlMethod.PHYSICAL_DELETE;
        String sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(),
            sqlWrapperNoDeleted(),
            sqlComment());
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addDeleteMappedStatement(mapperClass, methodName, sqlSource);
    }
}