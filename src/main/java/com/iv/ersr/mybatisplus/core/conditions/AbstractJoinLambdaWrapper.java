package com.iv.ersr.mybatisplus.core.conditions;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.iv.ersr.mybatisplus.constant.ConstantsPlus;
import com.iv.ersr.mybatisplus.core.entity.enums.SqlExcerpt;
import lombok.Getter;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.util.*;

import static java.util.stream.Collectors.joining;

/**
 * Lambda 语法使用 Wrapper
 * <p>统一处理解析 lambda 获取 column</p>
 *
 * @author moxiaoyu
 * @since 2023-03-22
 */
public abstract class AbstractJoinLambdaWrapper<T, Children extends AbstractJoinLambdaWrapper<T, Children>>
        extends AbstractJoinWrapper<T, Children> {

    /**
     * 主表别名
     */
    @Getter
    protected String masterTableAlias;

    /**
     * join字段
     */
    private final SharedString sqlJoin = SharedString.emptyString();

    /**
     * join表的别名
     */
    protected final Map<Class<?>, String> joinClassAliasMap = new HashMap<>(16);

    /**
     * 要联表的字段缓存
     */
    private final Map<Class<?>, Map<String, ColumnCache>> joinClassColumnMap = new HashMap<>(16);

    protected AbstractJoinLambdaWrapper(T entity) {
        this(entity, null);
    }

    protected AbstractJoinLambdaWrapper(T entity, String alias) {
        super.setEntity(entity);
        super.initNeed();
        masterTableAlias = ObjectUtil.defaultIfBlank(alias, ConstantsPlus.DEFAULT_TABLE_ALIAS_NAME);
    }

    protected AbstractJoinLambdaWrapper(Class<T> entityClass) {
        this(entityClass, null);
    }

    protected AbstractJoinLambdaWrapper(Class<T> entityClass, String alias) {
        super.setEntityClass(entityClass);
        super.initNeed();
        this.masterTableAlias = ObjectUtil.defaultIfBlank(alias, ConstantsPlus.DEFAULT_TABLE_ALIAS_NAME);
    }

    @Override
    protected String columnToString(SFunction<T, ?> column) {
        return columnsToString(false, true, column);
    }

    @Override
    protected <J> String joinColumnToString(SFunction<J, ?> column) {
        return columnsToString(false, false, column);
    }

    protected final String columnsToString(boolean onlyColumn, boolean mainTable, SFunction<?, ?>... columns) {
        return Arrays.stream(columns).map(i -> columnToString(i, onlyColumn, mainTable)).collect(joining(StringPool.COMMA));
    }

    /**
     * 获取字段名称
     * @param columns 字段
     * @param onlyColumn 是否只需要返回字段
     * @param <J> 联表类型
     * @return String 字段名
     */
    @SafeVarargs
    protected final <J> String joinColumnsToString(boolean onlyColumn, SFunction<J, ?>... columns) {
        return Arrays.stream(columns).map(i -> columnToString(i, onlyColumn, false)).collect(joining(StringPool.COMMA));
    }

    /**
     * 获取字段名称
     * @param joinClass 关联的表
     * @param alias 别名
     * @return String 字段名
     */
    protected final List<String> joinAllColumns(Class<?> joinClass, String alias) {
        Map<String, ColumnCache> columnMap;
        if (joinClassColumnMap.containsKey(joinClass)) {
            columnMap = LambdaUtils.getColumnMap(joinClass);
        } else {
            columnMap = joinClassColumnMap.get(joinClass);
        }
        List<String> columns = new ArrayList<>();
        columnMap.forEach((columnName, columnCache)-> columns.add(alias + StringPool.DOT + columnCache.getColumnSelect()));
        return columns;
    }

    @Override
    protected <J> void appendSqlSegments(SqlExcerpt sqlExcerpt, SFunction<T, ?> masterTableField, SFunction<J, ?> joinTableField, String alias) {
        LambdaMeta lambdaMeta = LambdaUtils.extract(joinTableField);
        Class<?> instantiatedClass = lambdaMeta.getInstantiatedClass();
        joinClassAliasMap.put(instantiatedClass, alias);
        TableInfo tableInfo = TableInfoHelper.getTableInfo(instantiatedClass);
        cn.hutool.core.lang.Assert.isTrue(tableInfo != null, "请在对应实体类上加上TableName注解！");
        assert tableInfo != null;
        String joinTableName = tableInfo.getTableName();
        String sql = String.format(sqlExcerpt.getSql(), joinTableName, alias, columnToString(masterTableField, false, true), columnToString(joinTableField, false, false));
        sqlJoin.setStringValue(sqlJoin.getStringValue() + StringPool.NEWLINE + sql);
    }

    /**
     * 获取字段名称
     * @param column 字段
     * @param onlyColumn 是否只需要返回字段
     * @param mainTable 是否是主表
     * @return String 字段名
     */
    protected String columnToString(SFunction<?, ?> column, boolean onlyColumn, boolean mainTable) {
        LambdaMeta meta = LambdaUtils.extract(column);
        String fieldName = PropertyNamer.methodToProperty(meta.getImplMethodName());
        Class<?> joinClass = meta.getInstantiatedClass();
        tryInitJoinCache(joinClass);
        ColumnCache joinColumnCache = getJoinColumnCache(fieldName, joinClass);

        if (onlyColumn) {
            return joinColumnCache.getColumn();
        } else {
            String prefix;
            if (mainTable) {
                prefix = getMasterTableAlias();
            } else {
                Assert.isTrue(joinClassAliasMap.containsKey(joinClass), "未找到" + joinClass.getSimpleName() + "的别名，请先添加别名！");
                prefix = joinClassAliasMap.get(joinClass);
            }
            return prefix + StringPool.DOT + joinColumnCache.getColumnSelect();
        }
    }

    /**
     * 尝试初始化缓存
     * @param joinClass 联表class
     */
    private void tryInitJoinCache(Class<?> joinClass) {
        if (!joinClassColumnMap.containsKey(joinClass)) {
            Map<String, ColumnCache> columnMap = LambdaUtils.getColumnMap(joinClass);
            Assert.notNull(columnMap, "can not find lambda cache for this entity [%s]", joinClass.getName());
            joinClassColumnMap.put(joinClass, columnMap);
        }
    }

    private ColumnCache getJoinColumnCache(String fieldName, Class<?> joinClass) {
        ColumnCache columnCache = joinClassColumnMap.get(joinClass).get(LambdaUtils.formatKey(fieldName));
        Assert.notNull(columnCache, "can not find lambda cache for this property [%s] of entity [%s]",
                fieldName, joinClass.getName());
        return columnCache;
    }

    /**
     * 子类返回一个自己的新对象
     * @return Children
     */
    @Override
    protected abstract Children instance();

    public String getSqlJoin() {
        return sqlJoin.getStringValue();
    }

    @Override
    public void clear() {
        super.clear();
        sqlJoin.toNull();
        joinClassAliasMap.clear();
        joinClassColumnMap.clear();
    }
}
