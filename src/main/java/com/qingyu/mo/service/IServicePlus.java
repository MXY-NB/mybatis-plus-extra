package com.qingyu.mo.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.qingyu.mo.mapper.BaseMapperPlus;
import com.qingyu.mo.toolkit.JoinWrappers;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 自定义Service
 * </p>
 *
 * @author qingyu-mo
 * @since 2023-12-19
 */
public interface IServicePlus<T> extends IService<T> {

    /**
     * 插入（批量）
     * @param entityList 实体对象集合
     * @return boolean 是否成功
     */
    @Override
    default boolean saveBatch(Collection<T> entityList) {
        return getBaseMapper().batchInsert(entityList) > 0;
    }

    /**
     * 根据ID 批量更新
     * @param entityList 实体对象集合
     * @return boolean 是否成功
     */
    @Override
    default boolean updateBatchById(Collection<T> entityList) {
        return getBaseMapper().batchUpdateById(entityList);
    }

    /**
     * 根据ID 批量更新
     * @param entityList 实体对象集合
     * @return boolean 是否成功
     */
    default boolean updateBatchByIdWithNull(Collection<T> entityList) {
        return getBaseMapper().batchUpdateByIdWithNull(entityList);
    }

    /**
     * 根据 Wrapper 条件，删除记录
     * @param queryWrapper 条件包装
     * @return int
     */
    default int physicalRemove(Wrapper<T> queryWrapper) {
        return getBaseMapper().physicalDelete(queryWrapper);
    }

    /**
     * 多表查询分页
     * @param page 分页参数
     * @param queryWrapper 条件包装
     * @param <E> 适配各种传入类型
     * @return 返回对象list
     */
    default <E, P extends IPage<E>> P joinPage(P page, Wrapper<E> queryWrapper) {
        return getBaseMapper().joinSelectPage(page, queryWrapper);
    }

    /**
     * 多表查询列表
     * @param queryWrapper 条件包装
     * @param <E> 适配各种传入类型
     * @return 返回对象list
     */
    default <E> List<E> joinList(Wrapper<E> queryWrapper) {
        return getBaseMapper().joinSelectList(queryWrapper);
    }

    /**
     * 多表查询列表
     * @param queryWrapper 条件包装
     * @param <E> 适配各种传入类型
     * @return 返回对象list
     */
    default <E> List<E> joinDeletedList(Wrapper<E> queryWrapper) {
        return getBaseMapper().joinSelectDeletedList(queryWrapper);
    }

    /**
     * 多表查询单个
     * @param queryWrapper 条件包装
     * @param <E> 适配各种传入类型
     * @return 返回对象list
     */
    default <E> E joinGetOne(Wrapper<E> queryWrapper) {
        return getBaseMapper().joinSelectOne(queryWrapper);
    }

    /**
     * 多表查询单个
     * @param queryWrapper 条件包装
     * @param <E> 适配各种传入类型
     * @return 返回对象list
     */
    default <E> E joinGetDeletedOne(Wrapper<E> queryWrapper) {
        return getBaseMapper().joinSelectDeletedOne(queryWrapper);
    }

    /**
     * 根据 Wrapper 条件，查询单个值
     * @param queryWrapper 条件包装
     * @return O
     */
    default <O> O getMapValue(Wrapper<T> queryWrapper) {
        return getBaseMapper().getMapValue(queryWrapper);
    }

    /**
     * 根据 Wrapper 条件，判断是否存在记录
     * @param queryWrapper 条件包装
     * @return boolean
     */
    default boolean exists(Wrapper<T> queryWrapper) {
        return getBaseMapper().exists(queryWrapper);
    }

    /**
     * 根据 Wrapper 条件，判断是否存在记录
     * @param queryWrapper 条件包装
     * @return boolean
     */
    default boolean joinExists(Wrapper<T> queryWrapper) {
        return getBaseMapper().joinExists(queryWrapper);
    }

    /**
     * 多表查询总记录数
     * @return 总记录数
     */
    default long joinCount() {
        return joinCount(JoinWrappers.emptyWrapper());
    }

    /**
     * 多表查询总记录数
     * @param queryWrapper 条件包装
     * @param <E> 适配各种传入类型
     * @return 总记录数
     */
    default <E> Long joinCount(Wrapper<E> queryWrapper) {
        return SqlHelper.retCount(getBaseMapper().joinSelectCount(queryWrapper));
    }

    /**
     * 获取对应 entity 的 BaseMapper
     * @return BaseMapper
     */
    @Override
    BaseMapperPlus<T> getBaseMapper();
}
