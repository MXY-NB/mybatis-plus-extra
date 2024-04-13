package com.qingyu.mo.mybatisplus.injector.methods;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.qingyu.mo.mybatisplus.entity.enums.SqlMethod;
import com.qingyu.mo.mybatisplus.injector.AbstractMethodPlus;
import com.qingyu.mo.mybatisplus.utils.JoinSqlScriptUtil;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.List;
import java.util.function.Predicate;

/**
 * 自定义JoinSelectCount方法
 * <p>
 * 批量新增数据,自选字段 insert
 * 不同的数据库支持度不一样!!! 只在 mysql 下测试过!!! 只在 mysql 下测试过!!! 只在 mysql 下测试过!!!
 * 除了主键是 数据库自增的未测试 外理论上都可以使用!!!
 * 如果你使用自增有报错或主键值无法回写到entity,就不要跑来问为什么了,因为我也不知道!!!
 * 注意: 这是自选字段 insert !!,如果个别字段在 entity 里为 null 但是数据库中有配置默认值, insert 后数据库字段是为 null 而不是默认值
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.6.2
 */
@Setter
public class InsertList extends AbstractMethodPlus {

    /**
     * 字段筛选条件
     */
    @Accessors(chain = true)
    private Predicate<TableFieldInfo> predicate;

    /**
     * 默认方法名
     * @param predicate 字段筛选条件
     */
    public InsertList(Predicate<TableFieldInfo> predicate) {
        this(SqlMethod.INSERT_LIST.getMethod(), predicate);
    }

    /**
     * 默认方法名
     * @param name 方法名
     * @param predicate 字段筛选条件
     */
    public InsertList(String name, Predicate<TableFieldInfo> predicate) {
        super(name);
        this.predicate = predicate;
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;
        SqlMethod sqlMethod = SqlMethod.INSERT_LIST;
        List<TableFieldInfo> fieldList = tableInfo.getFieldList();
        String insertSqlColumn = tableInfo.getKeyInsertSqlColumn(true, null, false) +
            filterTableFieldInfo(fieldList, predicate, TableFieldInfo::getInsertSqlColumn, EMPTY);
        String columnScript = JoinSqlScriptUtil.bracket(insertSqlColumn.substring(0, insertSqlColumn.length() - 1));
        String insertSqlProperty = tableInfo.getKeyInsertSqlProperty(true, ENTITY_DOT, false) +
            filterTableFieldInfo(fieldList, predicate, i -> i.getInsertSqlProperty(ENTITY_DOT), EMPTY);
        insertSqlProperty = JoinSqlScriptUtil.bracket(insertSqlProperty.substring(0, insertSqlProperty.length() - 1));
        String valuesScript = SqlScriptUtils.convertForeach(insertSqlProperty, "list", null, ENTITY, COMMA);
        String keyProperty = null;
        String keyColumn = null;
        // 表包含主键处理逻辑,如果不包含主键当普通字段处理
        if (tableInfo.havePK()) {
            if (tableInfo.getIdType() == IdType.AUTO) {
                /* 自增主键 */
                keyGenerator = Jdbc3KeyGenerator.INSTANCE;
                keyProperty = tableInfo.getKeyProperty();
                keyColumn = tableInfo.getKeyColumn();
            } else {
                if (null != tableInfo.getKeySequence()) {
                    keyGenerator = TableInfoHelper.genKeyGenerator(methodName, tableInfo, builderAssistant);
                    keyProperty = tableInfo.getKeyProperty();
                    keyColumn = tableInfo.getKeyColumn();
                }
            }
        }
        String sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), columnScript, valuesScript);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return addInsertMappedStatement(mapperClass, modelClass, methodName, sqlSource, keyGenerator, keyProperty, keyColumn);
    }

}
