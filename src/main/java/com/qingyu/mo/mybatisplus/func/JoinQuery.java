package com.qingyu.mo.mybatisplus.func;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.qingyu.mo.mybatisplus.entity.Search;
import com.qingyu.mo.mybatisplus.utils.LambdaUtil;
import com.qingyu.mo.mybatisplus.wrapper.query.JoinLambdaQueryWrapper;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

/**
 * <p>
 * join查询扩展方法
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.6.2
 */
@SuppressWarnings("unused")
public interface JoinQuery<Children, T, R> extends Serializable {

    /**
     * 链式调用，查询不加租户条件，自行使用
     * @return children
     */
    default Children noCarrier() {
        return noCarrier(true);
    }

    /**
     * 链式调用，查询不加租户条件，自行使用
     * @param condition 执行条件
     * @return children
     */
    Children noCarrier(boolean condition);

    /**
     * .last("LIMIT 1")
     * @return children
     */
    default Children justOne() {
        return justOne(true);
    }

    /**
     * .last("LIMIT 1")
     * @param condition 执行条件
     * @return children
     */
    Children justOne(boolean condition);

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
    default <J> Children jSelect(SFunction<J, ?>... columns) {
        return jSelect(true, columns);
    }

    /**
     * join查询字段
     * @param condition 执行条件
     * @param columns 需要查询的关联的表字段
     * @return children
     */
    @SuppressWarnings("unchecked")
    <J> Children jSelect(boolean condition, SFunction<J, ?>... columns);

    /**
     * join查询字段
     * @param column 需要查询的关联的表字段
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children jSelect(SFunction<J, ?> column, SFunction<T, ?> aliasField) {
        return jSelect(true, column, aliasField);
    }

    /**
     * join查询字段
     * @param condition 执行条件
     * @param column 需要查询的关联的表字段
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children jSelect(boolean condition, SFunction<J, ?> column, SFunction<T, ?> aliasField) {
        return jSelect(condition, column, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * join查询字段
     * <p>内部有 entity 才能使用该方法</p>
     * @param column 需要查询的关联的表字段
     * @param alias 别名
     * @return children
     */
    default <J> Children jSelect(SFunction<J, ?> column, String alias) {
        return jSelect(true, column, alias);
    }

    /**
     * join查询字段
     * <p>内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 需要查询的关联的表字段
     * @param alias 别名
     * @return children
     */
    <J> Children jSelect(boolean condition, SFunction<J, ?> column, String alias);

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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * @see com.qingyu.mo.mybatisplus.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    default Children selectSumOne(R column) {
        return selectSumOne(column, "sumValue");
    }

    /**
     * 只查询sum(column)
     * @param column 字段
     * @param alias 别名
     * @return children
     * @see com.qingyu.mo.mybatisplus.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    Children selectSumOne(R column, String alias);

    /**
     * 只查询sum(column)
     * @param column 字段
     * @return children
     * @see com.qingyu.mo.mybatisplus.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    default <J> Children jSelectSumOne(SFunction<J, ?> column) {
        return jSelectSumOne(column, "sumValue");
    }

    /**
     * 只查询sum(column)
     * @param column 字段
     * @param alias 别名
     * @return children
     * @see com.qingyu.mo.mybatisplus.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    <J> Children jSelectSumOne(SFunction<J, ?> column, String alias);

    /**
     * 只查询sum(column) + sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @return children
     * @see com.qingyu.mo.mybatisplus.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    default Children selectSumAddOne(R column, R column2) {
        return selectSumAddOne(column, column2, "sumValue");
    }

    /**
     * 只查询sum(column) + sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     * @see com.qingyu.mo.mybatisplus.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    Children selectSumAddOne(R column, R column2, String alias);

    /**
     * 只查询sum(column) + sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @return children
     * @see com.qingyu.mo.mybatisplus.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    default <J> Children selectSumAddJOne(R column, SFunction<J, ?> column2) {
        return selectSumAddJOne(column, column2, "sumValue");
    }

    /**
     * 只查询sum(column) + sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     * @see com.qingyu.mo.mybatisplus.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    <J> Children selectSumAddJOne(R column, SFunction<J, ?> column2, String alias);

    /**
     * 只查询sum(column) + sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @return children
     * @see com.qingyu.mo.mybatisplus.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    default <J> Children jSelectSumAddOne(SFunction<J, ?> column, R column2) {
        return jSelectSumAddOne(column, column2, "sumValue");
    }

    /**
     * 只查询sum(column) + sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @param alias 别名
     * @return children
     * @see com.qingyu.mo.mybatisplus.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    <J> Children jSelectSumAddOne(SFunction<J, ?> column, R column2, String alias);

    /**
     * 只查询sum(column) + sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @return children
     * @see com.qingyu.mo.mybatisplus.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    default <J, K> Children jSelectSumAddJOne(SFunction<J, ?> column, SFunction<K, ?> column2) {
        return jSelectSumAddJOne(column, column2, "sumValue");
    }

    /**
     * 只查询sum(column) + sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @return children
     * @see com.qingyu.mo.mybatisplus.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    <J, K> Children jSelectSumAddJOne(SFunction<J, ?> column, SFunction<K, ?> column2, String alias);

    /**
     * 只查询sum(column) - sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @return children
     * @see com.qingyu.mo.mybatisplus.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    default Children selectSumSubOne(R column, R column2) {
        return selectSumSubOne(column, column2, "sumValue");
    }

    /**
     * 只查询sum(column) - sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @return children
     * @see com.qingyu.mo.mybatisplus.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    Children selectSumSubOne(R column, R column2, String alias);

    /**
     * 只查询sum(column) - sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @return children
     * @see com.qingyu.mo.mybatisplus.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    default <J> Children selectSumSubJOne(R column, SFunction<J, ?> column2) {
        return selectSumSubJOne(column, column2, "sumValue");
    }

    /**
     * 只查询sum(column) - sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @return children
     * @see com.qingyu.mo.mybatisplus.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    <J> Children selectSumSubJOne(R column, SFunction<J, ?> column2, String alias);

    /**
     * 只查询sum(column) - sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @return children
     * @see com.qingyu.mo.mybatisplus.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    default <J> Children jSelectSumSubOne(SFunction<J, ?> column, R column2) {
        return jSelectSumSubOne(column, column2, "sumValue");
    }

    /**
     * 只查询sum(column) - sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @return children
     * @see com.qingyu.mo.mybatisplus.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    <J> Children jSelectSumSubOne(SFunction<J, ?> column, R column2, String alias);

    /**
     * 只查询sum(column) - sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @return children
     * @see com.qingyu.mo.mybatisplus.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    default <J> Children jSelectSumSubJOne(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jSelectSumSubJOne(column, column2, "sumValue");
    }

    /**
     * 只查询sum(column) - sum(column2)
     * @param column 被减数
     * @param column2 减数
     * @return children
     * @see com.qingyu.mo.mybatisplus.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    <J> Children jSelectSumSubJOne(SFunction<J, ?> column, SFunction<J, ?> column2, String alias);

    /**
     * 只查询max(column)
     * @param column 字段
     * @return children
     * @see com.qingyu.mo.mybatisplus.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    default Children selectMaxOne(R column) {
        return selectMaxOne(column, "maxOne");
    }

    /**
     * 只查询max(column)
     * @param column 字段
     * @param alias 别名
     * @return children
     * @see com.qingyu.mo.mybatisplus.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    Children selectMaxOne(R column, String alias);

    /**
     * 只查询max(column)
     * @param column 字段
     * @return children
     * @see com.qingyu.mo.mybatisplus.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    default <J> Children jSelectMaxOne(SFunction<J, ?> column) {
        return jSelectMaxOne(column, "maxOne");
    }

    /**
     * 只查询max(column)
     * @param column 字段
     * @param alias 别名
     * @return children
     * @see com.qingyu.mo.mybatisplus.mapper.BaseMapperPlus#getMapValue
     * 配合一起使用
     */
    <J> Children jSelectMaxOne(SFunction<J, ?> column, String alias);

    /**
     * sum(column)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 字段
     * @return children
     */
    default Children sum(R column) {
        return sum(true, column);
    }

    /**
     * sum(column)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 字段
     * @return children
     */
    default Children sum(boolean condition, R column) {
        return sum(condition, column, null);
    }

    /**
     * sum(column)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 字段
     * @param aliasField 别名字段
     * @return children
     */
    default Children sum(R column, SFunction<T, ?> aliasField) {
        return sum(column, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * sum(column)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 字段
     * @param alias 别名
     * @return children
     */
    default Children sum(R column, String alias) {
        return sum(true, column, alias);
    }

    /**
     * sum(column)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 字段
     * @return children
     */
    default Children sumIfNull(R column) {
        return sumIfNull(true, column);
    }

    /**
     * IFNULL(sum(column), 0)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 字段
     * @return children
     */
    default Children sumIfNull(boolean condition, R column) {
        return sumIfNull(condition, column, null);
    }

    /**
     * IFNULL(sum(column), 0)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 字段
     * @param aliasField 别名字段
     * @return children
     */
    default Children sumIfNull(R column, SFunction<T, ?> aliasField) {
        return sumIfNull(column, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * IFNULL(sum(column), 0)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 字段
     * @param alias 别名
     * @return children
     */
    default Children sumIfNull(R column, String alias) {
        return sumIfNull(true, column, alias);
    }

    /**
     * IFNULL(sum(column), 0)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param needIfNull 是否用IFNULL包裹
     * @param column 字段
     * @param alias 别名
     * @return children
     */
    Children sum(boolean condition, boolean needIfNull, R column, String alias);

    /**
     * sum(column)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 字段
     * @return children
     */
    default <J> Children jSum(SFunction<J, ?> column) {
        return jSum(true, column);
    }

    /**
     * sum(column)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 字段
     * @return children
     */
    default <J> Children jSum(boolean condition, SFunction<J, ?> column) {
        return jSum(condition, column, null);
    }

    /**
     * sum(column)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 字段
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children jSum(SFunction<J, ?> column, SFunction<T, ?> aliasField) {
        return jSum(column, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * sum(column)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 字段
     * @param alias 别名
     * @return children
     */
    default <J> Children jSum(SFunction<J, ?> column, String alias) {
        return jSum(true, column, alias);
    }

    /**
     * sum(column)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 字段
     * @return children
     */
    default <J> Children jSumIfNull(SFunction<J, ?> column) {
        return jSumIfNull(true, column);
    }

    /**
     * IFNULL(sum(column), 0)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 字段
     * @return children
     */
    default <J> Children jSumIfNull(boolean condition, SFunction<J, ?> column) {
        return jSumIfNull(condition, column, null);
    }

    /**
     * IFNULL(sum(column), 0)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 字段
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children jSumIfNull(SFunction<J, ?> column, SFunction<T, ?> aliasField) {
        return jSumIfNull(column, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * IFNULL(sum(column), 0)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 字段
     * @param alias 别名
     * @return children
     */
    default <J> Children jSumIfNull(SFunction<J, ?> column, String alias) {
        return jSumIfNull(true, column, alias);
    }

    /**
     * IFNULL(sum(column), 0)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param needIfNull 是否用IFNULL包裹
     * @param column 字段
     * @param alias 别名
     * @return children
     */
    <J> Children jSum(boolean condition, boolean needIfNull, SFunction<J, ?> column, String alias);

    /**
     * sum(column) + sum(column2)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 被加数
     * @param column2 加数
     * @param aliasField 别名字段
     * @return children
     */
    default Children sumAdd(R column, R column2, SFunction<T, ?> aliasField) {
        return sumAdd(true, column, column2, aliasField);
    }

    /**
     * sum(column) + sum(column2)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 被加数
     * @param column2 加数
     * @param aliasField 别名字段
     * @return children
     */
    default Children sumAdd(boolean condition, R column, R column2, SFunction<T, ?> aliasField) {
        return sumAdd(condition, column, column2, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * sum(column) + sum(column2)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 被加数
     * @param column2 加数
     * @param alias 别名
     * @return children
     */
    default Children sumAdd(R column, R column2, String alias) {
        return sumAdd(true, column, column2, alias);
    }

    /**
     * sum(column) + sum(column2)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 被加数
     * @param column2 加数
     * @param alias 别名
     * @return children
     */
    default Children sumAdd(boolean condition, R column, R column2, String alias) {
        return sumAdd(condition, false, column, column2, alias);
    }

    /**
     * IFNULL(sum(column) + sum(column2), 0)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 被加数
     * @param column2 加数
     * @param aliasField 别名字段
     * @return children
     */
    default Children sumAddIfNull(R column, R column2, SFunction<T, ?> aliasField) {
        return sumAddIfNull(true, column, column2, aliasField);
    }

    /**
     * IFNULL(sum(column) + sum(column2), 0)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 被加数
     * @param column2 加数
     * @param aliasField 别名字段
     * @return children
     */
    default Children sumAddIfNull(boolean condition, R column, R column2, SFunction<T, ?> aliasField) {
        return sumAddIfNull(condition, column, column2, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * IFNULL(sum(column) + sum(column2), 0)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 被加数
     * @param column2 加数
     * @param alias 别名
     * @return children
     */
    default Children sumAddIfNull(R column, R column2, String alias) {
        return sumAddIfNull(true, column, column2, alias);
    }

    /**
     * IFNULL(sum(column) + sum(column2), 0)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 被加数
     * @param column2 加数
     * @param alias 别名
     * @return children
     */
    default Children sumAddIfNull(boolean condition, R column, R column2, String alias) {
        return sumAdd(condition, true, column, column2, alias);
    }

    /**
     * 执行sum函数相加
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param needIfNull 是否用IFNULL包裹
     * @param column 被加数
     * @param column2 加数
     * @param alias 别名
     * @return children
     */
    Children sumAdd(boolean condition, boolean needIfNull, R column, R column2, String alias);

    /**
     * sum(column) + sum(column2)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 被加数
     * @param column2 加数
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children sumAddJ(R column, SFunction<J, ?> column2, SFunction<T, ?> aliasField) {
        return sumAddJ(true, column, column2, aliasField);
    }

    /**
     * sum(column) + sum(column2)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 被加数
     * @param column2 加数
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children sumAddJ(boolean condition, R column, SFunction<J, ?> column2, SFunction<T, ?> aliasField) {
        return sumAddJ(condition, column, column2, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * sum(column) + sum(column2)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 被加数
     * @param column2 加数
     * @param alias 别名
     * @return children
     */
    default <J> Children sumAddJ(R column, SFunction<J, ?> column2, String alias) {
        return sumAddJ(true, column, column2, alias);
    }

    /**
     * sum(column) + sum(column2)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 被加数
     * @param column2 加数
     * @param alias 别名
     * @return children
     */
    default <J> Children sumAddJ(boolean condition, R column, SFunction<J, ?> column2, String alias) {
        return sumAddJ(condition, false, column, column2, alias);
    }

    /**
     * IFNULL(sum(column) + sum(column2), 0)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 被加数
     * @param column2 加数
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children sumAddJIfNull(R column, SFunction<J, ?> column2, SFunction<T, ?> aliasField) {
        return sumAddJIfNull(true, column, column2, aliasField);
    }

    /**
     * IFNULL(sum(column) + sum(column2), 0)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 被加数
     * @param column2 加数
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children sumAddJIfNull(boolean condition, R column, SFunction<J, ?> column2, SFunction<T, ?> aliasField) {
        return sumAddJIfNull(condition, column, column2, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * IFNULL(sum(column) + sum(column2), 0)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 被加数
     * @param column2 加数
     * @param alias 别名
     * @return children
     */
    default <J> Children sumAddJIfNull(R column, SFunction<J, ?> column2, String alias) {
        return sumAddJIfNull(true, column, column2, alias);
    }

    /**
     * IFNULL(sum(column) + sum(column2), 0)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 被加数
     * @param column2 加数
     * @param alias 别名
     * @return children
     */
    default <J> Children sumAddJIfNull(boolean condition, R column, SFunction<J, ?> column2, String alias) {
        return sumAddJ(condition, true, column, column2, alias);
    }

    /**
     * 执行sum函数相加
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param needIfNull 是否用IFNULL包裹
     * @param column 被加数
     * @param column2 加数
     * @param alias 别名
     * @return children
     */
    <J> Children sumAddJ(boolean condition, boolean needIfNull, R column, SFunction<J, ?> column2, String alias);

    /**
     * sum(column) - sum(column2)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 被加数
     * @param column2 加数
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children jSumAdd(SFunction<J, ?> column, R column2, SFunction<T, ?> aliasField) {
        return jSumAdd(true, column, column2, aliasField);
    }

    /**
     * sum(column) + sum(column2)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 被加数
     * @param column2 加数
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children jSumAdd(boolean condition, SFunction<J, ?> column, R column2, SFunction<T, ?> aliasField) {
        return jSumAdd(condition, column, column2, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * sum(column) + sum(column2)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 被加数
     * @param column2 加数
     * @param alias 别名
     * @return children
     */
    default <J> Children jSumAdd(SFunction<J, ?> column, R column2, String alias) {
        return jSumAdd(true, column, column2, alias);
    }

    /**
     * sum(column) + sum(column2)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 被加数
     * @param column2 加数
     * @param alias 别名
     * @return children
     */
    default <J> Children jSumAdd(boolean condition, SFunction<J, ?> column, R column2, String alias) {
        return jSumAdd(condition, false, column, column2, alias);
    }

    /**
     * IFNULL(sum(column) + sum(column2), 0)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 被加数
     * @param column2 加数
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children jSumAddIfNull(SFunction<J, ?> column, R column2, SFunction<T, ?> aliasField) {
        return jSumAddIfNull(true, column, column2, aliasField);
    }

    /**
     * IFNULL(sum(column) + sum(column2), 0)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 被加数
     * @param column2 加数
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children jSumAddIfNull(boolean condition, SFunction<J, ?> column, R column2, SFunction<T, ?> aliasField) {
        return jSumAddIfNull(condition, column, column2, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * IFNULL(sum(column) + sum(column2), 0)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 被加数
     * @param column2 加数
     * @param alias 别名
     * @return children
     */
    default <J> Children jSumAddIfNull(SFunction<J, ?> column, R column2, String alias) {
        return jSumAddIfNull(true, column, column2, alias);
    }

    /**
     * IFNULL(sum(column) + sum(column2), 0)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 被加数
     * @param column2 加数
     * @param alias 别名
     * @return children
     */
    default <J> Children jSumAddIfNull(boolean condition, SFunction<J, ?> column, R column2, String alias) {
        return jSumAdd(condition, true, column, column2, alias);
    }

    /**
     * 执行sum函数相加
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param needIfNull 是否用IFNULL包裹
     * @param column 被加数
     * @param column2 加数
     * @param alias 别名
     * @return children
     */
    <J> Children jSumAdd(boolean condition, boolean needIfNull, SFunction<J, ?> column, R column2, String alias);

    /**
     * sum(column) + sum(column2)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 被加数
     * @param column2 加数
     * @param aliasField 别名字段
     * @return children
     */
    default <J, K> Children jSumAddJ(SFunction<J, ?> column, SFunction<K, ?> column2, SFunction<T, ?> aliasField) {
        return jSumAddJ(true, column, column2, aliasField);
    }

    /**
     * sum(column) + sum(column2)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 被加数
     * @param column2 加数
     * @param aliasField 别名字段
     * @return children
     */
    default <J, K> Children jSumAddJ(boolean condition, SFunction<J, ?> column, SFunction<K, ?> column2, SFunction<T, ?> aliasField) {
        return jSumAddJ(condition, column, column2, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * sum(column) + sum(column2)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 被加数
     * @param column2 加数
     * @param alias 别名
     * @return children
     */
    default <J, K> Children jSumAddJ(SFunction<J, ?> column, SFunction<K, ?> column2, String alias) {
        return jSumAddJ(true, column, column2, alias);
    }

    /**
     * sum(column) + sum(column2)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 被加数
     * @param column2 加数
     * @param alias 别名
     * @return children
     */
    default <J, K> Children jSumAddJ(boolean condition, SFunction<J, ?> column, SFunction<K, ?> column2, String alias) {
        return jSumAddJ(condition, false, column, column2, alias);
    }

    /**
     * IFNULL(sum(column) + sum(column2), 0)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 被加数
     * @param column2 加数
     * @param aliasField 别名字段
     * @return children
     */
    default <J, K> Children jSumAddJIfNull(SFunction<J, ?> column, SFunction<K, ?> column2, SFunction<T, ?> aliasField) {
        return jSumAddJIfNull(true, column, column2, aliasField);
    }

    /**
     * IFNULL(sum(column) + sum(column2), 0)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 被加数
     * @param column2 加数
     * @param aliasField 别名字段
     * @return children
     */
    default <J, K> Children jSumAddJIfNull(boolean condition, SFunction<J, ?> column, SFunction<K, ?> column2, SFunction<T, ?> aliasField) {
        return jSumAddJIfNull(condition, column, column2, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * IFNULL(sum(column) + sum(column2), 0)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 被加数
     * @param column2 加数
     * @param alias 别名
     * @return children
     */
    default <J, K> Children jSumAddJIfNull(SFunction<J, ?> column, SFunction<K, ?> column2, String alias) {
        return jSumAddJIfNull(true, column, column2, alias);
    }

    /**
     * IFNULL(sum(column) + sum(column2), 0)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 被加数
     * @param column2 加数
     * @param alias 别名
     * @return children
     */
    default <J, K> Children jSumAddJIfNull(boolean condition, SFunction<J, ?> column, SFunction<K, ?> column2, String alias) {
        return jSumAddJ(condition, true, column, column2, alias);
    }

    /**
     * 执行sum函数相加
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param needIfNull 是否用IFNULL包裹
     * @param column 被加数
     * @param column2 加数
     * @param alias 别名
     * @return children
     */
    <J, K> Children jSumAddJ(boolean condition, boolean needIfNull, SFunction<J, ?> column, SFunction<K, ?> column2, String alias);

    /**
     * sum(column) - sum(column2)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
     * <p>注意：只有内部有 entity 才能使用该方法</p>
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
    default Children ifNull(R column, SFunction<T, ?> column2) {
        return ifNull(true, column, column2);
    }

    /**
     * ifNull(column, column2)
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    Children ifNull(boolean condition, R column, SFunction<T, ?> column2);

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
    default <J> Children jIfNull(SFunction<J, ?> column, SFunction<T, ?> column2) {
        return jIfNull(true, column, column2);
    }

    /**
     * ifNull(column, column2)
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jIfNull(boolean condition, SFunction<J, ?> column, SFunction<T, ?> column2);

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
     * ifNull(column, val)
     * @param column 字段
     * @param val 值
     * @return children
     */
    default Children ifNull(SFunction<T, ?> column, Object val) {
        return ifNull(column, val, LambdaUtil.toUnderlinePropertyName(column));
    }

    /**
     * ifNull(column, val)
     * @param column 字段
     * @param val 值
     * @param aliasField 别名字段
     * @return children
     */
    default Children ifNull(SFunction<T, ?> column, Object val, SFunction<T, ?> aliasField) {
        return ifNull(column, val, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * ifNull(column, val)
     * @param column 字段
     * @param val 值
     * @return children
     */
    default Children ifNull(SFunction<T, ?> column, Object val, String alias) {
        return ifNull(true, column, val, alias);
    }

    /**
     * ifNull(column, val)
     * @param condition 执行条件
     * @param column 字段
     * @param val 字段
     * @return children
     */
    Children ifNull(boolean condition, SFunction<T, ?> column, Object val, String alias);

    /**
     * ifNull(column, val)
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jIfNull(SFunction<J, ?> column, Object val) {
        return jIfNull(column, val, LambdaUtil.toUnderlinePropertyName(column));
    }

    /**
     * ifNull(column, val)
     * @param column 字段
     * @param val 值
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children jIfNull(SFunction<J, ?> column, Object val, SFunction<T, ?> aliasField) {
        return jIfNull(column, val, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * ifNull(column, val)
     * @param column 字段
     * @param val 值
     * @param alias 别名
     * @return children
     */
    default <J> Children jIfNull(SFunction<J, ?> column, Object val, String alias) {
        return jIfNull(true, column, val, alias);
    }

    /**
     * ifNull(column, val)
     * @param condition 执行条件
     * @param column 字段
     * @param val 字段
     * @param alias 别名
     * @return children
     */
    <J> Children jIfNull(boolean condition, SFunction<J, ?> column, Object val, String alias);

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

    /**
     * count(字段) as alias
     * @param column 字段
     * @return children
     */
    default Children count(SFunction<T, ?> column) {
        return count(true, column, null);
    }

    /**
     * count(字段) as alias
     * @param column 字段
     * @param aliasField 别名字段
     * @return children
     */
    default Children count(SFunction<T, ?> column, SFunction<T, ?> aliasField) {
        return count(column, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * count(字段) as alias
     * @param column 字段
     * @param alias 别名
     * @return children
     */
    default Children count(SFunction<T, ?> column, String alias) {
        return count(true, column, alias);
    }

    /**
     * count(字段) as alias
     * @param condition 执行条件
     * @param column 字段
     * @param alias 别名
     * @return children
     */
    Children count(boolean condition, SFunction<T, ?> column, String alias);

    /**
     * count(字段) as alias
     * @param column 字段
     * @return children
     */
    default <J> Children jCount(SFunction<J, ?> column) {
        return jCount(true, column, StringPool.EMPTY);
    }

    /**
     * count(字段) as alias
     * @param column 字段
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children jCount(SFunction<J, ?> column, SFunction<T, ?> aliasField) {
        return jCount(true, column, aliasField);
    }

    /**
     * count(字段) as alias
     * @param condition 执行条件
     * @param column 字段
     * @param aliasField 别名字段
     * @return children
     */
    default <J> Children jCount(boolean condition, SFunction<J, ?> column, SFunction<T, ?> aliasField) {
        return jCount(condition, column, LambdaUtil.toUnderlinePropertyName(aliasField));
    }

    /**
     * count(字段) as alias
     * @param column 字段
     * @param alias 别名
     * @return children
     */
    default <J> Children jCount(SFunction<J, ?> column, String alias) {
        return jCount(true, column, alias);
    }

    /**
     * count(字段) as alias
     * @param condition 执行条件
     * @param column 字段
     * @param alias 别名
     * @return children
     */
    <J> Children jCount(boolean condition, SFunction<J, ?> column, String alias);
}