package com.qingyu.mo.mybatisplus.wrapper;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.qingyu.mo.mybatisplus.entity.JoinResultMap;
import com.qingyu.mo.mybatisplus.utils.ClassUtil;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.apache.ibatis.type.TypeHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Lambda 语法使用 Wrapper
 * <p>统一处理解析 lambda 获取 column</p>
 *
 * @author qingyu-mo
 * @since 1.0.6.2
 */
public abstract class AbstractJoinLambdaWrapper<T, Children extends AbstractJoinLambdaWrapper<T, Children>>
        extends AbstractJoinWrapper<T, Children> {

    @Override
    @SafeVarargs
    public final Children groupBy(boolean condition, SFunction<T, ?> column, SFunction<T, ?>... columns) {
        return super.groupBy(condition, column, columns);
    }

    @Override
    @SafeVarargs
    public final Children orderBy(boolean condition, boolean isAsc, SFunction<T, ?> column, SFunction<T, ?>... columns) {
        return orderBy(condition, isAsc, column, CollectionUtils.toList(columns));
    }

    @Override
    @SafeVarargs
    public final Children groupBy(SFunction<T, ?> column, SFunction<T, ?>... columns) {
        return doGroupBy(true, column, CollectionUtils.toList(columns));
    }

    @Override
    @SafeVarargs
    public final Children orderByAsc(SFunction<T, ?> column, SFunction<T, ?>... columns) {
        return super.orderByAsc(column, columns);
    }

    @Override
    @SafeVarargs
    public final Children orderByAsc(boolean condition, SFunction<T, ?> column, SFunction<T, ?>... columns) {
        return super.orderByAsc(condition, column, columns);
    }

    @Override
    @SafeVarargs
    public final Children orderByDesc(SFunction<T, ?> column, SFunction<T, ?>... columns) {
        return super.orderByDesc(column, columns);
    }

    @Override
    @SafeVarargs
    public final Children orderByDesc(boolean condition, SFunction<T, ?> column, SFunction<T, ?>... columns) {
        return super.orderByDesc(condition, column, columns);
    }

    /**
     * 获取字段名称
     * @param joinClass 关联的表
     * @param alias 别名
     * @return String 字段名
     */
    protected final List<ISqlSegment> joinAllColumns(Class<?> joinClass, String alias) {
        Map<String, ColumnCache> columnMap;
        if (joinClassColumnMap.containsKey(joinClass)) {
            columnMap = LambdaUtils.getColumnMap(joinClass);
        } else {
            columnMap = joinClassColumnMap.get(joinClass);
        }
        List<ISqlSegment> columns = new ArrayList<>();
        columnMap.forEach((columnName, columnCache)-> columns.add(strToSqlSegment(alias + StringPool.DOT + columnCache.getColumnSelect())));
        return columns;
    }

    /**
     * 获取字段名称
     * @param onlyColumn 是否只需要返回字段
     * @param mainTable 是否是主表
     * @param column 字段
     * @param alias 别名
     * @return String 字段名
     */
    @Override
    protected String columnToString(boolean onlyColumn, boolean mainTable, SFunction<?, ?> column, String alias) {
        Assert.isFalse(column == null, "column can't be null");
        LambdaMeta meta = LambdaUtils.extract(column);
        String fieldName = PropertyNamer.methodToProperty(meta.getImplMethodName());
        Class<?> joinClass = meta.getInstantiatedClass();
        tryInitJoinCache(joinClass);
        ColumnCache joinColumnCache = getJoinColumnCache(fieldName, joinClass);

        if (onlyColumn) {
            return joinColumnCache.getColumn();
        }
        String prefix;
        if (mainTable) {
            prefix = getMasterTableAlias();
        } else {
            if (CharSequenceUtil.isNotEmpty(joinColumnCache.getMapping())) {
                JoinResultMap joinResultMap = JoinResultMap.builder().column(alias == null ? joinColumnCache.getColumn() : CharSequenceUtil.toUnderlineCase(alias))
                        .property(alias == null ? fieldName : CharSequenceUtil.toCamelCase(alias))
                        .build();
                Class<?> typeHandler = ClassUtils.toClassConfident(CharSequenceUtil.subAfter(joinColumnCache.getMapping(), '=', true));
                if (typeHandler == JacksonTypeHandler.class) {
                    joinResultMap.setTypeHandler((TypeHandler<?>) ClassUtil.newInstance(typeHandler, ReflectUtil.getField(joinClass, fieldName).getType()));
                } else {
                    joinResultMap.setTypeHandler((TypeHandler<?>) ClassUtil.newInstance(typeHandler));
                }
                joinResultMaps.add(joinResultMap);
            }
            Assert.isTrue(joinClassAliasMap.containsKey(joinClass), "未找到" + joinClass.getSimpleName() + "的别名，请先添加别名！");
            prefix = joinClassAliasMap.get(joinClass);
        }
        return prefix + StringPool.DOT + joinColumnCache.getColumnSelect();
    }

    /**
     * 尝试初始化缓存
     * @param joinClass 联表class
     */
    private void tryInitJoinCache(Class<?> joinClass) {
        joinClassColumnMap.computeIfAbsent(joinClass, v->{
            Map<String, ColumnCache> columnMap = LambdaUtils.getColumnMap(v);
            Assert.notNull(columnMap, "can not find lambda cache for this entity [%s]", v.getName());
            return columnMap;
        });
    }

    private ColumnCache getJoinColumnCache(String fieldName, Class<?> joinClass) {
        ColumnCache columnCache = joinClassColumnMap.get(joinClass).get(LambdaUtils.formatKey(fieldName));
        Assert.notNull(columnCache, "can not find lambda cache for this property [%s] of entity [%s]",
                fieldName, joinClass.getName());
        return columnCache;
    }
}
