package com.iv.ersr.mybatisplus.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.iv.ersr.mybatisplus.core.toolkit.JoinWrappers;
import com.iv.ersr.mybatisplus.mapper.BaseMapperPlus;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 自定义Service
 * </p>
 *
 * @author moxiaoyu
 * @since 2023-03-22
 */
public interface IServicePlus<T> extends IService<T> {

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
     * 多表查询单个
     * @param queryWrapper 条件包装
     * @param <E> 适配各种传入类型
     * @return 返回对象list
     */
    default <E> E joinGetOne(Wrapper<E> queryWrapper) {
        return getBaseMapper().joinSelectOne(queryWrapper);
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
    default <E> long joinCount(Wrapper<E> queryWrapper) {
        return SqlHelper.retCount(getBaseMapper().selectJoinCount(queryWrapper));
    }

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
    @Transactional(rollbackFor = Exception.class)
    default boolean updateBatchById(Collection<T> entityList) {
        return getBaseMapper().batchUpdateById(entityList, DEFAULT_BATCH_SIZE);
    }

    /**
     * 获取对应 entity 的 BaseMapper
     * @return BaseMapper
     */
    @Override
    BaseMapperPlus<T> getBaseMapper();
}
