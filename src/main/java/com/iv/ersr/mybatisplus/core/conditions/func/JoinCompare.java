/*
 * Copyright (c) 2011-2022, baomidou (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iv.ersr.mybatisplus.core.conditions.func;

import java.io.Serializable;
import java.util.Map;
import java.util.function.BiPredicate;

/**
 * 查询条件封装
 * <p>比较值</p>
 *
 * @author moxiaoyu
 * @since 2023-03-22
 */
public interface JoinCompare<Children, S> extends Serializable {

    /**
     * ignore
     */
    default <R, V> Children joinAllEq(Map<R, V> params) {
        return joinAllEq(params, true);
    }

    /**
     * ignore
     */
    default <R, V> Children joinAllEq(Map<R, V> params, boolean null2IsNull) {
        return joinAllEq(true, params, null2IsNull);
    }

    /**
     * map 所有非空属性等于 =
     *
     * @param condition   执行条件
     * @param params      map 类型的参数, key 是字段名, value 是字段值
     * @param null2IsNull 是否参数为 null 自动执行 isNull 方法, false 则忽略这个字段\
     * @return children
     */
    <R, V> Children joinAllEq(boolean condition, Map<R, V> params, boolean null2IsNull);

    /**
     * ignore
     */
    default <R, V> Children joinAllEq(BiPredicate<R, V> filter, Map<R, V> params) {
        return joinAllEq(filter, params, true);
    }

    /**
     * ignore
     */
    default <R, V> Children joinAllEq(BiPredicate<R, V> filter, Map<R, V> params, boolean null2IsNull) {
        return joinAllEq(true, filter, params, null2IsNull);
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
    <R, V> Children joinAllEq(boolean condition, BiPredicate<R, V> filter, Map<R, V> params, boolean null2IsNull);

    /**
     * ignore
     */
    default <R> Children joinEq(R column, Object val) {
        return joinEq(true, column, val);
    }

    /**
     * 等于 =
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <R> Children joinEq(boolean condition, R column, Object val);

    /**
     * ignore
     */
    default <R> Children joinNe(R column, Object val) {
        return joinNe(true, column, val);
    }

    /**
     * 不等于 &lt;&gt;
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <R> Children joinNe(boolean condition, R column, Object val);

    /**
     * ignore
     */
    default <R> Children joinGt(R column, Object val) {
        return joinGt(true, column, val);
    }

    /**
     * 大于 &gt;
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <R> Children joinGt(boolean condition, R column, Object val);

    /**
     * ignore
     */
    default <R> Children joinGe(R column, Object val) {
        return joinGe(true, column, val);
    }

    /**
     * 大于等于 &gt;=
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <R> Children joinGe(boolean condition, R column, Object val);

    /**
     * ignore
     */
    default <R> Children joinLt(R column, Object val) {
        return joinLt(true, column, val);
    }

    /**
     * 小于 &lt;
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <R> Children joinLt(boolean condition, R column, Object val);

    /**
     * ignore
     */
    default <R> Children joinLe(R column, Object val) {
        return joinLe(true, column, val);
    }

    /**
     * 小于等于 &lt;=
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <R> Children joinLe(boolean condition, R column, Object val);

    /**
     * ignore
     */
    default <R> Children joinBetween(R column, Object val1, Object val2) {
        return joinBetween(true, column, val1, val2);
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
    <R> Children joinBetween(boolean condition, R column, Object val1, Object val2);

    /**
     * ignore
     */
    default <R> Children joinNotBetween(R column, Object val1, Object val2) {
        return joinNotBetween(true, column, val1, val2);
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
    <R> Children joinNotBetween(boolean condition, R column, Object val1, Object val2);

    /**
     * ignore
     */
    default <R> Children joinLike(R column, Object val) {
        return joinLike(true, column, val);
    }

    /**
     * LIKE '%值%'
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <R> Children joinLike(boolean condition, R column, Object val);

    /**
     * ignore
     */
    default <R> Children joinNotLike(R column, Object val) {
        return joinNotLike(true, column, val);
    }

    /**
     * NOT LIKE '%值%'
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <R> Children joinNotLike(boolean condition, R column, Object val);

    /**
     * ignore
     */
    default <R> Children joinNotLikeLeft(R column, Object val) {
        return joinNotLikeLeft(true, column, val);
    }

    /**
     * NOT LIKE '%值'
     *
     * @param condition
     * @param column
     * @param val
     * @return children
     */
    <R> Children joinNotLikeLeft(boolean condition, R column, Object val);

    /**
     * ignore
     */
    default <R> Children joinNotLikeRight(R column, Object val) {
        return joinNotLikeRight(true, column, val);
    }

    /**
     * NOT LIKE '值%'
     *
     * @param condition
     * @param column
     * @param val
     * @return children
     */
    <R> Children joinNotLikeRight(boolean condition, R column, Object val);

    /**
     * ignore
     */
    default <R> Children joinLikeLeft(R column, Object val) {
        return joinLikeLeft(true, column, val);
    }

    /**
     * LIKE '%值'
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <R> Children joinLikeLeft(boolean condition, R column, Object val);

    /**
     * ignore
     */
    default <R> Children joinLikeRight(R column, Object val) {
        return joinLikeRight(true, column, val);
    }

    /**
     * LIKE '值%'
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <R> Children joinLikeRight(boolean condition, R column, Object val);
}
