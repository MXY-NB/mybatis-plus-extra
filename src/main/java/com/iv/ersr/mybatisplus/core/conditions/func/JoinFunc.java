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

import com.baomidou.mybatisplus.core.conditions.interfaces.Func;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.Collection;

/**
 * 查询条件封装
 *
 * @author moxiaoyu
 * @since 2023-03-22
 */
public interface JoinFunc<Children, R> extends Func<Children, R> {

    /**
     * ignore
     */
    default <J> Children jIn(SFunction<J, ?> column, Collection<?> coll) {
        return jIn(true, column, coll);
    }

    /**
     * 字段 IN (value.get(0), value.get(1), ...)
     * <p>例: in("id", Arrays.asList(1, 2, 3, 4, 5))</p>
     *
     * <li> 注意！集合为空若存在逻辑错误，请在 condition 条件中判断 </li>
     * <li> 如果集合为 empty 则不会进行 sql 拼接 </li>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param coll      数据集合
     * @return children
     */
    <J> Children jIn(boolean condition, SFunction<J, ?> column, Collection<?> coll);

    /**
     * ignore
     */
    default <J> Children jIn(SFunction<J, ?> column, Object... values) {
        return jIn(true, column, values);
    }

    /**
     * 字段 IN (v0, v1, ...)
     * <p>例: in("id", 1, 2, 3, 4, 5)</p>
     *
     * <li> 注意！数组为空若存在逻辑错误，请在 condition 条件中判断 </li>
     * <li> 如果动态数组为 empty 则不会进行 sql 拼接 </li>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param values    数据数组
     * @return children
     */
    <J> Children jIn(boolean condition, SFunction<J, ?> column, Object... values);

    /**
     * ignore
     */
    default <J> Children jNotIn(SFunction<J, ?> column, Collection<?> coll) {
        return jNotIn(true, column, coll);
    }

    /**
     * 字段 NOT IN (value.get(0), value.get(1), ...)
     * <p>例: notIn("id", Arrays.asList(1, 2, 3, 4, 5))</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param coll      数据集合
     * @return children
     */
    <J> Children jNotIn(boolean condition, SFunction<J, ?> column, Collection<?> coll);

    /**
     * ignore
     */
    default <J> Children jNotIn(SFunction<J, ?> column, Object... value) {
        return jNotIn(true, column, value);
    }

    /**
     * 字段 NOT IN (v0, v1, ...)
     * <p>例: notIn("id", 1, 2, 3, 4, 5)</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param values    数据数组
     * @return children
     */
    <J> Children jNotIn(boolean condition, SFunction<J, ?> column, Object... values);
}
