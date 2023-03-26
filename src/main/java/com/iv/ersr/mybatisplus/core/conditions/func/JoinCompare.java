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

import com.baomidou.mybatisplus.core.conditions.interfaces.Compare;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * 查询条件封装
 * <p>比较值</p>
 *
 * @author moxiaoyu
 * @since 2023-03-22
 */
public interface JoinCompare<Children, R> extends Compare<Children, R> {

    default <J> Children jEq(SFunction<J, ?> column, Object val) {
        return jEq(true, column, val);
    }

    /**
     * 等于 =
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return Children
     */
    <J> Children jEq(boolean condition, SFunction<J, ?> column, Object val);

    default <J> Children jNe(SFunction<J, ?> column, Object val) {
        return jNe(true, column, val);
    }

    /**
     * 不等于 &lt;&gt;
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return Children
     */
    <J> Children jNe(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * ignore
     */
    default <J> Children jGt(SFunction<J, ?> column, Object val) {
        return jGt(true, column, val);
    }

    /**
     * 大于 &gt;
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return Children
     */
    <J> Children jGt(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * ignore
     */
    default <J> Children jGe(SFunction<J, ?> column, Object val) {
        return jGe(true, column, val);
    }

    /**
     * 大于等于 &gt;=
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return Children
     */
    <J> Children jGe(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * ignore
     */
    default <J> Children jLt(SFunction<J, ?> column, Object val) {
        return jLt(true, column, val);
    }

    /**
     * 小于 &lt;
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return Children
     */
    <J> Children jLt(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * ignore
     */
    default <J> Children jLe(SFunction<J, ?> column, Object val) {
        return jLe(true, column, val);
    }

    /**
     * 小于等于 &lt;=
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return Children
     */
    <J> Children jLe(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * ignore
     */
    default <J> Children jLike(SFunction<J, ?> column, Object val) {
        return jLike(true, column, val);
    }

    /**
     * LIKE '%值%'
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return Children
     */
    <J> Children jLike(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * ignore
     */
    default <J> Children jLikeLeft(SFunction<J, ?> column, Object val) {
        return jLikeLeft(true, column, val);
    }

    /**
     * LIKE '%值'
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return Children
     */
    <J> Children jLikeLeft(boolean condition, SFunction<J, ?> column, Object val);

    /**
     * ignore
     */
    default <J> Children jLikeRight(SFunction<J, ?> column, Object val) {
        return jLikeRight(true, column, val);
    }

    /**
     * LIKE '值%'
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return Children
     */
    <J> Children jLikeRight(boolean condition, SFunction<J, ?> column, Object val);
}
