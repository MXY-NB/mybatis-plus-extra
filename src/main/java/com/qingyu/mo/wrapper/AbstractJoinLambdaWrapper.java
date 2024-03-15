package com.qingyu.mo.wrapper;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.ClassUtils;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.qingyu.mo.entity.JoinResultMap;
import com.qingyu.mo.utils.ClassUtil;
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
        } else {
            String prefix;
            if (mainTable) {
                prefix = getMasterTableAlias();
            } else {
                if (CharSequenceUtil.isNotEmpty(joinColumnCache.getMapping())) {
                    JoinResultMap joinResultMap = JoinResultMap.builder().column(alias == null ? joinColumnCache.getColumn() : alias)
                            .property(alias == null ? fieldName : alias)
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
}
