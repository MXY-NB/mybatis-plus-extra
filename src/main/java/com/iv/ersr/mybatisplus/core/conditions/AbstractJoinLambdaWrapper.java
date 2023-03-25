package com.iv.ersr.mybatisplus.core.conditions;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.enums.SqlLike;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.iv.ersr.mybatisplus.constant.ConstantsPlus;
import com.iv.ersr.mybatisplus.core.conditions.func.JoinCompare;
import com.iv.ersr.mybatisplus.core.conditions.func.JoinFunc;
import com.iv.ersr.mybatisplus.core.conditions.func.JoinJoin;
import com.iv.ersr.mybatisplus.core.conditions.func.JoinNested;
import lombok.Getter;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import static com.baomidou.mybatisplus.core.enums.SqlKeyword.*;
import static com.baomidou.mybatisplus.core.enums.WrapperKeyword.APPLY;
import static java.util.stream.Collectors.joining;

/**
 * Lambda 语法使用 Wrapper
 * <p>统一处理解析 lambda 获取 column</p>
 *
 * @author moxiaoyu
 * @since 2023-03-22
 */
public abstract class AbstractJoinLambdaWrapper<T, Children extends AbstractJoinLambdaWrapper<T, Children>>
        extends AbstractWrapper<T, SFunction<T, ?>, Children> implements JoinCompare<Children, SFunction<T, ?>>, JoinNested<Children, Children>, JoinJoin<Children>, JoinFunc<Children> {

    /**
     * 主表别名
     */
    @Getter
    protected String masterTableAlias;

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
        return columnsToString(false, column);
    }

    @SafeVarargs
    protected final String columnsToString(boolean onlyColumn, SFunction<T, ?>... columns) {
        return Arrays.stream(columns).map(i -> columnToString(i, onlyColumn, true)).collect(joining(StringPool.COMMA));
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

    /**
     * 获取字段名称
     * @param column 字段
     * @param onlyColumn 是否只需要返回字段
     * @param mainTable 是否是主表
     * @param <J> 联表类型
     * @return String 字段名
     */
    protected <J> String columnToString(SFunction<J, ?> column, boolean onlyColumn, boolean mainTable) {
        LambdaMeta meta = LambdaUtils.extract(column);
        String fieldName = PropertyNamer.methodToProperty(meta.getImplMethodName());
        Class<?> joinClass = meta.getInstantiatedClass();
        tryInitJoinCache(joinClass);
        ColumnCache joinColumnCache = getJoinColumnCache(fieldName, joinClass);

        String prefix;
        if (mainTable) {
            prefix = getMasterTableAlias();
        } else {
            Assert.isTrue(joinClassAliasMap.containsKey(joinClass), "未找到" + joinClass.getSimpleName() + "的别名，请先添加别名！");
            prefix = joinClassAliasMap.get(joinClass);
        }
        return onlyColumn ? joinColumnCache.getColumn() : prefix + StringPool.DOT + joinColumnCache.getColumnSelect();
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
     * map 所有非空属性等于 =
     *
     * @param condition   执行条件
     * @param params      map 类型的参数, key 是字段名, value 是字段值
     * @param null2IsNull 是否参数为 null 自动执行 isNull 方法, false 则忽略这个字段\
     * @return children
     */
    @Override
    public <R, V> Children joinAllEq(boolean condition, Map<R, V> params, boolean null2IsNull) {
        if (condition && CollUtil.isNotEmpty(params)) {
            params.forEach((k, v) -> {
                if (StringUtils.checkValNotNull(v)) {
                    eq(k, v);
                } else {
                    if (null2IsNull) {
                        isNull(k);
                    }
                }
            });
        }
        return typedThis;
    }

    /**
     * 字段过滤接口，传入多参数时允许对参数进行过滤
     *
     * @param condition   执行条件
     * @param filter      返回 true 来允许字段传入比对条件中
     * @param params      map 类型的参数, key 是字段名, value 是字段值
     * @param null2IsNull 是否参数为 null 自动执行 isNull 方法, false 则忽略这个字段
     * @return children
     */
    @Override
    public <R, V> Children joinAllEq(boolean condition, BiPredicate<R, V> filter, Map<R, V> params, boolean null2IsNull) {
        if (condition && CollUtil.isNotEmpty(params)) {
            params.forEach((k, v) -> {
                if (filter.test(k, v)) {
                    if (StringUtils.checkValNotNull(v)) {
                        eq(k, v);
                    } else {
                        if (null2IsNull) {
                            isNull(k);
                        }
                    }
                }
            });
        }
        return typedThis;
    }

    /**
     * 等于 =
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    @Override
    public <R> Children joinEq(boolean condition, R column, Object val) {
        return addCondition(condition, column, EQ, val);
    }

    /**
     * 不等于 &lt;&gt;
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    @Override
    public <R> Children joinNe(boolean condition, R column, Object val) {
        return addCondition(condition, column, NE, val);
    }

    /**
     * 大于 &gt;
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    @Override
    public <R> Children joinGt(boolean condition, R column, Object val) {
        return addCondition(condition, column, GT, val);
    }

    /**
     * 大于等于 &gt;=
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    @Override
    public <R> Children joinGe(boolean condition, R column, Object val) {
        return addCondition(condition, column, GE, val);
    }

    /**
     * 小于 &lt;
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    @Override
    public <R> Children joinLt(boolean condition, R column, Object val) {
        return addCondition(condition, column, LT, val);
    }

    /**
     * 小于等于 &lt;=
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    @Override
    public <R> Children joinLe(boolean condition, R column, Object val) {
        return addCondition(condition, column, LE, val);
    }

    /**
     * LIKE '%值%'
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    @Override
    public <R> Children joinLike(boolean condition, R column, Object val) {
        return likeValue(condition, LIKE, column, val, SqlLike.DEFAULT);
    }

    /**
     * NOT LIKE '%值%'
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    @Override
    public <R> Children joinNotLike(boolean condition, R column, Object val) {
        return likeValue(condition, NOT_LIKE, column, val, SqlLike.DEFAULT);
    }

    /**
     * LIKE '%值'
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    @Override
    public <R> Children joinLikeLeft(boolean condition, R column, Object val) {
        return likeValue(condition, LIKE, column, val, SqlLike.LEFT);
    }

    /**
     * LIKE '值%'
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    @Override
    public <R> Children joinLikeRight(boolean condition, R column, Object val) {
        return likeValue(condition, LIKE, column, val, SqlLike.RIGHT);
    }

    /**
     * NOT LIKE '%值'
     *
     * @param condition
     * @param column
     * @param val
     * @return children
     */
    @Override
    public <R> Children joinNotLikeLeft(boolean condition, R column, Object val) {
        return likeValue(condition, NOT_LIKE, column, val, SqlLike.LEFT);
    }

    /**
     * NOT LIKE '值%'
     *
     * @param condition
     * @param column
     * @param val
     * @return children
     */
    @Override
    public <R> Children joinNotLikeRight(boolean condition, R column, Object val) {
        return likeValue(condition, NOT_LIKE, column, val, SqlLike.RIGHT);
    }

    /**
     * BETWEEN 值1 AND 值2
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val1      值1
     * @param val2      值2
     * @return children
     */
    @Override
    public <R> Children joinBetween(boolean condition, R column, Object val1, Object val2) {
        return maybeDo(condition, () -> appendSqlSegments(columnToSqlSegment(column), BETWEEN,
                () -> formatParam(null, val1), AND, () -> formatParam(null, val2)));
    }

    /**
     * NOT BETWEEN 值1 AND 值2
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val1      值1
     * @param val2      值2
     * @return children
     */
    @Override
    public <R> Children joinNotBetween(boolean condition, R column, Object val1, Object val2) {
        return maybeDo(condition, () -> appendSqlSegments(columnToSqlSegment(column), NOT_BETWEEN,
                () -> formatParam(null, val1), AND, () -> formatParam(null, val2)));
    }

    @Override
    public Children joinAnd(boolean condition, Consumer<Children> consumer) {
        return and(condition).addNestedCondition(condition, consumer);
    }

    @Override
    public Children joinOr(boolean condition, Consumer<Children> consumer) {
        return or(condition).addNestedCondition(condition, consumer);
    }

    @Override
    public Children joinNested(boolean condition, Consumer<Children> consumer) {
        return addNestedCondition(condition, consumer);
    }

    @Override
    public Children joinNot(boolean condition, Consumer<Children> consumer) {
        return not(condition).addNestedCondition(condition, consumer);
    }

    @Override
    public Children joinHaving(boolean condition, String sqlHaving, Object... params) {
        return maybeDo(condition, () -> appendSqlSegments(HAVING,
                () -> formatSqlMaybeWithParam(sqlHaving, null, params)));
    }

    @Override
    public Children joinFunc(boolean condition, Consumer<Children> consumer) {
        return maybeDo(condition, () -> consumer.accept(typedThis));
    }

    @Override
    public Children joinOr(boolean condition) {
        return maybeDo(condition, () -> appendSqlSegments(OR));
    }

    @Override
    public Children joinApply(boolean condition, String applySql, Object... values) {
        return maybeDo(condition, () -> appendSqlSegments(APPLY,
                () -> formatSqlMaybeWithParam(applySql, null, values)));
    }

    @Override
    public Children joinLast(boolean condition, String lastSql) {
        if (condition) {
            this.lastSql.setStringValue(StringPool.SPACE + lastSql);
        }
        return typedThis;
    }

    @Override
    public Children joinComment(boolean condition, String comment) {
        if (condition) {
            this.sqlComment.setStringValue(comment);
        }
        return typedThis;
    }

    @Override
    public Children joinFirst(boolean condition, String firstSql) {
        if (condition) {
            this.sqlFirst.setStringValue(firstSql);
        }
        return typedThis;
    }

    @Override
    public Children joinExists(boolean condition, String existsSql, Object... values) {
        return maybeDo(condition, () -> appendSqlSegments(EXISTS,
                () -> String.format("(%s)", formatSqlMaybeWithParam(existsSql, null, values))));
    }

    @Override
    public Children joinNotExists(boolean condition, String existsSql, Object... values) {
        return not(condition).exists(condition, existsSql, values);
    }

    @Override
    public <R> Children joinIsNull(boolean condition, R column) {
        return maybeDo(condition, () -> appendSqlSegments(columnToSqlSegment(column), IS_NULL));
    }

    @Override
    public <R> Children joinIsNotNull(boolean condition, R column) {
        return maybeDo(condition, () -> appendSqlSegments(columnToSqlSegment(column), IS_NOT_NULL));
    }

    @Override
    public <R> Children joinIn(boolean condition, R column, Collection<?> coll) {
        return maybeDo(condition, () -> appendSqlSegments(columnToSqlSegment(column), IN, inExpression(coll)));
    }

    @Override
    public <R> Children joinIn(boolean condition, R column, Object... values) {
        return maybeDo(condition, () -> appendSqlSegments(columnToSqlSegment(column), IN, inExpression(values)));
    }

    @Override
    public <R> Children joinNotIn(boolean condition, R column, Collection<?> coll) {
        return maybeDo(condition, () -> appendSqlSegments(columnToSqlSegment(column), NOT_IN, inExpression(coll)));
    }

    @Override
    public <R> Children joinNotIn(boolean condition, R column, Object... values) {
        return maybeDo(condition, () -> appendSqlSegments(columnToSqlSegment(column), NOT_IN, inExpression(values)));
    }

    @Override
    public <R> Children joinInSql(boolean condition, R column, String inValue) {
        return maybeDo(condition, () -> appendSqlSegments(columnToSqlSegment(column), IN,
                () -> String.format("(%s)", inValue)));
    }

    @Override
    public <R> Children joinGtSql(boolean condition, R column, String inValue) {
        return maybeDo(condition, () -> appendSqlSegments(columnToSqlSegment(column), GT,
                () -> String.format("(%s)", inValue)));
    }

    @Override
    public <R> Children joinGeSql(boolean condition, R column, String inValue) {
        return maybeDo(condition, () -> appendSqlSegments(columnToSqlSegment(column), GE,
                () -> String.format("(%s)", inValue)));
    }

    @Override
    public <R> Children joinLtSql(boolean condition, R column, String inValue) {
        return maybeDo(condition, () -> appendSqlSegments(columnToSqlSegment(column), LT,
                () -> String.format("(%s)", inValue)));
    }

    @Override
    public <R> Children joinLeSql(boolean condition, R column, String inValue) {
        return maybeDo(condition, () -> appendSqlSegments(columnToSqlSegment(column), LE,
                () -> String.format("(%s)", inValue)));
    }

    @Override
    public <R> Children joinNotInSql(boolean condition, R column, String inValue) {
        return maybeDo(condition, () -> appendSqlSegments(columnToSqlSegment(column), NOT_IN,
                () -> String.format("(%s)", inValue)));
    }

    @Override
    public <R> Children joinGroupBy(boolean condition, R column, R... columns) {
        return maybeDo(condition, () -> {
            String one = columnToString(column);
            if (ArrayUtils.isNotEmpty(columns)) {
                one += (StringPool.COMMA + columnsToString(columns));
            }
            final String finalOne = one;
            appendSqlSegments(GROUP_BY, () -> finalOne);
        });
    }

    @Override
    public <R> Children joinOrderBy(boolean condition, boolean isAsc, R column, R... columns) {
        return maybeDo(condition, () -> {
            final SqlKeyword mode = isAsc ? ASC : DESC;
            appendSqlSegments(ORDER_BY, columnToSqlSegment(columnSqlInjectFilter(column)), mode);
            if (ArrayUtils.isNotEmpty(columns)) {
                Arrays.stream(columns).forEach(c -> appendSqlSegments(ORDER_BY,
                        columnToSqlSegment(columnSqlInjectFilter(c)), mode));
            }
        });
    }

    @Override
    public <R> Children joinOrderBy(boolean condition, boolean isAsc, R column) {
        return maybeDo(condition, () -> appendSqlSegments(ORDER_BY, columnToSqlSegment(columnSqlInjectFilter(column)),
                isAsc ? ASC : DESC));
    }

    @Override
    public <R> Children joinOrderBy(boolean condition, boolean isAsc, List<R> columns) {
        return maybeDo(condition, () -> columns.forEach(c -> appendSqlSegments(ORDER_BY,
                columnToSqlSegment(columnSqlInjectFilter(c)), isAsc ? ASC : DESC)));
    }

    @Override
    public <R> Children joinGroupBy(boolean condition, R column) {
        return maybeDo(condition, () -> appendSqlSegments(GROUP_BY, () -> columnToString(column)));
    }

    @Override
    public <R> Children joinGroupBy(boolean condition, List<R> columns) {
        return maybeDo(condition, () -> appendSqlSegments(GROUP_BY, () -> columnsToString(columns)));
    }

    public <R> Children joinGroupBy(boolean condition, boolean isAsc, R column) {
        return maybeDo(condition, () -> appendSqlSegments(ORDER_BY, columnToSqlSegment(columnSqlInjectFilter(column)),
                isAsc ? ASC : DESC));
    }

    public <R> Children joinGroupBy(boolean condition, boolean isAsc, List<R> columns) {
        return maybeDo(condition, () -> columns.forEach(c -> appendSqlSegments(ORDER_BY,
                columnToSqlSegment(columnSqlInjectFilter(c)), isAsc ? ASC : DESC)));
    }

    /**
     * 子类返回一个自己的新对象
     * @return Children
     */
    @Override
    protected abstract Children instance();

    @Override
    public void clear() {
        super.clear();
        joinClassAliasMap.clear();
        joinClassColumnMap.clear();
    }
}
