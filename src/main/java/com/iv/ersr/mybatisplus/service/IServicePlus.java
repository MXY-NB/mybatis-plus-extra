package com.iv.ersr.mybatisplus.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
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
     * @param wrapper 条件包装
     * @param <E> 适配各种传入类型
     * @return 返回对象list
     */
    default <E> List<E> joinList(Wrapper<E> wrapper) {
        return getBaseMapper().joinSelectList(wrapper);
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
