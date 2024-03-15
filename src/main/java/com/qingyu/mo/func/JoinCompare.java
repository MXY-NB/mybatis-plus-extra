package com.qingyu.mo.func;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.qingyu.mo.wrapper.query.JoinLambdaQueryWrapper;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * 查询条件封装
 * <p>比较值</p>
 * <p>对mybatis-plus的Compare进行扩展</p>
 *
 * @author qingyu-mo
 * @since 1.0.6.2
 */
@SuppressWarnings("unused")
public interface JoinCompare<Children, R, T> extends Serializable {

    /**
     * column = column2
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children eq(R column, SFunction<J, ?> column2) {
        return eq(true, column, column2);
    }

    /**
     * column = column2
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children eq(boolean condition, R column, SFunction<J, ?> column2);

    /**
     * (子查询) 等于 = val
     * <p>注意只有内部有 entity 才能使用该方法</p>
     * @param entityClass 子查询主表
     * @param consumer 子查询
     * @param val 值
     * @return children
     */
    <J> Children eq(Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer, Object val);

    /**
     * column <> column2
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children ne(R column, SFunction<J, ?> column2) {
        return ne(true, column, column2);
    }

    /**
     * column <> column2
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children ne(boolean condition, R column, SFunction<J, ?> column2);

    /**
     * column > column2
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children gt(R column, SFunction<J, ?> column2) {
        return gt(true, column, column2);
    }

    /**
     * column > column2
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children gt(boolean condition, R column, SFunction<J, ?> column2);

    /**
     * column >= column2
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children ge(R column, SFunction<J, ?> column2) {
        return ge(true, column, column2);
    }

    /**
     * column >= column2
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children ge(boolean condition, R column, SFunction<J, ?> column2);

    /**
     * column < column2
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children lt(R column, SFunction<J, ?> column2) {
        return lt(true, column, column2);
    }

    /**
     * column < column2
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children lt(boolean condition, R column, SFunction<J, ?> column2);

    /**
     * column <= column2
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children le(R column, SFunction<J, ?> column2) {
        return le(true, column, column2);
    }

    /**
     * column <= column2
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children le(boolean condition, R column, SFunction<J, ?> column2);

    /**
     * column = val
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jEq(SFunction<J, ?> column, Object val) {
        return jEq(true, column, val);
    }

    /**
     * column = val
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jEq(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * column = column2
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jEq(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jEq(true, column, column2);
    }

    /**
     * column = column2
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jEq(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2);

    /**
     * column <> val
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jNe(SFunction<J, ?> column, Object val) {
        return jNe(true, column, val);
    }

    /**
     * column <> val
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jNe(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * column <> column2
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jNe(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jNe(true, column, column2);
    }

    /**
     * column <> column2
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jNe(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2);

    /**
     * column > val
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jGt(SFunction<J, ?> column, Object val) {
        return jGt(true, column, val);
    }

    /**
     * column > val
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jGt(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * column > column2
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jGt(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jGt(true, column, column2);
    }

    /**
     * column > column2
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jGt(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2);

    /**
     * column >= val
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jGe(SFunction<J, ?> column, Object val) {
        return jGe(true, column, val);
    }

    /**
     * column >= val
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jGe(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * column >= column2
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jGe(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jGe(true, column, column2);
    }

    /**
     * column >= column2
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jGe(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2);

    /**
     * column < val
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jLt(SFunction<J, ?> column, Object val) {
        return jLt(true, column, val);
    }

    /**
     * column < val
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jLt(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * column < column2
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jLt(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jLt(true, column, column2);
    }

    /**
     * column < column2
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jLt(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2);

    /**
     * column >= val
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jLe(SFunction<J, ?> column, Object val) {
        return jLe(true, column, val);
    }

    /**
     * column <= val
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jLe(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * column <= column2
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jLe(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jLe(true, column, column2);
    }

    /**
     * 小于等于 <=
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jLe(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2);

    /**
     * sum(column) = val
     * @param column 字段
     * @param val 值
     * @return children
     */
    default Children sumEq(R column, Object val) {
        return sumEq(true, column, val);
    }

    /**
     * sum(column) = val
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    Children sumEq(boolean condition, R column, Object val);

    /**
     * sum(column) = val
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children sumEq(R column, SFunction<J, ?> column2) {
        return sumEq(true, column, column2);
    }

    /**
     * sum(column) = val
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children sumEq(boolean condition, R column, SFunction<J, ?> column2);

    /**
     * sum(column) <> val
     * @param column 字段
     * @param val 值
     * @return children
     */
    default Children sumNe(R column, Object val) {
        return sumNe(true, column, val);
    }

    /**
     * sum(column) <> val
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    Children sumNe(boolean condition, R column, Object val);

    /**
     * sum(column) <> val
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children sumNe(R column, SFunction<J, ?> column2) {
        return sumNe(true, column, column2);
    }

    /**
     * sum(column) <> val
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children sumNe(boolean condition, R column, SFunction<J, ?> column2);

    /**
     * sum(column) > val
     * @param column 字段
     * @param val 值
     * @return children
     */
    default Children sumGt(R column, Object val) {
        return sumGt(true, column, val);
    }

    /**
     * sum(column) > val
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    Children sumGt(boolean condition, R column, Object val);

    /**
     * sum(column) > val
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children sumGt(R column, SFunction<J, ?> column2) {
        return sumGt(true, column, column2);
    }

    /**
     * sum(column) > val
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children sumGt(boolean condition, R column, SFunction<J, ?> column2);

    /**
     * sum(column) >= val
     * @param column 字段
     * @param val 值
     * @return children
     */
    default Children sumGe(R column, Object val) {
        return sumGe(true, column, val);
    }

    /**
     * sum(column) >= val
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    Children sumGe(boolean condition, R column, Object val);

    /**
     * sum(column) >= val
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children sumGe(R column, SFunction<J, ?> column2) {
        return sumGe(true, column, column2);
    }

    /**
     * sum(column) >= val
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children sumGe(boolean condition, R column, SFunction<J, ?> column2);

    /**
     * sum(column) < val
     * @param column 字段
     * @param val 值
     * @return children
     */
    default Children sumLt(R column, Object val) {
        return sumLt(true, column, val);
    }

    /**
     * sum(column) < val
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    Children sumLt(boolean condition, R column, Object val);

    /**
     * sum(column) < val
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children sumLt(R column, SFunction<J, ?> column2) {
        return sumLt(true, column, column2);
    }

    /**
     * sum(column) < val
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children sumLt(boolean condition, R column, SFunction<J, ?> column2);

    /**
     * sum(column) <= val
     * @param column 字段
     * @param val 值
     * @return children
     */
    default Children sumLe(R column, Object val) {
        return sumLe(true, column, val);
    }

    /**
     * sum(column) <= val
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    Children sumLe(boolean condition, R column, Object val);

    /**
     * sum(column) <= val
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children sumLe(R column, SFunction<J, ?> column2) {
        return sumLe(true, column, column2);
    }

    /**
     * sum(column) <= val
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children sumLe(boolean condition, R column, SFunction<J, ?> column2);

    /**
     * sum(column) = val
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jSumEq(SFunction<J, ?> column, Object val) {
        return jSumEq(true, column, val);
    }

    /**
     * sum(column) = val
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jSumEq(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * sum(column) = val
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jSumEq(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jSumEq(true, column, column2);
    }

    /**
     * sum(column) = val
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jSumEq(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2);

    /**
     * sum(column) <> val
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jSumNe(SFunction<J, ?> column, Object val) {
        return jSumNe(true, column, val);
    }

    /**
     * sum(column) <> val
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jSumNe(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * sum(column) <> val
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jSumNe(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jSumNe(true, column, column2);
    }

    /**
     * sum(column) <> val
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jSumNe(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2);

    /**
     * sum(column) > val
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jSumGt(SFunction<J, ?> column, Object val) {
        return jSumGt(true, column, val);
    }

    /**
     * sum(column) > val
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jSumGt(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * sum(column) > val
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jSumGt(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jSumGt(true, column, column2);
    }

    /**
     * sum(column) > val
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jSumGt(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2);

    /**
     * sum(column) >= val
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jSumGe(SFunction<J, ?> column, Object val) {
        return jSumGe(true, column, val);
    }

    /**
     * sum(column) >= val
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jSumGe(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * sum(column) >= val
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jSumGe(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jSumGe(true, column, column2);
    }

    /**
     * sum(column) >= val
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jSumGe(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2);

    /**
     * sum(column) < val
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jSumLt(SFunction<J, ?> column, Object val) {
        return jSumLt(true, column, val);
    }

    /**
     * sum(column) < val
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jSumLt(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * sum(column) < column2
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jSumLt(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jSumLt(true, column, column2);
    }

    /**
     * sum(column) < column2
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jSumLt(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2);

    /**
     * sum(column) <= val
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jSumLe(SFunction<J, ?> column, Object val) {
        return jSumLe(true, column, val);
    }

    /**
     * sum(column) <= val
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jSumLe(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * sum(column) <= column2
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jSumLe(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jSumLe(true, column, column2);
    }

    /**
     * sum(column) <= column2
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jSumLe(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2);

    /**
     * val = sum(column)
     * @param column 字段
     * @param val 值
     * @return children
     */
    default Children eqSum(Object val, R column) {
        return eqSum(true, val, column);
    }

    /**
     * val = sum(column)
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    Children eqSum(boolean condition, Object val, R column);

    /**
     * column = sum(column2)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children eqSum(SFunction<T, ?> column, SFunction<J, ?> column2) {
        return eqSum(true, column, column2);
    }

    /**
     * column = sum(column2)
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children eqSum(boolean condition, SFunction<T, ?> column, SFunction<J, ?> column2);

    /**
     * val <> sum(column)
     * @param column 字段
     * @param val 值
     * @return children
     */
    default Children neSum(Object val, R column) {
        return neSum(true, val, column);
    }

    /**
     * val <> sum(column)
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    Children neSum(boolean condition, Object val, R column);

    /**
     * column <> sum(column2)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children neSum(SFunction<T, ?> column, SFunction<J, ?> column2) {
        return neSum(true, column, column2);
    }

    /**
     * column <> sum(column2)
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children neSum(boolean condition, SFunction<T, ?> column, SFunction<J, ?> column2);

    /**
     * val > sum(column)
     * @param column 字段
     * @param val 值
     * @return children
     */
    default Children gtSum(Object val, R column) {
        return gtSum(true, val, column);
    }

    /**
     * val > sum(column)
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    Children gtSum(boolean condition, Object val, R column);

    /**
     * column > sum(column2)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children gtSum(SFunction<T, ?> column, SFunction<J, ?> column2) {
        return gtSum(true, column, column2);
    }

    /**
     * column > sum(column2)
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children gtSum(boolean condition, SFunction<T, ?> column, SFunction<J, ?> column2);

    /**
     * val >= sum(column)
     * @param column 字段
     * @param val 值
     * @return children
     */
    default Children geSum(Object val, R column) {
        return geSum(true, val, column);
    }

    /**
     * val >= sum(column)
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    Children geSum(boolean condition, Object val, R column);

    /**
     * column >= sum(column2)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children geSum(SFunction<T, ?> column, SFunction<J, ?> column2) {
        return geSum(true, column, column2);
    }

    /**
     * column >= sum(column2)
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children geSum(boolean condition, SFunction<T, ?> column, SFunction<J, ?> column2);

    /**
     * val < sum(column)
     * @param column 字段
     * @param val 值
     * @return children
     */
    default Children ltSum(Object val, R column) {
        return ltSum(true, val, column);
    }

    /**
     * val < sum(column)
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    Children ltSum(boolean condition, Object val, R column);

    /**
     * column < sum(column2)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children ltSum(SFunction<T, ?> column, SFunction<J, ?> column2) {
        return ltSum(true, column, column2);
    }

    /**
     * column < sum(column2)
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children ltSum(boolean condition, SFunction<T, ?> column, SFunction<J, ?> column2);

    /**
     * val <= sum(column)
     * @param column 字段
     * @param val 值
     * @return children
     */
    default Children leSum(Object val, R column) {
        return leSum(true, val, column);
    }

    /**
     * val <= sum(column)
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    Children leSum(boolean condition, Object val, R column);

    /**
     * column <= sum(column2)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children leSum(SFunction<T, ?> column, SFunction<J, ?> column2) {
        return leSum(true, column, column2);
    }

    /**
     * column <= sum(column2)
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children leSum(boolean condition, SFunction<T, ?> column, SFunction<J, ?> column2);

    /**
     * val = sum(column)
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jEqSum(Object val, SFunction<J, ?> column) {
        return jEqSum(true, val, column);
    }

    /**
     * val = sum(column)
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jEqSum(boolean condition, Object val, SFunction<J, ?> column);

    /**
     * column = sum(column2)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jEqSum(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jEqSum(true, column, column2);
    }

    /**
     * column = sum(column2)
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jEqSum(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2);

    /**
     * val <> sum(column)
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jNeSum(Object val, SFunction<J, ?> column) {
        return jNeSum(true, val, column);
    }

    /**
     * val <> sum(column)
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jNeSum(boolean condition, Object val, SFunction<J, ?> column);

    /**
     * column <> sum(column2)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jNeSum(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jNeSum(true, column, column2);
    }

    /**
     * column <> sum(column2)
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jNeSum(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2);

    /**
     * val > sum(column)
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jGtSum(Object val, SFunction<J, ?> column) {
        return jGtSum(true, val, column);
    }

    /**
     * val > sum(column)
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jGtSum(boolean condition, Object val, SFunction<J, ?> column);

    /**
     * column > sum(column2)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jGtSum(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jGtSum(true, column, column2);
    }

    /**
     * column > sum(column2)
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jGtSum(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2);

    /**
     * val >= sum(column)
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jGeSum(Object val, SFunction<J, ?> column) {
        return jGeSum(true, val, column);
    }

    /**
     * val >= sum(column)
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jGeSum(boolean condition, Object val, SFunction<J, ?> column);

    /**
     * column >= sum(column2)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jGeSum(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jGeSum(true, column, column2);
    }

    /**
     * column >= sum(column2)
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jGeSum(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2);

    /**
     * val < sum(column)
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jLtSum(Object val, SFunction<J, ?> column) {
        return jLtSum(true, val, column);
    }

    /**
     * val < sum(column)
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jLtSum(boolean condition, Object val, SFunction<J, ?> column);

    /**
     * column < sum(column2)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jLtSum(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jLtSum(true, column, column2);
    }

    /**
     * column < sum(column2)
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jLtSum(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2);

    /**
     * val <= sum(column)
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jLeSum(Object val, SFunction<J, ?> column) {
        return jLeSum(true, val, column);
    }

    /**
     * val <= sum(column)
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jLeSum(boolean condition, Object val, SFunction<J, ?> column);

    /**
     * column <= sum(column2)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jLeSum(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jLeSum(true, column, column2);
    }

    /**
     * column <= sum(column2)
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jLeSum(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2);

    /**
     * count(column) > val
     * @param column 字段
     * @param val 值
     * @return children
     */
    default Children countGt(SFunction<T, ?> column, Object val) {
        return countGt(true, column, val);
    }

    /**
     * count(column) > val
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    Children countGt(boolean condition, SFunction<T, ?> column, Object val);

    /**
     * count(column) >= val
     * @param column 字段
     * @param val 值
     * @return children
     */
    default Children countGe(SFunction<T, ?> column, Object val) {
        return countGe(true, column, val);
    }

    /**
     * count(column) >= val
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    Children countGe(boolean condition, SFunction<T, ?> column, Object val);

    /**
     * count(column) <= val
     * @param column 字段
     * @param val 值
     * @return children
     */
    default Children countLt(SFunction<T, ?> column, Object val) {
        return countLt(true, column, val);
    }

    /**
     * count(column) <= val
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    Children countLt(boolean condition, SFunction<T, ?> column, Object val);

    /**
     * count(column) <= val
     * @param column 字段
     * @param val 值
     * @return children
     */
    default Children countLe(SFunction<T, ?> column, Object val) {
        return countLe(true, column, val);
    }

    /**
     * count(column) <= val
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    Children countLe(boolean condition, SFunction<T, ?> column, Object val);

    /**
     * count(column) > val
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jCountGt(SFunction<J, ?> column, Object val) {
        return jCountGt(true, column, val);
    }

    /**
     * count(column) > val
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jCountGt(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * count(column) > val
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jCountGe(SFunction<J, ?> column, Object val) {
        return jCountGe(true, column, val);
    }

    /**
     * count(column) > val
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jCountGe(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * count(column) > val
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jCountLt(SFunction<J, ?> column, Object val) {
        return jCountLt(true, column, val);
    }

    /**
     * count(column) > val
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jCountLt(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * count(column) > val
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jCountLe(SFunction<J, ?> column, Object val) {
        return jCountLe(true, column, val);
    }

    /**
     * count(column) > val
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jCountLe(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * BETWEEN 值1 AND 值2
     * @param column 字段
     * @param val1 值1
     * @param val2 值2
     * @return children
     */
    default <J> Children jBetween(SFunction<J, ?> column, Object val1, Object val2) {
        return jBetween(true, column, val1, val2);
    }

    /**
     * BETWEEN 值1 AND 值2
     * @param condition 执行条件
     * @param column 字段
     * @param val1 值1
     * @param val2 值2
     * @return children
     */
    <J> Children jBetween(boolean condition, SFunction<J, ?> column, Object val1, Object val2);

    /**
     * NOT BETWEEN 值1 AND 值2
     * @param column 字段
     * @param val1 值1
     * @param val2 值2
     * @return children
     */
    default <J> Children jNotBetween(SFunction<J, ?> column, Object val1, Object val2) {
        return jNotBetween(true, column, val1, val2);
    }

    /**
     * NOT BETWEEN 值1 AND 值2
     * @param condition 执行条件
     * @param column 字段
     * @param val1 值1
     * @param val2 值2
     * @return children
     */
    <J> Children jNotBetween(boolean condition, SFunction<J, ?> column, Object val1, Object val2);

    /**
     * LIKE '%值%'
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jLike(SFunction<J, ?> column, Object val) {
        return jLike(true, column, val);
    }

    /**
     * LIKE '%值%'
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jLike(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * LIKE '%值'
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jLikeLeft(SFunction<J, ?> column, Object val) {
        return jLikeLeft(true, column, val);
    }

    /**
     * LIKE '%值'
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jLikeLeft(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * LIKE '值%'
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jLikeRight(SFunction<J, ?> column, Object val) {
        return jLikeRight(true, column, val);
    }

    /**
     * LIKE '值%'
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jLikeRight(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * json_contain 包含
     * @param column 字段
     * @param values 值
     * @return children
     */
    default Children jsonContain(R column, Object... values) {
        return jsonContain(column, Arrays.asList(values));
    }

    /**
     * json_contain 包含
     * @param column 字段
     * @param coll 值
     * @return children
     */
    default Children jsonContain(R column, Collection<?> coll) {
        return jsonContain(true, column, coll);
    }

    /**
     * json_contain 包含
     * @param condition 执行条件
     * @param column 字段
     * @param coll 值
     * @return children
     */
    Children jsonContain(boolean condition, R column, Collection<?> coll);

    /**
     * json_overlaps 包含任意一个
     * @param column 字段
     * @param values 值
     * @return children
     */
    default Children jsonContainAny(R column, Object... values) {
        return jsonContainAny(column, Arrays.asList(values));
    }

    /**
     * json_overlaps 包含任意一个
     * @param column 字段
     * @param coll 值
     * @return children
     */
    default Children jsonContainAny(R column, Collection<?> coll) {
        return jsonContainAny(true, column, coll);
    }

    /**
     * json_overlaps 包含任意一个
     * @param condition 执行条件
     * @param column 字段
     * @param coll 值
     * @return children
     */
    Children jsonContainAny(boolean condition, R column, Collection<?> coll);
}
