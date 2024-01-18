package com.qingyu.mo.wrapper.query;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.qingyu.mo.constant.ConstantPlus;
import com.qingyu.mo.entity.JoinResultMap;
import com.qingyu.mo.entity.Search;
import com.qingyu.mo.exception.Exceptions;
import com.qingyu.mo.func.JoinQuery;
import com.qingyu.mo.wrapper.AbstractJoinLambdaWrapper;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * <p>
 * JoinWrappers相关方法
 * </p>
 *
 * @author qingyu-mo
 * @since 2023-03-17
 */
public class JoinLambdaQueryWrapper<T> extends AbstractJoinLambdaWrapper<T, JoinLambdaQueryWrapper<T>>
    implements Query<JoinLambdaQueryWrapper<T>, T, SFunction<T, ?>>, JoinQuery<JoinLambdaQueryWrapper<T>, T, SFunction<T, ?>> {

    public JoinLambdaQueryWrapper() {
        this((T) null);
    }

    public JoinLambdaQueryWrapper(T entity) {
        super.setEntity(entity);
        super.initNeed();
    }

    public JoinLambdaQueryWrapper(Class<T> entityClass) {
        super.setEntityClass(entityClass);
        super.initNeed();
    }

    /**
     * 非对外公开的构造方法,只用于生产嵌套 sql
     */
    public JoinLambdaQueryWrapper(T entity, Class<T> entityClass, AtomicInteger paramNameSeq,
                                  Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments, SharedString paramAlias,
                                  SharedString lastSql, SharedString sqlComment, SharedString sqlFirst, Map<Class<?>, String> joinClassAliasMap, Integer joinNumber,
                                  Map<Class<?>, Map<String, ColumnCache>> joinClassColumnMap, List<JoinResultMap> joinResultMaps, String masterTableAlias) {
        super.setEntity(entity);
        super.setEntityClass(entityClass);
        super.initNeed();
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.paramAlias = paramAlias;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
        this.sqlFirst = sqlFirst;
        this.joinClassAliasMap = joinClassAliasMap;
        this.joinNumber = joinNumber;
        this.joinClassColumnMap = joinClassColumnMap;
        this.joinResultMaps = joinResultMaps;
        this.masterTableAlias = masterTableAlias;
    }

    /**
     * SELECT 部分 SQL 设置
     * @param condition 执行条件
     * @param columns 查询字段
     */
    @Override
    public JoinLambdaQueryWrapper<T> select(boolean condition, List<SFunction<T, ?>> columns) {
        return maybeDo(condition, ()->appendSelectSqlSegments(strToSqlSegment(columnsToString(columns))));
    }

    /**
     * 过滤查询的字段信息(主键除外!)
     * <p>例1: 只要 java 字段名以 "test" 开头的             -> select(i -&gt; i.getProperty().startsWith("test"))</p>
     * <p>例2: 只要 java 字段属性是 CharSequence 类型的     -> select(TableFieldInfo::isCharSequence)</p>
     * <p>例3: 只要 java 字段没有填充策略的                 -> select(i -&gt; i.getFieldFill() == FieldFill.DEFAULT)</p>
     * <p>例4: 要全部字段                                   -> select(i -&gt; true)</p>
     * <p>例5: 只要主键字段                                 -> select(i -&gt; false)</p>
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
        Exceptions.t(entityClass == null, "对象类为空！");
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entityClass);
        appendSelectSqlSegments(strToSqlSegment(masterTableAlias + StrPool.DOT + tableInfo.getKeySqlSelect()));
        tableInfo.getFieldList().stream().filter(predicate).forEach(i->appendSelectSqlSegments(strToSqlSegment(masterTableAlias + StrPool.DOT + i.getSqlSelect())));
        return typedThis;
    }

    /**
     * JOIN SELECT 部分 SQL 设置
     * @return this
     */
    @Override
    public JoinLambdaQueryWrapper<T> jSelect() {
        if (MapUtil.isNotEmpty(joinClassAliasMap)) {
            joinClassAliasMap.forEach((clz, alias) -> joinAllColumns(clz, alias).forEach(this::appendJoinSelectSqlSegments));
        }
        return typedThis;
    }

    /**
     * JOIN SELECT 部分 SQL 设置
     * @param columns 查询字段
     * @return this
     */
    @SafeVarargs
    @Override
    public final <J> JoinLambdaQueryWrapper<T> jSelect(SFunction<J, ?>... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            appendJoinSelectSqlSegments(strToSqlSegment(joinColumnsToString(columns)));
        }
        return typedThis;
    }

    /**
     * JOIN SELECT 部分 SQL 设置
     * @param column 查询字段
     * @param alias 别名
     * @return this
     */
    @Override
    public <J> JoinLambdaQueryWrapper<T> jSelect(SFunction<J, ?> column, String alias) {
        if (ObjectUtils.isNotEmpty(column)) {
            appendJoinSelectSqlSegments(strToSqlSegment(String.format(ConstantPlus.AS_C, joinColumnToString(column, alias), alias)));
        }
        return typedThis;
    }

    /**
     * 查询排除columns
     * <p>注意只有查询主表全部字段时(即未使用select方法)才会生效，生效时内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param columns 排除字段
     * @return this
     */
    @Override
    @SafeVarargs
    public final JoinLambdaQueryWrapper<T> noSelect(boolean condition, SFunction<T, ?>... columns) {
        return maybeDo(condition, ()->appendNoSelectSqlSegments(strToSqlSegment(columnsToString(true, columns))));
    }

    /**
     * join动态查询条件【暂定】
     * @param searches 搜索条件
     * @return children
     */
    @Override
    public JoinLambdaQueryWrapper<T> jDynamicSelect(List<Search> searches) {
        return typedThis;
    }

    /**
     * 子查询
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param entityClass 子查询主表
     * @param consumer 子查询
     * @param alias 别名
     * @return this
     */
    @Override
    public <J> JoinLambdaQueryWrapper<T> jChildSelect(Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer, String alias) {
        appendJoinSelectSqlSegments(strToSqlSegment(String.format(ConstantPlus.AS_C, addChildSelect(entityClass, consumer), alias)));
        return typedThis;
    }

    /**
     * 只查询sum(column)
     * @param column 字段
     * @return this
     */
    @Override
    public JoinLambdaQueryWrapper<T> selectSumOne(SFunction<T, ?> column, String alias) {
        appendSelectSqlSegments(strToSqlSegment(String.format(ConstantPlus.SUM_AS_IF_NULL, columnToString(column), alias)));
        return typedThis;
    }

    /**
     * 只查询sum(column)
     * @param column 字段
     * @return this
     */
    @Override
    public <J> JoinLambdaQueryWrapper<T> jSelectSumOne(SFunction<J, ?> column, String alias) {
        appendJoinSelectSqlSegments(strToSqlSegment(String.format(ConstantPlus.SUM_AS_IF_NULL, joinColumnToString(column), alias)));
        return typedThis;
    }

    /**
     * 只查询sum(column) + sum(column2)
     * @param column 字段
     * @return this
     */
    @Override
    public JoinLambdaQueryWrapper<T> selectSumAddOne(SFunction<T, ?> column, SFunction<T, ?> column2, String alias) {
        appendSelectSqlSegments(strToSqlSegment(String.format(ConstantPlus.SUM_ADD_AS_IF_NULL, columnToString(column), columnToString(column2), alias)));
        return typedThis;
    }

    /**
     * 只查询sum(column) + sum(column2)
     * @param column 字段
     * @return this
     */
    @Override
    public <J> JoinLambdaQueryWrapper<T> selectSumAddJOne(SFunction<T, ?> column, SFunction<J, ?> column2, String alias) {
        appendSelectSqlSegments(strToSqlSegment(String.format(ConstantPlus.SUM_ADD_AS_IF_NULL, columnToString(column), joinColumnToString(column2), alias)));
        return typedThis;
    }

    /**
     * 只查询sum(column) + sum(column2)
     * @param column 字段
     * @return this
     */
    @Override
    public <J> JoinLambdaQueryWrapper<T> jSelectSumAddOne(SFunction<J, ?> column, SFunction<T, ?> column2, String alias) {
        appendSelectSqlSegments(strToSqlSegment(String.format(ConstantPlus.SUM_ADD_AS_IF_NULL, joinColumnToString(column), columnToString(column2), alias)));
        return typedThis;
    }

    /**
     * 只查询sum(column) + sum(column2)
     * @param column 字段
     * @return this
     */
    @Override
    public <J> JoinLambdaQueryWrapper<T> jSelectSumAddJOne(SFunction<J, ?> column, SFunction<J, ?> column2, String alias) {
        appendSelectSqlSegments(strToSqlSegment(String.format(ConstantPlus.SUM_ADD_AS_IF_NULL, joinColumnToString(column), joinColumnToString(column2), alias)));
        return typedThis;
    }

    /**
     * 只查询sum(column) - sum(column2)
     * @param column 字段
     * @return this
     */
    @Override
    public JoinLambdaQueryWrapper<T> selectSumSubOne(SFunction<T, ?> column, SFunction<T, ?> column2, String alias) {
        appendSelectSqlSegments(strToSqlSegment(String.format(ConstantPlus.SUM_SUB_AS_IF_NULL, columnToString(column), columnToString(column2), alias)));
        return typedThis;
    }

    /**
     * 只查询sum(column) - sum(column2)
     * @param column 字段
     * @return this
     */
    @Override
    public <J> JoinLambdaQueryWrapper<T> selectSumSubJOne(SFunction<T, ?> column, SFunction<J, ?> column2, String alias) {
        appendSelectSqlSegments(strToSqlSegment(String.format(ConstantPlus.SUM_SUB_AS_IF_NULL, columnToString(column), joinColumnToString(column2), alias)));
        return typedThis;
    }

    /**
     * 只查询sum(column) - sum(column2)
     * @param column 字段
     * @return this
     */
    @Override
    public <J> JoinLambdaQueryWrapper<T> jSelectSumSubOne(SFunction<J, ?> column, SFunction<T, ?> column2, String alias) {
        appendSelectSqlSegments(strToSqlSegment(String.format(ConstantPlus.SUM_SUB_AS_IF_NULL, joinColumnToString(column), columnToString(column2), alias)));
        return typedThis;
    }

    /**
     * 只查询sum(column) - sum(column2)
     * @param column 字段
     * @return this
     */
    @Override
    public <J> JoinLambdaQueryWrapper<T> jSelectSumSubJOne(SFunction<J, ?> column, SFunction<J, ?> column2, String alias) {
        appendSelectSqlSegments(strToSqlSegment(String.format(ConstantPlus.SUM_SUB_AS_IF_NULL, joinColumnToString(column), joinColumnToString(column2), alias)));
        return typedThis;
    }

    /**
     * 只查询max(column)
     * @param column 字段
     * @return this
     */
    @Override
    public JoinLambdaQueryWrapper<T> selectMaxOne(SFunction<T, ?> column, String alias) {
        appendSelectSqlSegments(strToSqlSegment(String.format(ConstantPlus.MAX_AS, columnToString(column), alias)));
        return typedThis;
    }

    /**
     * 只查询max(column)
     * @param column 字段
     * @return this
     */
    @Override
    public <J> JoinLambdaQueryWrapper<T> jSelectMaxOne(SFunction<J, ?> column, String alias) {
        appendSelectSqlSegments(strToSqlSegment(String.format(ConstantPlus.MAX_AS, joinColumnToString(column), alias)));
        return typedThis;
    }

    /**
     * 执行sum函数
     * @param condition 执行条件
     * @param needIfNull 是否用IFNULL包裹
     * @param column 字段
     * @param alias 别名
     * @return this
     */
    @Override
    public JoinLambdaQueryWrapper<T> sum(boolean condition, boolean needIfNull, SFunction<T, ?> column, String alias) {
        return maybeDo(condition, () -> {
            String finalAlias = CharSequenceUtil.isEmpty(alias) ? columnToString(true, column) : alias;
            appendNoSelectSqlSegments(strToSqlSegment(finalAlias));
            appendJoinSelectSqlSegments(strToSqlSegment(String.format(
                    needIfNull ? ConstantPlus.SUM_AS_IF_NULL : ConstantPlus.SUM_AS,
                    columnToString(column), finalAlias
            )));
        });
    }

    /**
     * 执行sum函数
     * @param condition 执行条件
     * @param needIfNull 是否用IFNULL包裹
     * @param column 字段
     * @param alias 别名
     * @return this
     */
    @Override
    public <J> JoinLambdaQueryWrapper<T> jSum(boolean condition, boolean needIfNull, SFunction<J, ?> column, String alias) {
        return maybeDo(condition, () -> {
            String finalAlias = CharSequenceUtil.isEmpty(alias) ? joinColumnToString(true, column) : alias;
            appendNoSelectSqlSegments(strToSqlSegment(finalAlias));
            appendJoinSelectSqlSegments(strToSqlSegment(String.format(
                    needIfNull ? ConstantPlus.SUM_AS_IF_NULL : ConstantPlus.SUM_AS,
                    joinColumnToString(column), finalAlias

            )));
        });
    }

    /**
     * 执行sum函数相加
     * @param condition 执行条件
     * @param needIfNull 是否用IFNULL包裹
     * @param column 被加数
     * @param column2 加数
     * @param alias 别名
     * @return this
     */
    @Override
    public JoinLambdaQueryWrapper<T> sumAdd(boolean condition, boolean needIfNull, SFunction<T, ?> column, SFunction<T, ?> column2, String alias) {
        return maybeDo(condition, () -> {
            appendNoSelectSqlSegments(strToSqlSegment(alias));
            appendJoinSelectSqlSegments(strToSqlSegment(String.format(
                    needIfNull ? ConstantPlus.SUM_ADD_AS_IF_NULL : ConstantPlus.SUM_ADD_AS,
                    columnToString(column), columnToString(column2),
                    CharSequenceUtil.isEmpty(alias) ? columnToString(true, column) : alias
            )));
        });
    }

    /**
     * 执行sum函数相加
     * @param condition 执行条件
     * @param needIfNull 是否用IFNULL包裹
     * @param column 被加数
     * @param column2 加数
     * @param alias 别名
     * @return this
     */
    @Override
    public <J> JoinLambdaQueryWrapper<T> sumAddJ(boolean condition, boolean needIfNull, SFunction<T, ?> column, SFunction<J, ?> column2, String alias) {
        return maybeDo(condition, () -> {
            appendNoSelectSqlSegments(strToSqlSegment(alias));
            appendJoinSelectSqlSegments(strToSqlSegment(String.format(
                    needIfNull ? ConstantPlus.SUM_ADD_AS_IF_NULL : ConstantPlus.SUM_ADD_AS,
                    columnToString(column), joinColumnToString(column2),
                    CharSequenceUtil.isEmpty(alias) ? columnToString(true, column) : alias
            )));
        });
    }

    /**
     * 执行sum函数相减
     * @param condition 执行条件
     * @param needIfNull 是否用IFNULL包裹
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return this
     */
    @Override
    public JoinLambdaQueryWrapper<T> sumSub(boolean condition, boolean needIfNull, SFunction<T, ?> column, SFunction<T, ?> column2, String alias) {
        return maybeDo(condition, () -> {
            appendNoSelectSqlSegments(strToSqlSegment(alias));
            appendJoinSelectSqlSegments(strToSqlSegment(String.format(
                    needIfNull ? ConstantPlus.SUM_SUB_AS_IF_NULL : ConstantPlus.SUM_SUB_AS,
                    columnToString(column), columnToString(column2),
                    CharSequenceUtil.isEmpty(alias) ? columnToString(true, column) : alias
            )));
        });
    }

    /**
     * 执行sum函数相减
     * @param condition 执行条件
     * @param needIfNull 是否用IFNULL包裹
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return this
     */
    @Override
    public <J> JoinLambdaQueryWrapper<T> sumSubJ(boolean condition, boolean needIfNull, SFunction<T, ?> column, SFunction<J, ?> column2, String alias) {
        return maybeDo(condition, () -> {
            appendNoSelectSqlSegments(strToSqlSegment(alias));
            appendJoinSelectSqlSegments(strToSqlSegment(String.format(
                    needIfNull ? ConstantPlus.SUM_SUB_AS_IF_NULL : ConstantPlus.SUM_SUB_AS,
                    columnToString(column), joinColumnToString(column2),
                    CharSequenceUtil.isEmpty(alias) ? columnToString(true, column) : alias
            )));
        });
    }

    /**
     * 执行sum函数相加
     * @param condition 执行条件
     * @param needIfNull 是否用IFNULL包裹
     * @param column 被加数
     * @param column2 加数
     * @param alias 别名
     * @return this
     */
    @Override
    public <J> JoinLambdaQueryWrapper<T> jSumAdd(boolean condition, boolean needIfNull, SFunction<J, ?> column, SFunction<T, ?> column2, String alias) {
        return maybeDo(condition, () -> {
            appendNoSelectSqlSegments(strToSqlSegment(alias));
            appendJoinSelectSqlSegments(strToSqlSegment(String.format(
                    needIfNull ? ConstantPlus.SUM_ADD_AS_IF_NULL : ConstantPlus.SUM_ADD_AS,
                    joinColumnToString(column), columnToString(column2),
                    CharSequenceUtil.isEmpty(alias) ? joinColumnsToString(true, column) : alias
            )));
        });
    }

    /**
     * 执行sum函数相加
     * @param condition 执行条件
     * @param needIfNull 是否用IFNULL包裹
     * @param column 被加数
     * @param column2 加数
     * @param alias 别名
     * @return this
     */
    @Override
    public <J> JoinLambdaQueryWrapper<T> jSumAddJ(boolean condition, boolean needIfNull, SFunction<J, ?> column, SFunction<J, ?> column2, String alias) {
        return maybeDo(condition, () -> {
            appendNoSelectSqlSegments(strToSqlSegment(alias));
            appendJoinSelectSqlSegments(strToSqlSegment(String.format(
                    needIfNull ? ConstantPlus.SUM_ADD_AS_IF_NULL : ConstantPlus.SUM_ADD_AS,
                    joinColumnToString(column), joinColumnToString(column2),
                    CharSequenceUtil.isEmpty(alias) ? joinColumnsToString(true, column) : alias
            )));
        });
    }

    /**
     * 执行sum函数相减
     * @param condition 执行条件
     * @param needIfNull 是否用IFNULL包裹
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return this
     */
    @Override
    public <J> JoinLambdaQueryWrapper<T> jSumSub(boolean condition, boolean needIfNull, SFunction<J, ?> column, SFunction<T, ?> column2, String alias) {
        return maybeDo(condition, () -> {
            appendNoSelectSqlSegments(strToSqlSegment(alias));
            appendJoinSelectSqlSegments(strToSqlSegment(String.format(
                    needIfNull ? ConstantPlus.SUM_SUB_AS_IF_NULL : ConstantPlus.SUM_SUB_AS,
                    joinColumnToString(column), columnToString(column2),
                    CharSequenceUtil.isEmpty(alias) ? joinColumnsToString(true, column) : alias
            )));
        });
    }

    /**
     * 执行sum函数相减
     * @param condition 执行条件
     * @param needIfNull 是否用IFNULL包裹
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return this
     */
    @Override
    public <J> JoinLambdaQueryWrapper<T> jSumSubJ(boolean condition, boolean needIfNull, SFunction<J, ?> column, SFunction<J, ?> column2, String alias) {
        return maybeDo(condition, () -> {
            appendNoSelectSqlSegments(strToSqlSegment(alias));
            appendJoinSelectSqlSegments(strToSqlSegment(String.format(
                    needIfNull ? ConstantPlus.SUM_SUB_AS_IF_NULL : ConstantPlus.SUM_SUB_AS,
                    joinColumnToString(column), joinColumnToString(column2),
                    CharSequenceUtil.isEmpty(alias) ? joinColumnsToString(true, column) : alias
            )));
        });
    }

    /**
     * ifNull(column, column2)
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    @Override
    public JoinLambdaQueryWrapper<T> ifNull(boolean condition, SFunction<T, ?> column, SFunction<T, ?> column2) {
        return maybeDo(condition, ()->appendJoinSelectSqlSegments(()->String.format(ConstantPlus.IF_NULL,
                columnToString(column), columnToString(column2),
                columnToString(true, column))));
    }

    /**
     * ifNull(column, column2)
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    @Override
    public <J> JoinLambdaQueryWrapper<T> ifNullJ(boolean condition, SFunction<T, ?> column, SFunction<J, ?> column2) {
        return maybeDo(condition, ()->appendJoinSelectSqlSegments(()->String.format(ConstantPlus.IF_NULL,
                columnToString(column), joinColumnToString(column2),
                columnToString(true, column))));
    }

    /**
     * ifNull(column, column2)
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    @Override
    public <J> JoinLambdaQueryWrapper<T> jIfNull(boolean condition, SFunction<J, ?> column, SFunction<T, ?> column2) {
        return maybeDo(condition, ()->appendJoinSelectSqlSegments(()->String.format(ConstantPlus.IF_NULL,
                joinColumnToString(column), columnToString(column2),
                joinColumnToString(true, column))));
    }

    /**
     * ifNull(column, column2)
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    @Override
    public <J> JoinLambdaQueryWrapper<T> jIfNullJ(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2) {
        return maybeDo(condition, ()->appendJoinSelectSqlSegments(()->String.format(ConstantPlus.IF_NULL,
                joinColumnToString(column), joinColumnToString(column2),
                joinColumnToString(true, column))));
    }

    /**
     * ifNull(column, val)
     * @param condition 执行条件
     * @param column 字段
     * @param val 字段
     * @return children
     */
    @Override
    public JoinLambdaQueryWrapper<T> ifNull(boolean condition, SFunction<T, ?> column, Object val, String alias) {
        return maybeDo(condition, () -> appendJoinSelectSqlSegments(() -> String.format(ConstantPlus.IF_NULL,
                joinColumnToString(column), paramToSqlSegment(val).getSqlSegment(), alias)));
    }

    /**
     * ifNull(column, val)
     * @param condition 执行条件
     * @param column 字段
     * @param val 字段
     * @param alias 别名
     * @return children
     */
    @Override
    public <J> JoinLambdaQueryWrapper<T> jIfNull(boolean condition, SFunction<J, ?> column, Object val, String alias) {
        return maybeDo(condition, () -> appendJoinSelectSqlSegments(() -> String.format(ConstantPlus.IF_NULL,
                joinColumnToString(column), paramToSqlSegment(val).getSqlSegment(), alias)));
    }

    /**
     * if(consumer, trueValue, falseValue)
     * @param condition 执行条件
     * @param consumer 消费函数
     * @param trueValue 正确值
     * @param falseValue 错误值
     * @param alias 别名
     * @return children
     */
    @Override
    public JoinLambdaQueryWrapper<T> ifTo(boolean condition, Consumer<JoinLambdaQueryWrapper<T>> consumer, Object trueValue, Object falseValue, String alias) {
        return maybeDo(condition, ()->appendJoinSelectSqlSegments(strToSqlSegment(String.format(ConstantPlus.IF,
                getInstance(consumer).getSqlSegment(), trueValue, falseValue, alias))));
    }

    /**
     * if(column, trueValue, falseValue)
     * @param condition 执行条件
     * @param column 字段
     * @param trueValue 正确值
     * @param falseValue 错误值
     * @param alias 别名
     * @return children
     */
    @Override
    public JoinLambdaQueryWrapper<T> ifTo(boolean condition, SFunction<T, ?> column, Object trueValue, Object falseValue, String alias) {
        return maybeDo(condition, ()->appendJoinSelectSqlSegments(strToSqlSegment(String.format(ConstantPlus.IF, columnToString(column), trueValue, falseValue, alias))));
    }

    /**
     * if(column, trueValue, falseValue)
     * @param condition 执行条件
     * @param column 字段
     * @param trueValue 正确值
     * @param falseValue 错误值
     * @param alias 别名
     * @return children
     */
    @Override
    public <J> JoinLambdaQueryWrapper<T> jIfTo(boolean condition, SFunction<J, ?> column, Object trueValue, Object falseValue, String alias) {
        return maybeDo(condition, ()->appendJoinSelectSqlSegments(strToSqlSegment(String.format(ConstantPlus.IF, joinColumnToString(column), trueValue, falseValue, alias))));
    }

    /**
     * count(字段) as alias
     * @param condition 执行条件
     * @param column 字段
     * @param alias 别名
     * @return children
     */
    @Override
    public JoinLambdaQueryWrapper<T> count(boolean condition, SFunction<T, ?> column, String alias) {
        return maybeDo(condition, () -> {
            appendNoSelectSqlSegments(strToSqlSegment(alias));
            appendSelectSqlSegments(strToSqlSegment(String.format(ConstantPlus.COUNT_AS, columnToString(column), alias)));
        });
    }

    /**
     * count(字段) as alias
     * @param condition 执行条件
     * @param column 字段
     * @param alias 别名
     * @return children
     */
    @Override
    public <J> JoinLambdaQueryWrapper<T> jCount(boolean condition, SFunction<J, ?> column, String alias) {
        return maybeDo(condition, () -> appendSelectSqlSegments(strToSqlSegment(String.format(ConstantPlus.COUNT_AS, joinColumnToString(column), alias))));
    }

    /**
     * 用于生成嵌套 sql
     * <p>
     * 故 sqlSelect 不向下传递
     * </p>
     */
    @Override
    protected JoinLambdaQueryWrapper<T> instance() {
        return new JoinLambdaQueryWrapper<>(getEntity(), getEntityClass(), paramNameSeq, paramNameValuePairs, new MergeSegments(),
                paramAlias, SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString(), joinClassAliasMap, joinNumber, joinClassColumnMap, getJoinResultMaps(), getMasterTableAlias());
    }
}