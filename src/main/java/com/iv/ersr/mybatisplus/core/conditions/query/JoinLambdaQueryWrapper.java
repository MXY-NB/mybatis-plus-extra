package com.iv.ersr.mybatisplus.core.conditions.query;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.iv.ersr.mybatisplus.core.conditions.AbstractJoinLambdaWrapper;
import com.iv.ersr.mybatisplus.core.entity.CollectionResultMap;
import com.iv.ersr.mybatisplus.core.entity.FieldMapping;
import com.iv.ersr.mybatisplus.exception.Exceptions;
import com.iv.ersr.mybatisplus.utils.Lambdas;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

/**
 * <p>
 * JoinWrappers相关方法
 * </p>
 *
 * @author moxiaoyu
 * @since 2023-03-17
 */
public class JoinLambdaQueryWrapper<T> extends AbstractJoinLambdaWrapper<T, JoinLambdaQueryWrapper<T>>
    implements Query<JoinLambdaQueryWrapper<T>, T, SFunction<T, ?>> {

    /**
     * 查询字段
     */
    @Getter
    private boolean resultMap = false;

    /**
     * 查询字段
     */
    private final List<SharedString> sqlSelect = new ArrayList<>();

    /**
     * 查询字段
     */
    private final List<SharedString> joinSqlSelect = new ArrayList<>();

    /**
     * 一对多查询字段
     */
    @Getter
    private final List<CollectionResultMap> collectionResultMaps = new ArrayList<>();

    public JoinLambdaQueryWrapper() {
        super((T) null);
    }

    /**
     * 子类返回一个自己的新对象
     */
    @Override
    protected JoinLambdaQueryWrapper<T> instance() {
        return new JoinLambdaQueryWrapper<>();
    }

    /**
     * SELECT 部分 SQL 设置
     *
     * @param columns 查询字段
     */
    @SafeVarargs
    @Override
    public final JoinLambdaQueryWrapper<T> select(SFunction<T, ?>... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            sqlSelect.add(new SharedString(columnsToString(false, true, columns)));
        }
        return typedThis;
    }

    /**
     * 过滤查询的字段信息(主键除外!)
     * <p>例1: 只要 java 字段名以 "test" 开头的             -> select(i -&gt; i.getProperty().startsWith("test"))</p>
     * <p>例2: 只要 java 字段属性是 CharSequence 类型的     -> select(TableFieldInfo::isCharSequence)</p>
     * <p>例3: 只要 java 字段没有填充策略的                 -> select(i -&gt; i.getFieldFill() == FieldFill.DEFAULT)</p>
     * <p>例4: 要全部字段                                   -> select(i -&gt; true)</p>
     * <p>例5: 只要主键字段                                 -> select(i -&gt; false)</p>
     *
     * @param predicate 过滤方式
     * @return this
     */
    @Override
    public JoinLambdaQueryWrapper<T> select(Class<T> entityClass, Predicate<TableFieldInfo> predicate) {
        if (entityClass == null) {
            entityClass = getEntityClass();
        } else {
            setEntityClass(entityClass);
        }
        Assert.notNull(entityClass, "entityClass can not be null");
        sqlSelect.add(new SharedString(TableInfoHelper.getTableInfo(entityClass).chooseSelect(predicate)));
        return typedThis;
    }

    /**
     * JOIN SELECT 部分 SQL 设置
     * @param columns 查询字段
     */
    @SafeVarargs
    @Override
    public final <J> JoinLambdaQueryWrapper<T> jSelect(SFunction<J, ?>... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            joinSqlSelect.add(new SharedString(joinColumnsToString(false, columns)));
        }
        return typedThis;
    }

    /**
     * JOIN SELECT 部分 SQL 设置
     */
    @Override
    public final JoinLambdaQueryWrapper<T> jSelect() {
        if (MapUtil.isNotEmpty(joinClassAliasMap)) {
            joinClassAliasMap.forEach((clz, alias)-> joinSqlSelect.addAll(joinAllColumns(clz, alias).stream().map(SharedString::new).collect(Collectors.toList())));
        }
        return typedThis;
    }

    /**
     * JOIN SELECT 部分 SQL 设置
     * @param collectionResultMap 查询字段
     */
    @Override
    public final JoinLambdaQueryWrapper<T> coll(CollectionResultMap collectionResultMap) {
        collectionResultMap.setPropertyName(Lambdas.toPropertyName(collectionResultMap.getProperty()));
        if (CollUtil.isNotEmpty(collectionResultMap.getFieldMappings())) {
            for (FieldMapping fieldMapping : collectionResultMap.getFieldMappings()) {
                if (fieldMapping.getColumn() != null) {
                    fieldMapping.setColumnName(Lambdas.toPropertyName(fieldMapping.getColumn()));
                }
                if (fieldMapping.getParam() != null) {
                    fieldMapping.setParamName(Lambdas.toPropertyName(fieldMapping.getParam()));
                }
                if (CharSequenceUtil.isEmpty(fieldMapping.getColumnName()) || CharSequenceUtil.isEmpty(fieldMapping.getParamName())) {
                    throw Exceptions.t("fieldMapping请填写完整参数");
                }
            }
        }
        collectionResultMaps.add(collectionResultMap);
        return typedThis;
    }

    @Override
    public String getSqlSelect() {
        String sql = sqlSelect.stream().map(SharedString::getStringValue).collect(joining(StringPool.COMMA));
        return CharSequenceUtil.isEmpty(sql) ? null : sql;
    }

    @Override
    public String getSqlSegment() {
        String sqlSegment = expression.getSqlSegment();
        return sqlSegment + lastSql.getStringValue();
    }

    public String getJoinSqlSelect() {
        String sql = joinSqlSelect.stream().map(SharedString::getStringValue).collect(joining(StringPool.COMMA));
        return CharSequenceUtil.isEmpty(sql) ? null : StringPool.COMMA + sql;
    }

    @Override
    public void clear() {
        super.clear();
        sqlSelect.clear();
        joinSqlSelect.clear();
        collectionResultMaps.clear();
    }
}