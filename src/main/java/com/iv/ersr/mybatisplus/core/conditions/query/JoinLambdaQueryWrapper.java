package com.iv.ersr.mybatisplus.core.conditions.query;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.iv.ersr.mybatisplus.core.conditions.AbstractJoinLambdaWrapper;
import com.iv.ersr.mybatisplus.core.conditions.func.JoinMethodFunc;
import com.iv.ersr.mybatisplus.core.entity.enums.SqlExcerpt;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class JoinLambdaQueryWrapper<T> extends AbstractJoinLambdaWrapper<T, JoinLambdaQueryWrapper<T>>
    implements Query<JoinLambdaQueryWrapper<T>, T, SFunction<T, ?>>, JoinMethodFunc<JoinLambdaQueryWrapper<T>, T> {

    /**
     * join字段
     */
    private final SharedString sqlJoin = SharedString.emptyString();

    /**
     * 查询字段
     */
    protected final List<SharedString> sqlSelect = new ArrayList<>();

    /**
     * 查询字段
     */
    protected final List<SharedString> joinSqlSelect = new ArrayList<>();

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
            sqlSelect.add(new SharedString(columnsToString(false, columns)));
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

    @Override
    public <J> JoinLambdaQueryWrapper<T> leftJoin(boolean condition, SFunction<T, ?> masterTableField, SFunction<J, ?> joinTableField, String alias) {
        return maybeDo(condition, ()->appendSqlSegments(SqlExcerpt.LEFT_JOIN, masterTableField, joinTableField, alias));
    }

    @Override
    public <J> JoinLambdaQueryWrapper<T> rightJoin(boolean condition, SFunction<T, ?> masterTableField, SFunction<J, ?> joinTableField, String alias) {
        return maybeDo(condition, ()->appendSqlSegments(SqlExcerpt.RIGHT_JOIN, masterTableField, joinTableField, alias));
    }

    /**
     * JOIN SELECT 部分 SQL 设置
     * @param columns 查询字段
     */
    @SafeVarargs
    @Override
    public final <J> JoinLambdaQueryWrapper<T> joinSelect(SFunction<J, ?>... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            joinSqlSelect.add(new SharedString(joinColumnsToString(false, columns)));
        }
        return typedThis;
    }

    /**
     * JOIN SELECT 部分 SQL 设置
     */
    @Override
    public final JoinLambdaQueryWrapper<T> joinSelect() {
        if (MapUtil.isNotEmpty(joinClassAliasMap)) {
            joinClassAliasMap.forEach((clz, alias)-> joinSqlSelect.addAll(joinAllColumns(clz, alias).stream().map(SharedString::new).collect(Collectors.toList())));
        }
        return typedThis;
    }

    protected <J> void appendSqlSegments(SqlExcerpt sqlExcerpt, SFunction<T, ?> masterTableField, SFunction<J, ?> joinTableField, String alias) {
        LambdaMeta lambdaMeta = LambdaUtils.extract(joinTableField);
        Class<?> instantiatedClass = lambdaMeta.getInstantiatedClass();
        joinClassAliasMap.put(instantiatedClass, alias);
        TableInfo tableInfo = TableInfoHelper.getTableInfo(instantiatedClass);
        Assert.isTrue(tableInfo != null, "请在对应实体类上加上TableName注解！");
        assert tableInfo != null;
        String joinTableName = tableInfo.getTableName();
        String sql = String.format(sqlExcerpt.getSql(), joinTableName, alias, columnToString(masterTableField, false, true), columnToString(joinTableField, false, false));
        log.info("sql:{}", sql);
        sqlJoin.setStringValue(sqlJoin.getStringValue() + StringPool.NEWLINE + sql);
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

    public String getSqlJoin() {
        return sqlJoin.getStringValue();
    }

    public String getJoinSqlSelect() {
        String sql = joinSqlSelect.stream().map(SharedString::getStringValue).collect(joining(StringPool.COMMA));
        return CharSequenceUtil.isEmpty(sql) ? null : StringPool.COMMA + sql;
    }

    @Override
    public void clear() {
        super.clear();
        sqlJoin.toNull();
        sqlSelect.clear();
        joinSqlSelect.clear();
    }
}