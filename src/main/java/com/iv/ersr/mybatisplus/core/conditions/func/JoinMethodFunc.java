package com.iv.ersr.mybatisplus.core.conditions.func;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.iv.ersr.mybatisplus.core.entity.CollectionResultMap;

import java.io.Serializable;

/**
 * <p>
 * JoinWrappers相关方法
 * </p>
 *
 * @author moxiaoyu
 * @since 2023-03-17
 */
public interface JoinMethodFunc<Children, T> extends Serializable {

    /**
     * left join 处理
     * @param masterTableField 主表关联表字段
     * @param joinTableField 需要关联的表字段
     * @param alias 关联的表别名
     * @return Children
     */
    default <J> Children leftJoin(SFunction<T, ?> masterTableField, SFunction<J, ?> joinTableField, String alias) {
        return leftJoin(true, masterTableField, joinTableField, alias);
    }

    /**
     * left join 处理
     * @param condition 执行条件
     * @param masterTableField 主表关联表字段
     * @param joinTableField 需要关联的表字段
     * @param alias 关联的表别名
     * @return Children
     */
    <J> Children leftJoin(boolean condition, SFunction<T, ?> masterTableField, SFunction<J, ?> joinTableField, String alias);

    /**
     * right join 处理
     * @param masterTableField 主表关联表字段
     * @param joinTableField 需要关联的表字段
     * @param alias 关联的表别名
     * @return Children
     */
    default <J> Children rightJoin(SFunction<T, ?> masterTableField, SFunction<J, ?> joinTableField, String alias) {
        return rightJoin(true, masterTableField, joinTableField, alias);
    }

    /**
     * right join 处理
     * @param condition 执行条件
     * @param masterTableField 主表关联表字段
     * @param joinTableField 需要关联的表字段
     * @param alias 关联的表别名
     * @return Children
     */
    <J> Children rightJoin(boolean condition, SFunction<T, ?> masterTableField, SFunction<J, ?> joinTableField, String alias);

    /**
     * join查询字段
     * @param columns 需要查询的关联的表字段
     * @return Children
     */
    <J> Children jSelect(SFunction<J, ?>... columns);

    /**
     * join查询字段
     * @return Children
     */
    Children jSelect();

    /**
     * join查询字段
     * @param collectionResultMap 需要查询的关联的表字段
     */
    Children coll(CollectionResultMap collectionResultMap);
}