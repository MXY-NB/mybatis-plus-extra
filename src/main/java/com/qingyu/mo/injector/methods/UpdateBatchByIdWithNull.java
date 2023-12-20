package com.qingyu.mo.injector.methods;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.qingyu.mo.entity.enums.SqlMethod;
import com.qingyu.mo.injector.AbstractMethodPlus;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.function.Predicate;

/**
 * 自定义UpdateBatchById方法
 * <p>
 * 批量更新数据，允许字段更新为null值
 * </p>
 *
 * @author qingyu-mo
 * @since 2023-12-19
 */
@Setter
public class UpdateBatchByIdWithNull extends AbstractMethodPlus {

    /**
     * 字段筛选条件
     */
    @Accessors(chain = true)
    private Predicate<TableFieldInfo> predicate;

    /**
     * 默认方法名
     * @param predicate 字段筛选条件
     * @since 3.5.0
     */
    public UpdateBatchByIdWithNull(Predicate<TableFieldInfo> predicate) {
        this(SqlMethod.UPDATE_BATCH_BY_ID_WITH_NULL.getMethod(), predicate);
    }

    /**
     * 默认方法名
     * @param name 方法名
     * @param predicate 字段筛选条件
     */
    public UpdateBatchByIdWithNull(String name, Predicate<TableFieldInfo> predicate) {
        super(name);
        this.predicate = predicate;
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlMethod sqlMethod = SqlMethod.UPDATE_BATCH_BY_ID_WITH_NULL;
        String additional = tableInfo.isWithVersion() ? tableInfo.getVersionFieldInfo().getVersionOli("item", "item.") : "" + tableInfo.getLogicDeleteSql(true, true);
        String setSql = sqlSet(predicate, tableInfo,
                tableInfo.isWithLogicDelete(), true, "item", "item.", true);
        String sqlResult = String.format(sqlMethod.getSql(), tableInfo.getTableName(), setSql, tableInfo.getKeyColumn(), "item." + tableInfo.getKeyProperty(), additional);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sqlResult, modelClass);
        // 第三个参数必须和RootMapper的自定义方法名一致
        return this.addUpdateMappedStatement(mapperClass, modelClass, methodName, sqlSource);
    }
}
