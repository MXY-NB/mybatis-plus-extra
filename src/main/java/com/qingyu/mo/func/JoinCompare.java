package com.qingyu.mo.func;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

/**
 * 查询条件封装
 * <p>比较值</p>
 * <p>对mybatis-plus的Compare进行扩展</p>
 *
 * @author qingyu-mo
 * @since 2023-12-19
 */
public interface JoinCompare<Children, R> extends Serializable {

    /**
     * 等于 =
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children eq(R column, SFunction<J, ?> column2) {
        return eq(true, column, column2);
    }

    /**
     * 等于 =
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children eq(boolean condition, R column, SFunction<J, ?> column2);

    /**
     * 不等于 <>
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children ne(R column, SFunction<J, ?> column2) {
        return ne(true, column, column2);
    }

    /**
     * 不等于 <>
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children ne(boolean condition, R column, SFunction<J, ?> column2);

    /**
     * 大于 >
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children gt(R column, SFunction<J, ?> column2) {
        return gt(true, column, column2);
    }

    /**
     * 大于 >
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children gt(boolean condition, R column, SFunction<J, ?> column2);

    /**
     * 大于等于 >=
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children ge(R column, SFunction<J, ?> column2) {
        return ge(true, column, column2);
    }

    /**
     * 大于等于 >=
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children ge(boolean condition, R column, SFunction<J, ?> column2);

    /**
     * 小于 <
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children lt(R column, SFunction<J, ?> column2) {
        return lt(true, column, column2);
    }

    /**
     * 小于 <
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children lt(boolean condition, R column, SFunction<J, ?> column2);

    /**
     * 小于等于 <=
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children le(R column, SFunction<J, ?> column2) {
        return le(true, column, column2);
    }

    /**
     * 小于等于 <=
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children le(boolean condition, R column, SFunction<J, ?> column2);

    /**
     * 等于 =
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jEq(SFunction<J, ?> column, Object val) {
        return jEq(true, column, val);
    }

    /**
     * 等于 =
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jEq(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * 等于 =
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jEq(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jEq(true, column, column2);
    }

    /**
     * 等于 =
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jEq(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2);

    /**
     * 不等于 <>
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jNe(SFunction<J, ?> column, Object val) {
        return jNe(true, column, val);
    }

    /**
     * 不等于 <>
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jNe(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * 不等于 <>
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jNe(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jNe(true, column, column2);
    }

    /**
     * 不等于 <>
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jNe(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2);

    /**
     * 大于 >
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jGt(SFunction<J, ?> column, Object val) {
        return jGt(true, column, val);
    }
    
    /**
     * 大于 >
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jGt(boolean condition, SFunction<J, ?> column, Object val);
    
    /**
     * 大于 >
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jGt(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jGt(true, column, column2);
    }

    /**
     * 大于 >
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jGt(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2);

    /**
     * 大于等于 >=
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jGe(SFunction<J, ?> column, Object val) {
        return jGe(true, column, val);
    }

    /**
     * 大于等于 >=
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jGe(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * 大于等于 >=
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jGe(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jGe(true, column, column2);
    }

    /**
     * 大于等于 >=
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jGe(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2);

    /**
     * 小于 <
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jLt(SFunction<J, ?> column, Object val) {
        return jLt(true, column, val);
    }

    /**
     * 小于 <
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jLt(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * 小于 <
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jLt(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jLt(true, column, column2);
    }

    /**
     * 小于 <
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jLt(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2);

    /**
     * 小于等于 >=
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jLe(SFunction<J, ?> column, Object val) {
        return jLe(true, column, val);
    }

    /**
     * 小于等于 <=
     * @param condition 执行条件
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jLe(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * 小于等于 <=
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
     * sum(字段) 等于 =
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children sumEq(R column, Object val) {
        return sumEq(true, column, val);
    }

    /**
     * sum(字段) 等于 =
     * @param column 字段
     * @param val 值
     * @return children
     */
    Children sumEq(boolean condition, R column, Object val);

    /**
     * sum(字段) 等于 = sum(字段)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children sumEq(R column, SFunction<J, ?> column2) {
        return sumEq(true, column, column2);
    }

    /**
     * sum(字段) 等于 = sum(字段)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children sumEq(boolean condition, R column, SFunction<J, ?> column2);

    /**
     * sum(字段) 不等于 <>
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children sumNe(R column, Object val) {
        return sumNe(true, column, val);
    }

    /**
     * sum(字段) 不等于 <>
     * @param column 字段
     * @param val 值
     * @return children
     */
    Children sumNe(boolean condition, R column, Object val);

    /**
     * sum(字段) 不等于 <> sum(字段)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children sumNe(R column, SFunction<J, ?> column2) {
        return sumNe(true, column, column2);
    }

    /**
     * sum(字段) 不等于 <> sum(字段)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children sumNe(boolean condition, R column, SFunction<J, ?> column2);

    /**
     * sum(字段) 大于 >
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children sumGt(R column, Object val) {
        return sumGt(true, column, val);
    }

    /**
     * sum(字段) 大于 >
     * @param column 字段
     * @param val 值
     * @return children
     */
    Children sumGt(boolean condition, R column, Object val);

    /**
     * sum(字段) 大于 > sum(字段)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children sumGt(R column, SFunction<J, ?> column2) {
        return sumGt(true, column, column2);
    }

    /**
     * sum(字段) 大于 > sum(字段)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children sumGt(boolean condition, R column, SFunction<J, ?> column2);

    /**
     * sum(字段) 大于等于 >=
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children sumGe(R column, Object val) {
        return sumGe(true, column, val);
    }

    /**
     * sum(字段) 大于等于 >=
     * @param column 字段
     * @param val 值
     * @return children
     */
    Children sumGe(boolean condition, R column, Object val);

    /**
     * sum(字段) 大于等于 >= sum(字段)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children sumGe(R column, SFunction<J, ?> column2) {
        return sumGe(true, column, column2);
    }

    /**
     * sum(字段) 大于等于 >= sum(字段)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children sumGe(boolean condition, R column, SFunction<J, ?> column2);

    /**
     * sum(字段) 小于 <
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children sumLt(R column, Object val) {
        return sumLt(true, column, val);
    }

    /**
     * sum(字段) 小于 <
     * @param column 字段
     * @param val 值
     * @return children
     */
    Children sumLt(boolean condition, R column, Object val);

    /**
     * sum(字段) 小于 < sum(字段)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children sumLt(R column, SFunction<J, ?> column2) {
        return sumLt(true, column, column2);
    }

    /**
     * sum(字段) 小于 < sum(字段)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children sumLt(boolean condition, R column, SFunction<J, ?> column2);

    /**
     * sum(字段) 小于等于 <=
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children sumLe(R column, Object val) {
        return sumLe(true, column, val);
    }

    /**
     * sum(字段) 小于等于 <=
     * @param column 字段
     * @param val 值
     * @return children
     */
    Children sumLe(boolean condition, R column, Object val);

    /**
     * sum(字段) 小于等于 <= sum(字段)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children sumLe(R column, SFunction<J, ?> column2) {
        return sumLe(true, column, column2);
    }

    /**
     * sum(字段) 小于等于 <= sum(字段)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children sumLe(boolean condition, R column, SFunction<J, ?> column2);

    /**
     * sum(字段) 等于 =
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jSumEq(SFunction<J, ?> column, Object val) {
        return jSumEq(true, column, val);
    }

    /**
     * sum(字段) 等于 =
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jSumEq(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * sum(字段) 等于 = sum(字段)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jSumEq(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jSumEq(true, column, column2);
    }

    /**
     * sum(字段) 等于 = sum(字段)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jSumEq(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2);

    /**
     * sum(字段) 不等于 <>
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jSumNe(SFunction<J, ?> column, Object val) {
        return jSumNe(true, column, val);
    }

    /**
     * sum(字段) 不等于 <>
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jSumNe(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * sum(字段) 不等于 <> sum(字段)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jSumNe(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jSumNe(true, column, column2);
    }

    /**
     * sum(字段) 不等于 <> sum(字段)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jSumNe(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2);

    /**
     * sum(字段) 大于 >
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jSumGt(SFunction<J, ?> column, Object val) {
        return jSumGt(true, column, val);
    }

    /**
     * sum(字段) 大于 >
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jSumGt(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * sum(字段) 大于 > sum(字段)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jSumGt(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jSumGt(true, column, column2);
    }

    /**
     * sum(字段) 大于 > sum(字段)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jSumGt(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2);

    /**
     * sum(字段) 大于等于 >=
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jSumGe(SFunction<J, ?> column, Object val) {
        return jSumGe(true, column, val);
    }

    /**
     * sum(字段) 大于等于 >=
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jSumGe(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * sum(字段) 大于等于 >= sum(字段)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jSumGe(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jSumGe(true, column, column2);
    }

    /**
     * sum(字段) 大于等于 >= sum(字段)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jSumGe(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2);

    /**
     * sum(字段) 小于 <
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jSumLt(SFunction<J, ?> column, Object val) {
        return jSumLt(true, column, val);
    }

    /**
     * sum(字段) 小于 <
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jSumLt(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * sum(字段) 小于 < sum(字段)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jSumLt(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jSumLt(true, column, column2);
    }

    /**
     * sum(字段) 小于 < sum(字段)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jSumLt(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2);

    /**
     * sum(字段) 小于等于 <=
     * @param column 字段
     * @param val 值
     * @return children
     */
    default <J> Children jSumLe(SFunction<J, ?> column, Object val) {
        return jSumLe(true, column, val);
    }

    /**
     * sum(字段) 小于等于 <=
     * @param column 字段
     * @param val 值
     * @return children
     */
    <J> Children jSumLe(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * sum(字段) 小于等于 <= sum(字段)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jSumLe(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jSumLe(true, column, column2);
    }

    /**
     * sum(字段) 小于等于 <= sum(字段)
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jSumLe(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2);

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
