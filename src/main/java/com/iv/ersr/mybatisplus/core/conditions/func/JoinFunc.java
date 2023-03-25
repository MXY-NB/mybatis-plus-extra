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
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * 查询条件封装
 *
 * @author hubin miemie HCL
 * @since 2017-05-26
 */
@SuppressWarnings("unchecked")
public interface JoinFunc<Children> extends Serializable {

    /**
     * ignore
     */
    default <R> Children joinIsNull(R column) {
        return joinIsNull(true, column);
    }

    /**
     * 字段 IS NULL
     * <p>例: joinIsNull("name")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @return children
     */
    <R> Children joinIsNull(boolean condition, R column);

    /**
     * ignore
     */
    default <R> Children joinIsNotNull(R column) {
        return joinIsNotNull(true, column);
    }

    /**
     * 字段 IS NOT NULL
     * <p>例: joinIsNotNull("name")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @return children
     */
    <R> Children joinIsNotNull(boolean condition, R column);

    /**
     * ignore
     */
    default <R> Children joinIn(R column, Collection<?> coll) {
        return joinIn(true, column, coll);
    }

    /**
     * 字段 IN (value.get(0), value.get(1), ...)
     * <p>例: joinIn("id", Arrays.asList(1, 2, 3, 4, 5))</p>
     *
     * <li> 注意！集合为空若存在逻辑错误，请在 condition 条件中判断 </li>
     * <li> 如果集合为 empty 则不会进行 sql 拼接 </li>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param coll      数据集合
     * @return children
     */
    <R> Children joinIn(boolean condition, R column, Collection<?> coll);

    /**
     * ignore
     */
    default <R> Children joinIn(R column, Object... values) {
        return joinIn(true, column, values);
    }

    /**
     * 字段 IN (v0, v1, ...)
     * <p>例: joinIn("id", 1, 2, 3, 4, 5)</p>
     *
     * <li> 注意！数组为空若存在逻辑错误，请在 condition 条件中判断 </li>
     * <li> 如果动态数组为 empty 则不会进行 sql 拼接 </li>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param values    数据数组
     * @return children
     */
    <R> Children joinIn(boolean condition, R column, Object... values);

    /**
     * ignore
     */
    default <R> Children joinNotIn(R column, Collection<?> coll) {
        return joinNotIn(true, column, coll);
    }

    /**
     * 字段 NOT IN (value.get(0), value.get(1), ...)
     * <p>例: joinNotIn("id", Arrays.asList(1, 2, 3, 4, 5))</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param coll      数据集合
     * @return children
     */
    <R> Children joinNotIn(boolean condition, R column, Collection<?> coll);

    /**
     * ignore
     */
    default <R> Children joinNotIn(R column, Object... value) {
        return joinNotIn(true, column, value);
    }

    /**
     * 字段 NOT IN (v0, v1, ...)
     * <p>例: joinNotIn("id", 1, 2, 3, 4, 5)</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param values    数据数组
     * @return children
     */
    <R> Children joinNotIn(boolean condition, R column, Object... values);

    /**
     * ignore
     */
    default <R> Children joinInSql(R column, String inValue) {
        return joinInSql(true, column, inValue);
    }

    /**
     * 字段 IN ( sql语句 )
     * <p>!! sql 注入方式的 in 方法 !!</p>
     * <p>例1: joinInSql("id", "1, 2, 3, 4, 5, 6")</p>
     * <p>例2: joinInSql("id", "select id from table where id &lt; 3")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param inValue   sql语句
     * @return children
     */
    <R> Children joinInSql(boolean condition, R column, String inValue);

    /**
     * 字段 &gt; ( sql语句 )
     * <p>例1: joinGtSql("id", "1, 2, 3, 4, 5, 6")</p>
     * <p>例1: joinGtSql("id", "select id from table where name = 'JunJun'")</p>
     *
     * @param condition
     * @param column
     * @param inValue
     * @return
     */
    <R> Children joinGtSql(boolean condition, R column, String inValue);

    /**
     * ignore
     */
    default <R> Children joinGtSql(R column, String inValue) {
        return joinGtSql(true, column, inValue);
    }

    /**
     * 字段 >= ( sql语句 )
     * <p>例1: joinGeSql("id", "1, 2, 3, 4, 5, 6")</p>
     * <p>例1: joinGeSql("id", "select id from table where name = 'JunJun'")</p>
     *
     * @param condition
     * @param column
     * @param inValue
     * @return
     */
    <R> Children joinGeSql(boolean condition, R column, String inValue);

    /**
     * ignore
     */
    default <R> Children joinGeSql(R column, String inValue) {
        return joinGeSql(true, column, inValue);
    }

    /**
     * 字段 &lt; ( sql语句 )
     * <p>例1: joinLtSql("id", "1, 2, 3, 4, 5, 6")</p>
     * <p>例1: joinLtSql("id", "select id from table where name = 'JunJun'")</p>
     *
     * @param condition
     * @param column
     * @param inValue
     * @return
     */
    <R> Children joinLtSql(boolean condition, R column, String inValue);

    /**
     * ignore
     */
    default <R> Children joinLtSql(R column, String inValue) {
        return joinLtSql(true, column, inValue);
    }

    /**
     * 字段 <= ( sql语句 )
     * <p>例1: joinLeSql("id", "1, 2, 3, 4, 5, 6")</p>
     * <p>例1: joinLeSql("id", "select id from table where name = 'JunJun'")</p>
     *
     * @param condition
     * @param column
     * @param inValue
     * @return
     */
    <R> Children joinLeSql(boolean condition, R column, String inValue);

    /**
     * ignore
     */
    default <R> Children joinLeSql(R column, String inValue) {
        return joinLeSql(true, column, inValue);
    }

    /**
     * ignore
     */
    default <R> Children joinNotInSql(R column, String inValue) {
        return joinNotInSql(true, column, inValue);
    }

    /**
     * 字段 NOT IN ( sql语句 )
     * <p>!! sql 注入方式的 not in 方法 !!</p>
     * <p>例1: joinNotInSql("id", "1, 2, 3, 4, 5, 6")</p>
     * <p>例2: joinNotInSql("id", "select id from table where id &lt; 3")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param inValue   sql语句 ---&gt; 1,2,3,4,5,6 或者 select id from table where id &lt; 3
     * @return children
     */
    <R> Children joinNotInSql(boolean condition, R column, String inValue);

    /**
     * 分组：GROUP BY 字段, ...
     * <p>例: joinGroupBy("id")</p>
     *
     * @param condition 执行条件
     * @param column    单个字段
     * @return children
     */
    <R> Children joinGroupBy(boolean condition, R column);

    default <R> Children joinGroupBy(R column) {
        return joinGroupBy(true, column);
    }

    /**
     * 分组：GROUP BY 字段, ...
     * <p>例: joinGroupBy(Arrays.asList("id", "name"))</p>
     *
     * @param condition 执行条件
     * @param columns   字段数组
     * @return children
     */
    <R> Children joinGroupBy(boolean condition, List<R> columns);

    default <R> Children joinGroupBy(List<R> columns) {
        return joinGroupBy(true, columns);
    }

    default <R> Children joinGroupBy(R column, R... columns) {
        return joinGroupBy(true, column, columns);
    }

    /**
     * 分组：GROUP BY 字段, ...
     */
    <R> Children joinGroupBy(boolean condition, R column, R... columns);

    /**
     * 排序：ORDER BY 字段, ... ASC
     * <p>例: joinOrderByAsc(true, "id")</p>
     *
     * @param condition 执行条件
     * @param column    单个字段
     * @return children
     */
    default <R> Children joinOrderByAsc(boolean condition, R column) {
        return joinOrderBy(condition, true, column);
    }

    default <R> Children joinOrderByAsc(R column) {
        return joinOrderByAsc(true, column);
    }

    /**
     * 排序：ORDER BY 字段, ... ASC
     * <p>例: joinOrderByAsc(true, Arrays.asList("id", "name"))</p>
     *
     * @param condition 执行条件
     * @param columns   字段数组
     * @return children
     */
    default <R> Children joinOrderByAsc(boolean condition, List<R> columns) {
        return joinOrderBy(condition, true, columns);
    }

    default <R> Children joinOrderByAsc(List<R> columns) {
        return joinOrderByAsc(true, columns);
    }

    default <R> Children joinOrderByAsc(R column, R... columns) {
        return joinOrderByAsc(true, column, columns);
    }

    /**
     * 排序：ORDER BY 字段, ... ASC
     */
    default <R> Children joinOrderByAsc(boolean condition, R column, R... columns) {
        return joinOrderBy(condition, true, column, columns);
    }

    /**
     * 排序：ORDER BY 字段, ... DESC
     * <p>例: joinOrderByDesc(true, "id")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @return children
     */
    default <R> Children joinOrderByDesc(boolean condition, R column) {
        return joinOrderBy(condition, false, column);
    }

    default <R> Children joinOrderByDesc(R column) {
        return joinOrderByDesc(true, column);
    }

    /**
     * 排序：ORDER BY 字段, ... DESC
     * <p>例: joinOrderByDesc(true, Arrays.asList("id", "name"))</p>
     *
     * @param condition 执行条件
     * @param columns   字段列表
     * @return children
     */
    default <R> Children joinOrderByDesc(boolean condition, List<R> columns) {
        return joinOrderBy(condition, false, columns);
    }

    default <R> Children joinOrderByDesc(List<R> columns) {
        return joinOrderByDesc(true, columns);
    }

    default <R> Children joinOrderByDesc(R column, R... columns) {
        return joinOrderByDesc(true, column, columns);
    }

    /**
     * 排序：ORDER BY 字段, ... DESC
     */
    default <R> Children joinOrderByDesc(boolean condition, R column, R... columns) {
        return joinOrderBy(condition, false, column, columns);
    }

    /**
     * 排序：ORDER BY 字段, ...
     * <p>例: joinOrderBy(true, "id")</p>
     *
     * @param condition 执行条件
     * @param isAsc     是否是 ASC 排序
     * @param column    单个字段
     * @return children
     */
    <R> Children joinOrderBy(boolean condition, boolean isAsc, R column);

    /**
     * 排序：ORDER BY 字段, ...
     * <p>例: joinOrderBy(true, Arrays.asList("id", "name"))</p>
     *
     * @param condition 执行条件
     * @param isAsc     是否是 ASC 排序
     * @param columns   字段列表
     * @return children
     */
    <R> Children joinOrderBy(boolean condition, boolean isAsc, List<R> columns);

    /**
     * 排序：ORDER BY 字段, ...
     */
    <R> Children joinOrderBy(boolean condition, boolean isAsc, R column, R... columns);

    /**
     * ignore
     */
    default <R> Children joinHaving(String sqlHaving, Object... params) {
        return joinHaving(true, sqlHaving, params);
    }

    /**
     * HAVING ( sql语句 )
     * <p>例1: joinHaving("sum(age) &gt; 10")</p>
     * <p>例2: joinHaving("sum(age) &gt; {0}", 10)</p>
     *
     * @param condition 执行条件
     * @param sqlHaving sql 语句
     * @param params    参数数组
     * @return children
     */
    <R> Children joinHaving(boolean condition, String sqlHaving, Object... params);

    /**
     * ignore
     */
    default Children joinFunc(Consumer<Children> consumer) {
        return joinFunc(true, consumer);
    }

    /**
     * 消费函数
     *
     * @param consumer 消费函数
     * @return children
     * @since 3.3.1
     */
    Children joinFunc(boolean condition, Consumer<Children> consumer);
}
