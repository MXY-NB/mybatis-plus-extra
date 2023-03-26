package com.iv.ersr.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 自定义Mapper
 * </p>
 *
 * @author moxiaoyu
 * @since 2023-03-22
 */
public interface BaseMapperPlus<T> extends BaseMapper<T> {

    /**
     * 应为mysql对于太长的sql语句是有限制的，所以我这里设置每1000条批量插入拼接sql
     */
    int DEFAULT_BATCH_SIZE = 1000;

    /**
     * 多表查询列表
     * @param queryWrapper 条件包装
     * @param <E> 适配各种传入类型
     * @return 返回包装类型的对象list
     */
    <E> List<E> joinSelectList(@Param(Constants.WRAPPER) Wrapper<E> queryWrapper);

    /**
     * 多表查询列表
     * @param queryWrapper 条件包装
     * @param <E> 适配各种传入类型
     * @return 返回包装类型的对象
     */
    default <E> E joinSelectOne(@Param(Constants.WRAPPER) Wrapper<E> queryWrapper){
        List<E> list = this.joinSelectList(queryWrapper);
        // 抄自 DefaultSqlSession#selectOne
        if (list.size() == 1) {
            return list.get(0);
        } else if (list.size() > 1) {
            throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
        } else {
            return null;
        }
    }

    /**
     * 多表查询分页
     * @param page 分页参数
     * @param queryWrapper 条件包装
     * @param <E> 适配各种传入类型
     * @return 返回包装类型的对象
     */
    <E, P extends IPage<E>> P joinSelectPage(P page, @Param(Constants.WRAPPER) Wrapper<E> queryWrapper);

    /**
     * 多表查询总记录数
     * @param queryWrapper 条件包装
     * @param <E> 适配各种传入类型
     * @return 总记录数
     */
    <E> long selectJoinCount(@Param(Constants.WRAPPER) Wrapper<E> queryWrapper);

    /**
     * 批量插入
     * @param list 实体列表
     * @return int 影响行数
     */
    int insertList(Collection<T> list);

    /**
     * 批量更新
     * @param list 实体列表
     * @return boolean 是否成功
     */
    boolean updateBatchById(Collection<T> list);

    /**
     * 批量更新
     * @param list 实体列表
     * @return boolean 是否成功
     */
    boolean updateBatchByIdWithNull(Collection<T> list);

    /**
     * 批量插入
     * @param list 实体列表
     * @param batchSize 一次性插入数量
     * @return int 影响行数
     */
    default int batchInsert(Collection<T> list, int batchSize) {
        Assert.isFalse(batchSize < 1, "batchSize must not be less than one");
        int result = 0;
        Collection<T> tempEntityList = new ArrayList<>();
        int i = 0;
        for (T entity : list) {
            tempEntityList.add(entity);
            if (i > 0 && (i % batchSize == 0)) {
                result += insertList(tempEntityList);
                tempEntityList.clear();
            }
            i++;
        }
        result += insertList(tempEntityList);
        return result;
    }

    /**
     * 批量更新
     * @param list 实体列表
     * @param batchSize 一次性更新数量
     * @return int 影响行数
     */
    default boolean batchUpdateById(Collection<T> list, int batchSize) {
        Assert.isFalse(batchSize < 1, "更新数量必须大于1...");
        Assert.isFalse(list.isEmpty(), "更新集合不能为空...");
        boolean flag = true;
        Collection<T> tempEntityList = new ArrayList<>();
        int i = 0;
        for (T entity : list) {
            tempEntityList.add(entity);
            if (i > 0 && (i % batchSize == 0)) {
                boolean update = updateBatchById(tempEntityList);
                if (!update) {
                    flag = false;
                }
                tempEntityList.clear();
            }
            i++;
        }
        boolean update = updateBatchById(tempEntityList);
        if (!update) {
            flag = false;
        }
        return flag;
    }

    /**
     * 批量更新
     * @param list 实体列表
     * @param batchSize 一次性更新数量
     * @return int 影响行数
     */
    default boolean batchUpdateByIdWithNull(Collection<T> list, int batchSize) {
        Assert.isFalse(batchSize < 1, "batchSize must not be less than one");
        boolean flag = true;
        Collection<T> tempEntityList = new ArrayList<>();
        int i = 0;
        for (T entity : list) {
            tempEntityList.add(entity);
            if (i > 0 && (i % batchSize == 0)) {
                boolean update = updateBatchByIdWithNull(tempEntityList);
                if (!update) {
                    flag = false;
                }
                tempEntityList.clear();
            }
            i++;
        }
        boolean update = updateBatchByIdWithNull(tempEntityList);
        if (!update) {
            flag = false;
        }
        return flag;
    }

    /**
     * 批量插入
     * @param list 实体列表
     * @return int 影响行数
     */
    @Transactional(rollbackFor = Exception.class)
    default int batchInsert(Collection<T> list) {
        return batchInsert(list, DEFAULT_BATCH_SIZE);
    }

    /**
     * 批量更新
     * @param list 实体列表
     * @return boolean 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean batchUpdateById(Collection<T> list) {
        return batchUpdateById(list, DEFAULT_BATCH_SIZE);
    }

    /**
     * 批量更新
     * @param list 实体列表
     * @return boolean 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean batchUpdateByIdWithNull(Collection<T> list) {
        return batchUpdateByIdWithNull(list, DEFAULT_BATCH_SIZE);
    }
}