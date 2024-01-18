package com.qingyu.mo.func;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.qingyu.mo.wrapper.query.JoinLambdaQueryWrapper;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * <p>
 * 查询条件封装
 * </p>
 * <p>对mybatis-plus的Func进行扩展</p>
 *
 * @author qingyu-mo
 * @since 2023-12-19
 */
@SuppressWarnings("unused")
public interface JoinFunc<Children, R> extends Serializable {

    /**
     * 排序：正序
     * @param columns 字段
     * @return children
     */
    @SuppressWarnings("unchecked")
    default <J> Children jOrderByAsc(SFunction<J, ?>... columns) {
        return jOrderByAsc(true, columns);
    }

    /**
     * 排序：正序
     * @param condition 执行条件
     * @param columns 字段
     * @return children
     */
    @SuppressWarnings("unchecked")
    default <J> Children jOrderByAsc(boolean condition, SFunction<J, ?>... columns) {
        return jOrderByAsc(condition, Arrays.asList(columns));
    }

    /**
     * 排序：正序
     * @param columns 字段数组
     * @return children
     */
    default <J> Children jOrderByAsc(List<SFunction<J, ?>> columns) {
        return jOrderByAsc(true, columns);
    }

    /**
     * 排序：正序
     * @param condition 执行条件
     * @param columns 字段数组
     * @return children
     */
    default <J> Children jOrderByAsc(boolean condition, List<SFunction<J, ?>> columns) {
        return jOrderBy(condition, true, columns);
    }

    /**
     * 排序：倒序
     * @param columns 字段
     * @return children
     */
    @SuppressWarnings("unchecked")
    default <J> Children jOrderByDesc(SFunction<J, ?>... columns) {
        return jOrderByDesc(true, columns);
    }

    /**
     * 排序：倒序
     * @param condition 执行条件
     * @param columns 字段
     * @return children
     */
    @SuppressWarnings("unchecked")
    default <J> Children jOrderByDesc(boolean condition, SFunction<J, ?>... columns) {
        return jOrderByDesc(condition, Arrays.asList(columns));
    }

    /**
     * 排序：倒序
     * @param columns 字段数组
     * @return children
     */
    default <J> Children jOrderByDesc(List<SFunction<J, ?>> columns) {
        return jOrderByDesc(true, columns);
    }

    /**
     * 排序：倒序
     * @param condition 执行条件
     * @param columns 字段数组
     * @return children
     */
    default <J> Children jOrderByDesc(boolean condition, List<SFunction<J, ?>> columns) {
        return jOrderBy(condition, false, columns);
    }

    /**
     * 排序
     * @param condition 执行条件
     * @param isAsc 是否是 ASC 排序
     * @param columns 字段数组
     * @return children
     */
    <J> Children jOrderBy(boolean condition, boolean isAsc, List<SFunction<J, ?>> columns);

    /**
     * 排序 sum函数：正序
     * @param column 字段
     * @return children
     */
    default Children orderBySumAsc(R column) {
        return orderBySumAsc(true, column);
    }

    /**
     * 排序 sum函数：正序
     * @param condition 执行条件
     * @param column 字段
     * @return children
     */
    default Children orderBySumAsc(boolean condition, R column) {
        return orderBySum(condition, true, column);
    }

    /**
     * 排序 sum函数：正序
     * @param column 字段
     * @return children
     */
    default Children orderBySumDesc(R column) {
        return orderBySumDesc(true, column);
    }

    /**
     * 排序 sum函数：正序
     * @param condition 执行条件
     * @param column 字段
     * @return children
     */
    default Children orderBySumDesc(boolean condition, R column) {
        return orderBySum(condition, false, column);
    }

    /**
     * 排序 sum函数
     * @param condition 执行条件
     * @param isAsc 是否是 ASC 排序
     * @param column 字段
     * @return children
     */
    Children orderBySum(boolean condition, boolean isAsc, R column);

    /**
     * 排序 sum函数：倒序
     * @param column 字段
     * @return children
     */
    default <J> Children jOrderBySumAsc(SFunction<J, ?> column) {
        return jOrderBySumAsc(true, column);
    }

    /**
     * 排序 sum函数：倒序
     * @param condition 执行条件
     * @param column 字段
     * @return children
     */
    default <J> Children jOrderBySumAsc(boolean condition, SFunction<J, ?> column) {
        return jOrderBySum(condition, true, column);
    }

    /**
     * 排序 sum函数：倒序
     * @param column 字段
     * @return children
     */
    default <J> Children jOrderBySumDesc(SFunction<J, ?> column) {
        return jOrderBySumDesc(true, column);
    }

    /**
     * 排序 sum函数：倒序
     * @param condition 执行条件
     * @param column 字段
     * @return children
     */
    default <J> Children jOrderBySumDesc(boolean condition, SFunction<J, ?> column) {
        return jOrderBySum(condition, false, column);
    }

    /**
     * 排序 sum函数
     * @param condition 执行条件
     * @param isAsc 是否是 ASC 排序
     * @param column 字段
     * @return children
     */
    <J> Children jOrderBySum(boolean condition, boolean isAsc, SFunction<J, ?> column);

    /**
     * 字段 IS NULL
     * @param column 字段
     * @return children
     */
    default <J> Children jIsNull(SFunction<J, ?> column) {
        return jIsNull(true, column);
    }

    /**
     * 字段 IS NULL
     * @param condition 执行条件
     * @param column 字段
     * @return children
     */
    <J> Children jIsNull(boolean condition, SFunction<J, ?> column);

    /**
     * 字段 IS NOT NULL
     * @param column 字段
     * @return children
     */
    default <J> Children jIsNotNull(SFunction<J, ?> column) {
        return jIsNotNull(true, column);
    }

    /**
     * 字段 IS NOT NULL
     * @param condition 执行条件
     * @param column 字段
     * @return children
     */
    <J> Children jIsNotNull(boolean condition, SFunction<J, ?> column);

    /**
     * 字段 IN (子查询)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 字段
     * @param entityClass 子查询主表
     * @param consumer 子查询
     * @return children
     */
    <J> Children in(R column, Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer);

    /**
     * 字段 NOT IN (子查询)
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 字段
     * @param entityClass 子查询主表
     * @param consumer 子查询
     * @return children
     */
    <J> Children notIn(R column, Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer);

    /**
     * 字段 IN ()
     * @param column 字段
     * @param values 数据集合
     * @return children
     */
    default <J> Children jIn(SFunction<J, ?> column, Object... values) {
        return jIn(true, column, values);
    }

    /**
     * 字段 IN ()
     * @param condition 执行条件
     * @param column 字段
     * @param values 数据集合
     * @return children
     */
    default <J> Children jIn(boolean condition, SFunction<J, ?> column, Object... values) {
        return jIn(condition, column, Arrays.asList(values));
    }

    /**
     * 字段 IN ()
     * @param column 字段
     * @param coll 数据集合
     * @return children
     */
    default <J> Children jIn(SFunction<J, ?> column, Collection<?> coll) {
        return jIn(true, column, coll);
    }

    /**
     * 字段 IN ()
     * @param condition 执行条件
     * @param column 字段
     * @param coll 数据集合
     * @return children
     */
    <J> Children jIn(boolean condition, SFunction<J, ?> column, Collection<?> coll);

    /**
     * 字段 NOT IN ()
     * @param column 字段
     * @param values 数据集合
     * @return children
     */
    default <J> Children jNotIn(SFunction<J, ?> column, Object... values) {
        return jNotIn(true, column, values);
    }

    /**
     * 字段 NOT IN ()
     * @param condition 执行条件
     * @param column 字段
     * @param values 数据集合
     * @return children
     */
    default <J> Children jNotIn(boolean condition, SFunction<J, ?> column, Object... values) {
        return jNotIn(condition, column, Arrays.asList(values));
    }

    /**
     * 字段 NOT IN ()
     * @param column 字段
     * @param coll 数据集合
     * @return children
     */
    default <J> Children jNotIn(SFunction<J, ?> column, Collection<?> coll) {
        return jNotIn(true, column, coll);
    }

    /**
     * 字段 NOT IN ()
     * @param condition 执行条件
     * @param column 字段
     * @param coll 数据集合
     * @return children
     */
    <J> Children jNotIn(boolean condition, SFunction<J, ?> column, Collection<?> coll);

    /**
     * DATE_FORMAT(column) IN ()
     * @param column 字段
     * @param values 数据集合
     * @return children
     * @see #groupByMonth
     * 配合一起使用
     */
    default Children inMonths(R column, Object... values) {
        return inMonths(true, column, values);
    }

    /**
     * DATE_FORMAT(column) IN ()
     * @param condition 执行条件
     * @param column 字段
     * @param values 数据集合
     * @return children
     * @see #groupByMonth
     * 配合一起使用
     */
    default Children inMonths(boolean condition, R column, Object... values) {
        return inMonths(condition, column, Arrays.asList(values));
    }

    /**
     * DATE_FORMAT(column) IN ()
     * @param column 字段
     * @param coll 数据集合
     * @return children
     * @see #groupByMonth
     * 配合一起使用
     */
    default Children inMonths(R column, Collection<?> coll) {
        return inMonths(true, column, coll);
    }

    /**
     * DATE_FORMAT(column) IN ()
     * @param condition 执行条件
     * @param column 字段
     * @param coll 数据集合
     * @return children
     * @see #groupByMonth
     * 配合一起使用
     */
    Children inMonths(boolean condition, R column, Collection<?> coll);

    /**
     * DATE_FORMAT(column) IN ()
     * @param column 字段
     * @param values 数据集合
     * @return children
     * @see #groupByMonth
     * 配合一起使用
     */
    default <J> Children jInMonths(SFunction<J, ?> column, Object... values) {
        return jInMonths(true, column, values);
    }

    /**
     * DATE_FORMAT(column) IN ()
     * @param condition 执行条件
     * @param column 字段
     * @param values 数据集合
     * @return children
     * @see #jGroupByMonth
     * 配合一起使用
     */
    default <J> Children jInMonths(boolean condition, SFunction<J, ?> column, Object... values) {
        return jInMonths(condition, column, Arrays.asList(values));
    }

    /**
     * DATE_FORMAT(column) IN ()
     * @param column 字段
     * @param coll 数据集合
     * @return children
     * @see #jGroupByMonth
     * 配合一起使用
     */
    default <J> Children jInMonths(SFunction<J, ?> column, Collection<?> coll) {
        return jInMonths(true, column, coll);
    }

    /**
     * DATE_FORMAT(column) IN ()
     * @param condition 执行条件
     * @param column 字段
     * @param coll 数据集合
     * @return children
     * @see #jGroupByMonth
     * 配合一起使用
     */
    <J> Children jInMonths(boolean condition, SFunction<J, ?> column, Collection<?> coll);

    /**
     * DATE_FORMAT(column) NOT IN ()
     * @param column 字段
     * @param values 数据集合
     * @return children
     * @see #groupByMonth
     * 配合一起使用
     */
    default Children notInMonths(R column, Object... values) {
        return notInMonths(true, column, values);
    }

    /**
     * DATE_FORMAT(column) NOT IN ()
     * @param condition 执行条件
     * @param column 字段
     * @param values 数据集合
     * @return children
     * @see #groupByMonth
     * 配合一起使用
     */
    default Children notInMonths(boolean condition, R column, Object... values) {
        return notInMonths(condition, column, Arrays.asList(values));
    }

    /**
     * DATE_FORMAT(column) NOT IN ()
     * @param column 字段
     * @param coll 数据集合
     * @return children
     * @see #groupByMonth
     * 配合一起使用
     */
    default Children notInMonths(R column, Collection<?> coll) {
        return notInMonths(true, column, coll);
    }

    /**
     * DATE_FORMAT(column) NOT IN ()
     * @param condition 执行条件
     * @param column 字段
     * @param coll 数据集合
     * @return children
     * @see #groupByMonth
     * 配合一起使用
     */
    Children notInMonths(boolean condition, R column, Collection<?> coll);

    /**
     * DATE_FORMAT(column) NOT IN ()
     * @param column 字段
     * @param values 数据集合
     * @return children
     * @see #groupByMonth
     * 配合一起使用
     */
    default <J> Children jNotInMonths(SFunction<J, ?> column, Object... values) {
        return jNotInMonths(true, column, values);
    }

    /**
     * DATE_FORMAT(column) NOT IN ()
     * @param condition 执行条件
     * @param column 字段
     * @param values 数据集合
     * @return children
     * @see #jGroupByMonth
     * 配合一起使用
     */
    default <J> Children jNotInMonths(boolean condition, SFunction<J, ?> column, Object... values) {
        return jNotInMonths(condition, column, Arrays.asList(values));
    }

    /**
     * DATE_FORMAT(column) NOT IN ()
     * @param column 字段
     * @param coll 数据集合
     * @return children
     * @see #jGroupByMonth
     * 配合一起使用
     */
    default <J> Children jNotInMonths(SFunction<J, ?> column, Collection<?> coll) {
        return jNotInMonths(true, column, coll);
    }

    /**
     * DATE_FORMAT(column) NOT IN ()
     * @param condition 执行条件
     * @param column 字段
     * @param coll 数据集合
     * @return children
     * @see #jGroupByMonth
     * 配合一起使用
     */
    <J> Children jNotInMonths(boolean condition, SFunction<J, ?> column, Collection<?> coll);

    /**
     * GROUP BY 字段：按月分组
     * @param column 需要查询的表字段
     * @return children
     */
    default Children groupByMonth(R column) {
        return groupByMonth(true, column);
    }

    /**
     * GROUP BY 字段：按月分组
     * @param condition 执行条件
     * @param column 需要查询的表字段
     * @return children
     */
    Children groupByMonth(boolean condition, R column);

    /**
     * GROUP BY 字段：按月分组
     * @param column 需要查询的表字段
     * @return children
     */
    default <J> Children jGroupByMonth(SFunction<J, ?> column) {
        return jGroupByMonth(true, column);
    }

    /**
     * GROUP BY 字段：按月分组
     * @param condition 执行条件
     * @param column 需要查询的表字段
     * @return children
     */
    <J> Children jGroupByMonth(boolean condition, SFunction<J, ?> column);

    /**
     * HAVING
     * @param consumer 消费函数
     * @return children
     */
    default Children jHaving(Consumer<Children> consumer) {
        return jHaving(true, consumer);
    }

    /**
     * HAVING
     * @param consumer 消费函数
     * @return children
     */
    Children jHaving(boolean condition, Consumer<Children> consumer);
}
