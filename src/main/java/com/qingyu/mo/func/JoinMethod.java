package com.qingyu.mo.func;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.qingyu.mo.wrapper.query.JoinLambdaQueryWrapper;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * <p>
 * 自定义的的扩展方法
 * </p>
 *
 * @author qingyu-mo
 * @since 2023-03-17
 */
@SuppressWarnings("unused")
public interface JoinMethod<Children, R> extends Serializable {

    /**
     * join连接条件 嵌套
     * @param consumer 消费函数
     * @return children
     */
    default Children joinOn(Consumer<Children> consumer) {
        return joinOn(true, consumer);
    }

    /**
     * join连接条件 嵌套
     * <p>
     * 例: joinOn(i -&gt; i.jEq("name", "李白").jNe("status", "活着"))
     * </p>
     * @param condition 执行条件j
     * @param consumer  消费函数
     * @return children
     */
    Children joinOn(boolean condition, Consumer<Children> consumer);

    /**
     * join连接条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children joinOn(R column, SFunction<J, ?> column2) {
        return joinOn(true, column, column2);
    }

    /**
     * join连接条件
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children joinOn(boolean condition, R column, SFunction<J, ?> column2);

    /**
     * join连接条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jJoinOn(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jJoinOn(true, column, column2);
    }

    /**
     * join连接条件
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    <J> Children jJoinOn(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2);

    /**
     * inner join 处理
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children innerJoin(R column, SFunction<J, ?> column2) {
        return innerJoin(true, column, column2);
    }

    /**
     * inner join 处理
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children innerJoin(boolean condition, R column, SFunction<J, ?> column2) {
        return innerJoin(condition, column, column2, null);
    }

    /**
     * inner join 处理
     * @param column 字段
     * @param column2 字段
     * @param alias 关联的表别名
     * @return children
     */
    default <J> Children innerJoin(R column, SFunction<J, ?> column2, String alias) {
        return innerJoin(true, column, column2, alias);
    }

    /**
     * inner join 处理
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @param alias 关联的表别名
     * @return children
     */
    <J> Children innerJoin(boolean condition, R column, SFunction<J, ?> column2, String alias);

    /**
     * inner join 处理
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J, K> Children jInnerJoin(SFunction<J, ?> column, SFunction<K, ?> column2) {
        return jInnerJoin(true, column, column2);
    }

    /**
     * inner join 处理
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J, K> Children jInnerJoin(boolean condition, SFunction<J, ?> column, SFunction<K, ?> column2) {
        return jInnerJoin(condition, column, column2, null);
    }

    /**
     * inner join 处理
     * @param column 字段
     * @param column2 字段
     * @param alias 关联的表别名
     * @return children
     */
    default <J, K> Children jInnerJoin(SFunction<J, ?> column, SFunction<K, ?> column2, String alias) {
        return jInnerJoin(true, column, column2, alias);
    }

    /**
     * inner join 处理
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @param alias 关联的表别名
     * @return children
     */
    <J, K> Children jInnerJoin(boolean condition, SFunction<J, ?> column, SFunction<K, ?> column2, String alias);

    /**
     * inner join 处理
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 字段
     * @param entityClass 子查询主表实体类
     * @param consumer 关联的子查询
     * @param joinOn 关联到子查询的条件
     * @return children
     */
    default <J> Children innerJoin(R column, Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer, String joinOn) {
        return innerJoin(true, column, entityClass, consumer, joinOn);
    }

    /**
     * inner join 处理
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 字段
     * @param entityClass 子查询主表实体类
     * @param consumer 关联的子查询
     * @param joinOn 关联到子查询的条件
     * @return children
     */
    default <J> Children innerJoin(boolean condition, R column, Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer, String joinOn) {
        return innerJoin(condition, column, entityClass, consumer, null, joinOn);
    }

    /**
     * inner join 处理
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 字段
     * @param entityClass 子查询主表实体类
     * @param consumer 关联的子查询
     * @param alias 关联的子查询的别名
     * @param joinOn 关联到子查询的条件
     * @return children
     */
    <J> Children innerJoin(boolean condition, R column, Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer, String alias, String joinOn);

    /**
     * inner join 处理
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 字段
     * @param entityClass 子查询主表实体类
     * @param consumer 关联的子查询
     * @param joinOn 关联到子查询的条件
     * @return children
     */
    default <J> Children jInnerJoin(SFunction<J, ?> column, Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer, String joinOn) {
        return jInnerJoin(true, column, entityClass, consumer, joinOn);
    }

    /**
     * inner join 处理
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 字段
     * @param entityClass 子查询主表实体类
     * @param consumer 关联的子查询
     * @param joinOn 关联到子查询的条件
     * @return children
     */
    default <J> Children jInnerJoin(boolean condition, SFunction<J, ?> column, Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer, String joinOn) {
        return jInnerJoin(condition, column, entityClass, consumer, null, joinOn);
    }

    /**
     * inner join 处理
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 字段
     * @param entityClass 子查询主表实体类
     * @param consumer 关联的子查询
     * @param alias 关联的子查询的别名
     * @param joinOn 关联到子查询的条件
     * @return children
     */
    <J> Children jInnerJoin(boolean condition, SFunction<J, ?> column, Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer, String alias, String joinOn);

    /**
     * left join 处理
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children leftJoin(R column, SFunction<J, ?> column2) {
        return leftJoin(true, column, column2);
    }

    /**
     * left join 处理
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children leftJoin(boolean condition, R column, SFunction<J, ?> column2) {
        return leftJoin(condition, column, column2, null);
    }

    /**
     * left join 处理
     * @param column 字段
     * @param column2 字段
     * @param alias 关联的表别名
     * @return children
     */
    default <J> Children leftJoin(R column, SFunction<J, ?> column2, String alias) {
        return leftJoin(true, column, column2, alias);
    }

    /**
     * left join 处理
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @param alias 关联的表别名
     * @return children
     */
    <J> Children leftJoin(boolean condition, R column, SFunction<J, ?> column2, String alias);

    /**
     * left join 处理
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J, K> Children jLeftJoin(SFunction<J, ?> column, SFunction<K, ?> column2) {
        return jLeftJoin(true, column, column2);
    }

    /**
     * left join 处理
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J, K> Children jLeftJoin(boolean condition, SFunction<J, ?> column, SFunction<K, ?> column2) {
        return jLeftJoin(condition, column, column2, null);
    }

    /**
     * left join 处理
     * @param column 字段
     * @param column2 字段
     * @param alias 关联的表别名
     * @return children
     */
    default <J, K> Children jLeftJoin(SFunction<J, ?> column, SFunction<K, ?> column2, String alias) {
        return jLeftJoin(true, column, column2, alias);
    }

    /**
     * left join 处理
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @param alias 关联的表别名
     * @return children
     */
    <J, K> Children jLeftJoin(boolean condition, SFunction<J, ?> column, SFunction<K, ?> column2, String alias);

    /**
     * left join 处理
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 字段
     * @param entityClass 子查询主表实体类
     * @param consumer 关联的子查询
     * @param joinOn 关联到子查询的条件
     * @return children
     */
    default <J> Children leftJoin(R column, Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer, String joinOn) {
        return leftJoin(true, column, entityClass, consumer, joinOn);
    }

    /**
     * left join 处理
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 字段
     * @param entityClass 子查询主表实体类
     * @param consumer 关联的子查询
     * @param joinOn 关联到子查询的条件
     * @return children
     */
    default <J> Children leftJoin(boolean condition, R column, Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer, String joinOn) {
        return leftJoin(condition, column, entityClass, consumer, null, joinOn);
    }

    /**
     * left join 处理
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 字段
     * @param entityClass 子查询主表实体类
     * @param consumer 关联的子查询
     * @param alias 关联的子查询的别名
     * @param joinOn 关联到子查询的条件
     * @return children
     */
    <J> Children leftJoin(boolean condition, R column, Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer, String alias, String joinOn);

    /**
     * left join 处理
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 字段
     * @param entityClass 子查询主表实体类
     * @param consumer 关联的子查询
     * @param joinOn 关联到子查询的条件
     * @return children
     */
    default <J> Children jLeftJoin(SFunction<J, ?> column, Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer, String joinOn) {
        return jLeftJoin(true, column, entityClass, consumer, joinOn);
    }

    /**
     * left join 处理
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 字段
     * @param entityClass 子查询主表实体类
     * @param consumer 关联的子查询
     * @param joinOn 关联到子查询的条件
     * @return children
     */
    default <J> Children jLeftJoin(boolean condition, SFunction<J, ?> column, Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer, String joinOn) {
        return jLeftJoin(condition, column, entityClass, consumer, null, joinOn);
    }

    /**
     * left join 处理
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 字段
     * @param entityClass 子查询主表实体类
     * @param consumer 关联的子查询
     * @param alias 关联的子查询的别名
     * @param joinOn 关联到子查询的条件
     * @return children
     */
    <J> Children jLeftJoin(boolean condition, SFunction<J, ?> column, Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer, String alias, String joinOn);

    /**
     * right join 处理
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children rightJoin(R column, SFunction<J, ?> column2) {
        return rightJoin(true, column, column2);
    }

    /**
     * right join 处理
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children rightJoin(boolean condition, R column, SFunction<J, ?> column2) {
        return rightJoin(condition, column, column2, null);
    }

    /**
     * right join 处理
     * @param column 字段
     * @param column2 字段
     * @param alias 关联的表别名
     * @return children
     */
    default <J> Children rightJoin(R column, SFunction<J, ?> column2, String alias) {
        return rightJoin(true, column, column2, alias);
    }

    /**
     * right join 处理
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @param alias 关联的表别名
     * @return children
     */
    <J> Children rightJoin(boolean condition, R column, SFunction<J, ?> column2, String alias);

    /**
     * right join 处理
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J> Children jRightJoin(SFunction<J, ?> column, SFunction<J, ?> column2) {
        return jRightJoin(true, column, column2);
    }

    /**
     * right join 处理
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @return children
     */
    default <J, K> Children jRightJoin(boolean condition, SFunction<J, ?> column, SFunction<K, ?> column2) {
        return jRightJoin(condition, column, column2, null);
    }

    /**
     * right join 处理
     * @param column 字段
     * @param column2 字段
     * @param alias 关联的表别名
     * @return children
     */
    default <J, K> Children jRightJoin(SFunction<J, ?> column, SFunction<K, ?> column2, String alias) {
        return jRightJoin(true, column, column2, alias);
    }

    /**
     * right join 处理
     * @param condition 执行条件
     * @param column 字段
     * @param column2 字段
     * @param alias 关联的表别名
     * @return children
     */
    <J, K> Children jRightJoin(boolean condition, SFunction<J, ?> column, SFunction<K, ?> column2, String alias);

    /**
     * right join 处理
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 字段
     * @param entityClass 子查询主表实体类
     * @param consumer 关联的子查询
     * @param joinOn 关联到子查询的条件
     * @return children
     */
    default <J> Children rightJoin(R column, Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer, String joinOn) {
        return rightJoin(true, column, entityClass, consumer, joinOn);
    }

    /**
     * right join 处理
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 字段
     * @param entityClass 子查询主表实体类
     * @param consumer 关联的子查询
     * @param joinOn 关联到子查询的条件
     * @return children
     */
    default <J> Children rightJoin(boolean condition, R column, Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer, String joinOn) {
        return rightJoin(condition, column, entityClass, consumer, null, joinOn);
    }

    /**
     * right join 处理
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 字段
     * @param entityClass 子查询主表实体类
     * @param consumer 关联的子查询
     * @param alias 关联的子查询的别名
     * @param joinOn 关联到子查询的条件
     * @return children
     */
    <J> Children rightJoin(boolean condition, R column, Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer, String alias, String joinOn);

    /**
     * right join 处理
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param column 字段
     * @param entityClass 子查询主表实体类
     * @param consumer 关联的子查询
     * @param joinOn 关联到子查询的条件
     * @return children
     */
    default <J> Children jRightJoin(SFunction<J, ?> column, Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer, String joinOn) {
        return jRightJoin(true, column, entityClass, consumer, joinOn);
    }

    /**
     * right join 处理
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 字段
     * @param entityClass 子查询主表实体类
     * @param consumer 关联的子查询
     * @param joinOn 关联到子查询的条件
     * @return children
     */
    default <J> Children jRightJoin(boolean condition, SFunction<J, ?> column, Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer, String joinOn) {
        return jRightJoin(condition, column, entityClass, consumer, null, joinOn);
    }

    /**
     * right join 处理
     * <p>注意：只有内部有 entity 才能使用该方法</p>
     * @param condition 执行条件
     * @param column 字段
     * @param entityClass 子查询主表实体类
     * @param consumer 关联的子查询
     * @param alias 关联的子查询的别名
     * @param joinOn 关联到子查询的条件
     * @return children
     */
    <J> Children jRightJoin(boolean condition, SFunction<J, ?> column, Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer, String alias, String joinOn);
}