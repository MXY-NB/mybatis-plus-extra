package com.qingyu.mo.func;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.qingyu.mo.entity.Search;
import com.qingyu.mo.utils.LambdaUtil;
import com.qingyu.mo.wrapper.query.JoinLambdaQueryWrapper;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

/**
 * <p>
 * join查询扩展方法
 * </p>
 *
 * @author qingyu-mo
 * @since 2023-03-17
 */
public interface JoinQuery<Children, T, R> extends Serializable {

    /**
     * join查询所有字段
     * @return children
     */
    Children jSelect();

    /**
     * join查询字段
     * @param columns 需要查询的关联的表字段
     * @return children
     */
    @SuppressWarnings("unchecked")
    <J> Children jSelect(SFunction<J, ?>... columns);

    /**
     * join查询字段
     * @param column 需要查询的关联的表字段
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children jSelect(SFunction<J, ?> column, SFunction<T, ?> aliasField) {
        return jSelect(column, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * join查询字段
     * <p>注意只有查询主表全部字段时(即未使用select方法)才会生效，生效时内部有 entity 才能使用该方法</p>
     * @param column 需要查询的关联的表字段
     * @param alias 别名
     * @return children
     */
    <J> Children jSelect(SFunction<J, ?> column, String alias);

    /**
     * 查询排除columns
     * <p>注意只有查询主表全部字段时(即未使用select方法)才会生效，生效时内部有 entity 才能使用该方法</p>
     * @param columns 字段
     * @return children
     */
    @SuppressWarnings("unchecked")
    default Children noSelect(R... columns) {
        return noSelect(true, columns);
    }

    /**
     * 查询排除columns
     * @param condition 执行条件
     * @param columns 字段
     * @return children
     */
    @SuppressWarnings("unchecked")
    Children noSelect(boolean condition, R... columns);

    /**
     * join动态查询条件【暂定】
     * @param searches 搜索条件
     * @return children
     */
    Children jDynamicSelect(List<Search> searches);

    /**
     * 子查询
     * <p>注意只有内部有 entity 才能使用该方法</p>
     * @param entityClass 子查询主表
     * @param consumer 子查询
     * @param alias 别名
     * @return children
     */
    <J> Children jChildSelect(Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer, String alias);

    /**
     * 只查询sum(column)
     * @param column 字段
     * @return children
     * @see com.qingyu.mo.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    Children selectSumOne(R column);

    /**
     * 只查询sum(column)
     * @param column 字段
     * @return children
     * @see com.qingyu.mo.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    <J> Children jSelectSumOne(SFunction<J, ?> column);

    /**
     * 只查询sum(column) + sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @return children
     * @see com.qingyu.mo.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    Children selectSumAddOne(R column, R column2);

    /**
     * 只查询sum(column) + sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @return children
     * @see com.qingyu.mo.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    <J> Children selectSumAddJOne(R column, SFunction<J, ?> column2);

    /**
     * 只查询sum(column) + sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @return children
     * @see com.qingyu.mo.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    <J> Children jSelectSumAddOne(SFunction<J, ?> column, R column2);

    /**
     * 只查询sum(column) + sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @return children
     * @see com.qingyu.mo.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    <J> Children jSelectSumAddJOne(SFunction<J, ?> column, SFunction<J, ?> column2);

    /**
     * 只查询sum(column) - sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @return children
     * @see com.qingyu.mo.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    Children selectSumSubOne(R column, R column2);

    /**
     * 只查询sum(column) - sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @return children
     * @see com.qingyu.mo.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    <J> Children selectSumSubJOne(R column, SFunction<J, ?> column2);

    /**
     * 只查询sum(column) - sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @return children
     * @see com.qingyu.mo.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    <J> Children jSelectSumSubOne(SFunction<J, ?> column, R column2);

    /**
     * 只查询sum(column) - sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @return children
     * @see com.qingyu.mo.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    <J> Children jSelectSumSubJOne(SFunction<J, ?> column, SFunction<J, ?> column2);

    /**
     * 只查询max(column)
     * @param column 字段
     * @return children
     * @see com.qingyu.mo.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    Children selectMaxOne(R column);

    /**
     * 只查询max(column)
     * @param column 字段
     * @return children
     * @see com.qingyu.mo.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    <J> Children jSelectMaxOne(SFunction<J, ?> column);

    /**
     * sum(column)
     * @param column 字段
     * @return children
     */
    default Children sum(R column) {
        return sum(true, column);
    }

    /**
     * sum(column)
     * @param condition 执行条件
     * @param column 字段
     * @return children
     */
    default Children sum(boolean condition, R column) {
        return sum(condition, column, null);
    }

    /**
     * sum(column)
     * @param column 字段
     * @param aliasField 别名字段
     * @return children
     */
    default Children sum(R column, SFunction<T, ?> aliasField) {
        return sum(column, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * sum(column)
     * @param column 字段
     * @param alias 别名
     * @return children
     */
    default Children sum(R column, String alias) {
        return sum(true, column, alias);
    }

    /**
     * sum(column)
     * @param condition 执行条件
     * @param column 字段
     * @param alias 别名
     * @return children
     */
    default Children sum(boolean condition, R column, String alias) {
        return sum(condition, false, column, alias);
    }

    /**
     * IFNULL(sum(column), 0)
     * @param column 字段
     * @return children
     */
    default Children sumIfNull(R column) {
        return sumIfNull(true, column);
    }

    /**
     * IFNULL(sum(column), 0)
     * @param condition 执行条件
     * @param column 字段
     * @return children
     */
    default Children sumIfNull(boolean condition, R column) {
        return sumIfNull(condition, column, null);
    }

    /**
     * IFNULL(sum(column), 0)
     * @param column 字段
     * @param aliasField 别名字段
     * @return children
     */
    default Children sumIfNull(R column, SFunction<T, ?> aliasField) {
        return sumIfNull(column, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * IFNULL(sum(column), 0)
     * @param column 字段
     * @param alias 别名
     * @return children
     */
    default Children sumIfNull(R column, String alias) {
        return sumIfNull(true, column, alias);
    }

    /**
     * IFNULL(sum(column), 0)
     * @param condition 执行条件
     * @param column 字段
     * @param alias 别名
     * @return children
     */
    default Children sumIfNull(boolean condition, R column, String alias) {
        return sum(condition, true, column, alias);
    }

    /**
     * 执行sum函数
     * @param condition 执行条件
     * @param needIfNull 是否用IFNULL包裹
     * @param column 字段
     * @param alias 别名
     * @return children
     */
    Children sum(boolean condition, boolean needIfNull, R column, String alias);

    /**
     * sum(column)
     * @param column 字段
     * @return children
     */
    default <J> Children jSum(SFunction<J, ?> column) {
        return jSum(true, column);
    }

    /**
     * sum(column)
     * @param condition 执行条件
     * @param column 字段
     * @return children
     */
    default <J> Children jSum(boolean condition, SFunction<J, ?> column) {
        return jSum(condition, column, null);
    }

    /**
     * sum(column)
     * @param column 字段
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children jSum(SFunction<J, ?> column, SFunction<T, ?> aliasField) {
        return jSum(column, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * sum(column)
     * @param column 字段
     * @param alias 别名
     * @return children
     */
    default <J> Children jSum(SFunction<J, ?> column, String alias) {
        return jSum(true, column, alias);
    }

    /**
     * sum(column)
     * @param condition 执行条件
     * @param column 字段
     * @param alias 别名
     * @return children
     */
    default <J> Children jSum(boolean condition, SFunction<J, ?> column, String alias) {
        return jSum(condition, false, column, alias);
    }

    /**
     * IFNULL(sum(column), 0)
     * @param column 字段
     * @return children
     */
    default <J> Children jSumIfNull(SFunction<J, ?> column) {
        return jSumIfNull(true, column);
    }

    /**
     * IFNULL(sum(column), 0)
     * @param condition 执行条件
     * @param column 字段
     * @return children
     */
    default <J> Children jSumIfNull(boolean condition, SFunction<J, ?> column) {
        return jSumIfNull(condition, column, null);
    }

    /**
     * IFNULL(sum(column), 0)
     * @param column 字段
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children jSumIfNull(SFunction<J, ?> column, SFunction<T, ?> aliasField) {
        return jSumIfNull(column, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * IFNULL(sum(column), 0)
     * @param column 字段
     * @param alias 别名
     * @return children
     */
    default <J> Children jSumIfNull(SFunction<J, ?> column, String alias) {
        return jSumIfNull(true, column, alias);
    }

    /**
     * IFNULL(sum(column), 0)
     * @param condition 执行条件
     * @param column 字段
     * @param alias 别名
     * @return children
     */
    default <J> Children jSumIfNull(boolean condition, SFunction<J, ?> column, String alias) {
        return jSum(condition, true, column, alias);
    }

    /**
     * 执行sum函数
     * @param condition 执行条件
     * @param needIfNull 是否用IFNULL包裹
     * @param column 字段
     * @param alias 别名
     * @return children
     */
    <J> Children jSum(boolean condition, boolean needIfNull, SFunction<J, ?> column, String alias);

    /**
     * sum(column) + sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @param aliasField 别名字段
     * @return children
     */
    default Children sumAdd(R column, R column2, SFunction<T, ?> aliasField) {
        return sumAdd(true, column, column2, aliasField);
    }

    /**
     * sum(column) + sum(column2)
     * @param condition 执行条件
     * @param column 被减数
     * @param column2 减数
     * @param aliasField 别名字段
     * @return children
     */
    default Children sumAdd(boolean condition, R column, R column2, SFunction<T, ?> aliasField) {
        return sumAdd(condition, column, column2, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * sum(column) + sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    default Children sumAdd(R column, R column2, String alias) {
        return sumAdd(true, column, column2, alias);
    }

    /**
     * sum(column) + sum(column2)
     * @param condition 执行条件
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    default Children sumAdd(boolean condition, R column, R column2, String alias) {
        return sumAdd(condition, false, column, column2, alias);
    }

    /**
     * IFNULL(sum(column) + sum(column2), 0)
     * @param column 被减数
     * @param column2 减数
     * @param aliasField 别名字段
     * @return children
     */
    default Children sumAddIfNull(R column, R column2, SFunction<T, ?> aliasField) {
        return sumAddIfNull(true, column, column2, aliasField);
    }

    /**
     * IFNULL(sum(column) + sum(column2), 0)
     * @param condition 执行条件
     * @param column 被减数
     * @param column2 减数
     * @param aliasField 别名字段
     * @return children
     */
    default Children sumAddIfNull(boolean condition, R column, R column2, SFunction<T, ?> aliasField) {
        return sumAddIfNull(condition, column, column2, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * IFNULL(sum(column) + sum(column2), 0)
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    default Children sumAddIfNull(R column, R column2, String alias) {
        return sumAddIfNull(true, column, column2, alias);
    }

    /**
     * IFNULL(sum(column) + sum(column2), 0)
     * @param condition 执行条件
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    default Children sumAddIfNull(boolean condition, R column, R column2, String alias) {
        return sumAdd(condition, true, column, column2, alias);
    }

    /**
     * 执行sum函数相加
     * @param condition 执行条件
     * @param needIfNull 是否用IFNULL包裹
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    Children sumAdd(boolean condition, boolean needIfNull, R column, R column2, String alias);

    /**
     * sum(column) + sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children sumAddJ(R column, SFunction<J, ?> column2, SFunction<T, ?> aliasField) {
        return sumAddJ(true, column, column2, aliasField);
    }

    /**
     * sum(column) + sum(column2)
     * @param condition 执行条件
     * @param column 被减数
     * @param column2 减数
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children sumAddJ(boolean condition, R column, SFunction<J, ?> column2, SFunction<T, ?> aliasField) {
        return sumAddJ(condition, column, column2, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * sum(column) + sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    default <J> Children sumAddJ(R column, SFunction<J, ?> column2, String alias) {
        return sumAddJ(true, column, column2, alias);
    }

    /**
     * sum(column) + sum(column2)
     * @param condition 执行条件
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    default <J> Children sumAddJ(boolean condition, R column, SFunction<J, ?> column2, String alias) {
        return sumAddJ(condition, false, column, column2, alias);
    }

    /**
     * IFNULL(sum(column) + sum(column2), 0)
     * @param column 被减数
     * @param column2 减数
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children sumAddJIfNull(R column, SFunction<J, ?> column2, SFunction<T, ?> aliasField) {
        return sumAddJIfNull(true, column, column2, aliasField);
    }

    /**
     * IFNULL(sum(column) + sum(column2), 0)
     * @param condition 执行条件
     * @param column 被减数
     * @param column2 减数
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children sumAddJIfNull(boolean condition, R column, SFunction<J, ?> column2, SFunction<T, ?> aliasField) {
        return sumAddJIfNull(condition, column, column2, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * IFNULL(sum(column) + sum(column2), 0)
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    default <J> Children sumAddJIfNull(R column, SFunction<J, ?> column2, String alias) {
        return sumAddJIfNull(true, column, column2, alias);
    }

    /**
     * IFNULL(sum(column) + sum(column2), 0)
     * @param condition 执行条件
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    default <J> Children sumAddJIfNull(boolean condition, R column, SFunction<J, ?> column2, String alias) {
        return sumAddJ(condition, true, column, column2, alias);
    }

    /**
     * 执行sum函数相加
     * @param condition 执行条件
     * @param needIfNull 是否用IFNULL包裹
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    <J> Children sumAddJ(boolean condition, boolean needIfNull, R column, SFunction<J, ?> column2, String alias);

    /**
     * sum(column) - sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @param aliasField 别名字段
     * @return children
     */
    default Children sumSub(R column, R column2, SFunction<T, ?> aliasField) {
        return sumSub(true, column, column2, aliasField);
    }

    /**
     * sum(column) - sum(column2)
     * @param condition 执行条件
     * @param column 被减数
     * @param column2 减数
     * @param aliasField 别名字段
     * @return children
     */
    default Children sumSub(boolean condition, R column, R column2, SFunction<T, ?> aliasField) {
        return sumSub(condition, column, column2, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * sum(column) - sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    default Children sumSub(R column, R column2, String alias) {
        return sumSub(true, column, column2, alias);
    }

    /**
     * sum(column) - sum(column2)
     * @param condition 执行条件
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    default Children sumSub(boolean condition, R column, R column2, String alias) {
        return sumSub(condition, false, column, column2, alias);
    }

    /**
     * sum(column) - sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @param aliasField 别名字段
     * @return children
     */
    default Children sumSubIfNull(R column, R column2, SFunction<T, ?> aliasField) {
        return sumSubIfNull(true, column, column2, aliasField);
    }

    /**
     * sum(column) - sum(column2)
     * @param condition 执行条件
     * @param column 被减数
     * @param column2 减数
     * @param aliasField 别名字段
     * @return children
     */
    default Children sumSubIfNull(boolean condition, R column, R column2, SFunction<T, ?> aliasField) {
        return sumSubIfNull(condition, column, column2, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * sum(column) - sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    default Children sumSubIfNull(R column, R column2, String alias) {
        return sumSubIfNull(true, column, column2, alias);
    }

    /**
     * IFNULL(sum(column) - sum(column2), 0)
     * @param condition 执行条件
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    default Children sumSubIfNull(boolean condition, R column, R column2, String alias) {
        return sumSub(condition, true, column, column2, alias);
    }

    /**
     * 执行sum函数相减
     * @param condition 执行条件
     * @param needIfNull 是否用IFNULL包裹
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    Children sumSub(boolean condition, boolean needIfNull, R column, R column2, String alias);

    /**
     * sum(column) - sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children sumSubJ(R column, SFunction<J, ?> column2, SFunction<T, ?> aliasField) {
        return sumSubJ(true, column, column2, aliasField);
    }

    /**
     * sum(column) - sum(column2)
     * @param condition 执行条件
     * @param column 被减数
     * @param column2 减数
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children sumSubJ(boolean condition, R column, SFunction<J, ?> column2, SFunction<T, ?> aliasField) {
        return sumSubJ(condition, column, column2, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * sum(column) - sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    default <J> Children sumSubJ(R column, SFunction<J, ?> column2, String alias) {
        return sumSubJ(true, column, column2, alias);
    }

    /**
     * sum(column) - sum(column2)
     * @param condition 执行条件
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    default <J> Children sumSubJ(boolean condition, R column, SFunction<J, ?> column2, String alias) {
        return sumSubJ(condition, false, column, column2, alias);
    }

    /**
     * IFNULL(sum(column) - sum(column2), 0)
     * @param column 被减数
     * @param column2 减数
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children sumSubJIfNull(R column, SFunction<J, ?> column2, SFunction<T, ?> aliasField) {
        return sumSubJIfNull(true, column, column2, aliasField);
    }

    /**
     * IFNULL(sum(column) - sum(column2), 0)
     * @param condition 执行条件
     * @param column 被减数
     * @param column2 减数
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children sumSubJIfNull(boolean condition, R column, SFunction<J, ?> column2, SFunction<T, ?> aliasField) {
        return sumSubJIfNull(condition, column, column2, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * IFNULL(sum(column) - sum(column2), 0)
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    default <J> Children sumSubJIfNull(R column, SFunction<J, ?> column2, String alias) {
        return sumSubJIfNull(true, column, column2, alias);
    }

    /**
     * IFNULL(sum(column) - sum(column2), 0)
     * @param condition 执行条件
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    default <J> Children sumSubJIfNull(boolean condition, R column, SFunction<J, ?> column2, String alias) {
        return sumSubJ(condition, true, column, column2, alias);
    }

    /**
     * 执行sum函数相减
     * @param condition 执行条件
     * @param needIfNull 是否用IFNULL包裹
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    <J> Children sumSubJ(boolean condition, boolean needIfNull, R column, SFunction<J, ?> column2, String alias);

    /**
     * sum(column) + sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children jSumAdd(SFunction<J, ?> column, R column2, SFunction<T, ?> aliasField) {
        return jSumAdd(true, column, column2, aliasField);
    }

    /**
     * sum(column) + sum(column2)
     * @param condition 执行条件
     * @param column 被减数
     * @param column2 减数
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children jSumAdd(boolean condition, SFunction<J, ?> column, R column2, SFunction<T, ?> aliasField) {
        return jSumAdd(condition, column, column2, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * sum(column) + sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    default <J> Children jSumAdd(SFunction<J, ?> column, R column2, String alias) {
        return jSumAdd(true, column, column2, alias);
    }

    /**
     * sum(column) + sum(column2)
     * @param condition 执行条件
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    default <J> Children jSumAdd(boolean condition, SFunction<J, ?> column, R column2, String alias) {
        return jSumAdd(condition, false, column, column2, alias);
    }

    /**
     * IFNULL(sum(column) + sum(column2), 0)
     * @param column 被减数
     * @param column2 减数
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children jSumAddIfNull(SFunction<J, ?> column, R column2, SFunction<T, ?> aliasField) {
        return jSumAddIfNull(true, column, column2, aliasField);
    }

    /**
     * IFNULL(sum(column) + sum(column2), 0)
     * @param condition 执行条件
     * @param column 被减数
     * @param column2 减数
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children jSumAddIfNull(boolean condition, SFunction<J, ?> column, R column2, SFunction<T, ?> aliasField) {
        return jSumAddIfNull(condition, column, column2, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * IFNULL(sum(column) + sum(column2), 0)
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    default <J> Children jSumAddIfNull(SFunction<J, ?> column, R column2, String alias) {
        return jSumAddIfNull(true, column, column2, alias);
    }

    /**
     * IFNULL(sum(column) + sum(column2), 0)
     * @param condition 执行条件
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    default <J> Children jSumAddIfNull(boolean condition, SFunction<J, ?> column, R column2, String alias) {
        return jSumAdd(condition, true, column, column2, alias);
    }

    /**
     * 执行sum函数相加
     * @param condition 执行条件
     * @param needIfNull 是否用IFNULL包裹
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    <J> Children jSumAdd(boolean condition, boolean needIfNull, SFunction<J, ?> column, R column2, String alias);

    /**
     * sum(column) + sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children jSumAddJ(SFunction<J, ?> column, SFunction<J, ?> column2, SFunction<T, ?> aliasField) {
        return jSumAddJ(true, column, column2, aliasField);
    }

    /**
     * sum(column) + sum(column2)
     * @param condition 执行条件
     * @param column 被减数
     * @param column2 减数
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children jSumAddJ(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2, SFunction<T, ?> aliasField) {
        return jSumAddJ(condition, column, column2, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * sum(column) + sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    default <J> Children jSumAddJ(SFunction<J, ?> column, SFunction<J, ?> column2, String alias) {
        return jSumAddJ(true, column, column2, alias);
    }

    /**
     * sum(column) + sum(column2)
     * @param condition 执行条件
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    default <J> Children jSumAddJ(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2, String alias) {
        return jSumAddJ(condition, false, column, column2, alias);
    }

    /**
     * IFNULL(sum(column) + sum(column2), 0)
     * @param column 被减数
     * @param column2 减数
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children jSumAddJIfNull(SFunction<J, ?> column, SFunction<J, ?> column2, SFunction<T, ?> aliasField) {
        return jSumAddJIfNull(true, column, column2, aliasField);
    }

    /**
     * IFNULL(sum(column) + sum(column2), 0)
     * @param condition 执行条件
     * @param column 被减数
     * @param column2 减数
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children jSumAddJIfNull(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2, SFunction<T, ?> aliasField) {
        return jSumAddJIfNull(condition, column, column2, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * IFNULL(sum(column) + sum(column2), 0)
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    default <J> Children jSumAddJIfNull(SFunction<J, ?> column, SFunction<J, ?> column2, String alias) {
        return jSumAddJIfNull(true, column, column2, alias);
    }

    /**
     * IFNULL(sum(column) + sum(column2), 0)
     * @param condition 执行条件
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    default <J> Children jSumAddJIfNull(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2, String alias) {
        return jSumAddJ(condition, true, column, column2, alias);
    }

    /**
     * 执行sum函数相加
     * @param condition 执行条件
     * @param needIfNull 是否用IFNULL包裹
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    <J> Children jSumAddJ(boolean condition, boolean needIfNull, SFunction<J, ?> column, SFunction<J, ?> column2, String alias);

    /**
     * sum(column) - sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children jSumSub(SFunction<J, ?> column, R column2, SFunction<T, ?> aliasField) {
        return jSumSub(true, column, column2, aliasField);
    }

    /**
     * sum(column) - sum(column2)
     * @param condition 执行条件
     * @param column 被减数
     * @param column2 减数
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children jSumSub(boolean condition, SFunction<J, ?> column, R column2, SFunction<T, ?> aliasField) {
        return jSumSub(condition, column, column2, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * sum(column) - sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    default <J> Children jSumSub(SFunction<J, ?> column, R column2, String alias) {
        return jSumSub(true, column, column2, alias);
    }

    /**
     * sum(column) - sum(column2)
     * @param condition 执行条件
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    default <J> Children jSumSub(boolean condition, SFunction<J, ?> column, R column2, String alias) {
        return jSumSub(condition, false, column, column2, alias);
    }

    /**
     * IFNULL(sum(column) - sum(column2), 0)
     * @param column 被减数
     * @param column2 减数
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children jSumSubIfNull(SFunction<J, ?> column, R column2, SFunction<T, ?> aliasField) {
        return jSumSubIfNull(true, column, column2, aliasField);
    }

    /**
     * IFNULL(sum(column) - sum(column2), 0)
     * @param condition 执行条件
     * @param column 被减数
     * @param column2 减数
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children jSumSubIfNull(boolean condition, SFunction<J, ?> column, R column2, SFunction<T, ?> aliasField) {
        return jSumSubIfNull(condition, column, column2, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * IFNULL(sum(column) - sum(column2), 0)
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    default <J> Children jSumSubIfNull(SFunction<J, ?> column, R column2, String alias) {
        return jSumSubIfNull(true, column, column2, alias);
    }

    /**
     * IFNULL(sum(column) - sum(column2), 0)
     * @param condition 执行条件
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    default <J> Children jSumSubIfNull(boolean condition, SFunction<J, ?> column, R column2, String alias) {
        return jSumSub(condition, true, column, column2, alias);
    }

    /**
     * 执行sum函数相减
     * @param condition 执行条件
     * @param needIfNull 是否用IFNULL包裹
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    <J> Children jSumSub(boolean condition, boolean needIfNull, SFunction<J, ?> column, R column2, String alias);

    /**
     * sum(column) - sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children jSumSubJ(SFunction<J, ?> column, SFunction<J, ?> column2, SFunction<T, ?> aliasField) {
        return jSumSubJ(true, column, column2, aliasField);
    }

    /**
     * sum(column) - sum(column2)
     * @param condition 执行条件
     * @param column 被减数
     * @param column2 减数
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children jSumSubJ(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2, SFunction<T, ?> aliasField) {
        return jSumSubJ(condition, column, column2, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * sum(column) - sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    default <J> Children jSumSubJ(SFunction<J, ?> column, SFunction<J, ?> column2, String alias) {
        return jSumSubJ(true, column, column2, alias);
    }

    /**
     * sum(column) - sum(column2)
     * @param condition 执行条件
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    default <J> Children jSumSubJ(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2, String alias) {
        return jSumSubJ(condition, false, column, column2, alias);
    }

    /**
     * IFNULL(sum(column) - sum(column2), 0)
     * @param column 被减数
     * @param column2 减数
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children jSumSubJIfNull(SFunction<J, ?> column, SFunction<J, ?> column2, SFunction<T, ?> aliasField) {
        return jSumSubJIfNull(true, column, column2, aliasField);
    }

    /**
     * IFNULL(sum(column) - sum(column2), 0)
     * @param condition 执行条件
     * @param column 被减数
     * @param column2 减数
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children jSumSubJIfNull(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2, SFunction<T, ?> aliasField) {
        return jSumSubJIfNull(condition, column, column2, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * IFNULL(sum(column) - sum(column2), 0)
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    default <J> Children jSumSubJIfNull(SFunction<J, ?> column, SFunction<J, ?> column2, String alias) {
        return jSumSubJIfNull(true, column, column2, alias);
    }

    /**
     * IFNULL(sum(column) - sum(column2), 0)
     * @param condition 执行条件
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    default <J> Children jSumSubJIfNull(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2, String alias) {
        return jSumSubJ(condition, true, column, column2, alias);
    }

    /**
     * 执行sum函数相减
     * @param condition 执行条件
     * @param needIfNull 是否用IFNULL包裹
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     */
    <J> Children jSumSubJ(boolean condition, boolean needIfNull, SFunction<J, ?> column, SFunction<J, ?> column2, String alias);

    /**
     * ifNull(column, column2)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default Children ifNull(R column, R column2) {
        return ifNull(true, column, column2);
    }

    /**
     * ifNull(column, column2)
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    Children ifNull(boolean condition, R column, R column2);

    /**
     * ifNull(column, column2)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children ifNullJ(R column, SFunction<J, ?> column2) {
        return ifNullJ(true, column, column2);
    }

    /**
     * ifNull(column, column2)
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children ifNullJ(boolean condition, R column, SFunction<J, ?> column2);

    /**
     * ifNull(column, column2)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jIfNull(SFunction<J, ?> column, R column2) {
        return jIfNull(true, column, column2);
    }

    /**
     * ifNull(column, column2)
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jIfNull(boolean condition, SFunction<J, ?> column, R column2);

    /**
     * ifNull(column, column2)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jIfNullJ(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jIfNullJ(true, column, column2);
    }

    /**
     * ifNull(column, column2)
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jIfNullJ(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2);

    /**
     * if(consumer, trueValue, falseValue)
     * @param consumer 判断条件
     * @param trueValue 正确值
     * @param falseValue 错误值
     * @param aliasField 别名字段
     * @return children
     */
    default Children ifTo(Consumer<Children> consumer, Object trueValue, Object falseValue, SFunction<T, ?> aliasField) {
        return ifTo(true, consumer, trueValue, falseValue, aliasField);
    }

    /**
     * if(consumer, trueValue, falseValue)
     * @param condition 执行条件
     * @param consumer 判断条件
     * @param trueValue 正确值
     * @param falseValue 错误值
     * @param aliasField 别名字段
     * @return children
     */
    default Children ifTo(boolean condition, Consumer<Children> consumer, Object trueValue, Object falseValue, SFunction<T, ?> aliasField) {
        return ifTo(condition, consumer, trueValue, falseValue, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * if(consumer, trueValue, falseValue)
     * @param consumer 判断条件
     * @param trueValue 正确值
     * @param falseValue 错误值
     * @param alias 别名
     * @return children
     */
    default Children ifTo(Consumer<Children> consumer, Object trueValue, Object falseValue, String alias) {
        return ifTo(true, consumer, trueValue, falseValue, alias);
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
    Children ifTo(boolean condition, Consumer<Children> consumer, Object trueValue, Object falseValue, String alias);

    /**
     * if(column, trueValue, falseValue)
     * @param column 字段
     * @param trueValue 正确值
     * @param falseValue 错误值
     * @param aliasField 别名字段
     * @return children
     */
    default Children ifTo(R column, Object trueValue, Object falseValue, SFunction<T, ?> aliasField) {
        return ifTo(true, column, trueValue, falseValue, aliasField);
    }

    /**
     * if(column, trueValue, falseValue)
     * @param condition 执行条件
     * @param column 字段
     * @param trueValue 正确值
     * @param falseValue 错误值
     * @param aliasField 别名字段
     * @return children
     */
    default Children ifTo(boolean condition, R column, Object trueValue, Object falseValue, SFunction<T, ?> aliasField) {
        return ifTo(condition, column, trueValue, falseValue, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * if(column, trueValue, falseValue)
     * @param column 字段
     * @param trueValue 正确值
     * @param falseValue 错误值
     * @param alias 别名
     * @return children
     */
    default Children ifTo(R column, Object trueValue, Object falseValue, String alias) {
        return ifTo(true, column, trueValue, falseValue, alias);
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
    Children ifTo(boolean condition, R column, Object trueValue, Object falseValue, String alias);

    /**
     * if(column, trueValue, falseValue)
     * @param column 字段
     * @param trueValue 正确值
     * @param falseValue 错误值
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children jIfTo(SFunction<J, ?> column, Object trueValue, Object falseValue, SFunction<T, ?> aliasField) {
        return jIfTo(true, column, trueValue, falseValue, aliasField);
    }

    /**
     * if(column, trueValue, falseValue)
     * @param condition 执行条件
     * @param column 字段
     * @param trueValue 正确值
     * @param falseValue 错误值
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children jIfTo(boolean condition, SFunction<J, ?> column, Object trueValue, Object falseValue, SFunction<T, ?> aliasField) {
        return jIfTo(condition, column, trueValue, falseValue, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * if函数
     * @param column 字段
     * @param trueValue 正确值
     * @param falseValue 错误值
     * @param alias 别名
     * @return children
     */
    default <J> Children jIfTo(SFunction<J, ?> column, Object trueValue, Object falseValue, String alias) {
        return jIfTo(true, column, trueValue, falseValue, alias);
    }

    /**
     * if函数
     * @param condition 执行条件
     * @param column 字段
     * @param trueValue 正确值
     * @param falseValue 错误值
     * @param alias 别名
     * @return children
     */
    <J> Children jIfTo(boolean condition, SFunction<J, ?> column, Object trueValue, Object falseValue, String alias);
}