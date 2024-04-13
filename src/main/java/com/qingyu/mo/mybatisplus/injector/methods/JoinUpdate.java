package com.qingyu.mo.mybatisplus.injector.methods;

import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.qingyu.mo.mybatisplus.entity.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.qingyu.mo.mybatisplus.injector.AbstractMethodPlus;
import lombok.Setter;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 自定义UpdateBatchById方法
 * <p>
 * 批量更新数据
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
@Setter
public class JoinUpdate extends AbstractMethodPlus {

    public JoinUpdate() {
        this(SqlMethod.JOIN_UPDATE.getMethod());
    }

    /**
     * @since 3.5.0
     * @param name 方法名
     */
    public JoinUpdate(String name) {
        super(name);
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlMethod sqlMethod = SqlMethod.JOIN_UPDATE;

        setTableInfo(tableInfo);

        String sql = String.format(sqlMethod.getSql(), getJoinTableName(),
                SqlScriptUtils.convertSet(convertIfEwParam(U_WRAPPER_SQL_SET, false)),
                sqlWhereEntityWrapper(true, tableInfo), sqlComment());
        SqlSource sqlSource = super.createSqlSource(configuration, sql, modelClass);
        return this.addUpdateMappedStatement(mapperClass, modelClass, methodName, sqlSource);
    }
}
