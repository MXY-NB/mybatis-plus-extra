package com.qingyu.mo.mapper;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * 自定义Mapper
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.6.2
 */
public interface BaseMapperPlus<T> extends BaseMapper<T> {

    /**
     * 应为mysql对于太长的sql语句是有限制的，所以我这里设置每1000条批量插入拼接sql
     */
    int DEFAULT_INSERT_BATCH_SIZE = 1000;

    int DEFAULT_UPDATE_BATCH_SIZE = 500;

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
     * 物理删除
     * @param queryWrapper 条件包装
     * @return int
     */
    int physicalDelete(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    /**
     * 根据 ID 物理删除
     * @param id 主键ID
     * @return int
     */
    int physicalDeleteById(Serializable id);

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
     * @return 返回包装类型的对象list
     */
    <E> List<E> joinSelectDeletedList(@Param(Constants.WRAPPER) Wrapper<E> queryWrapper);

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
    <E> Long joinSelectCount(@Param(Constants.WRAPPER) Wrapper<E> queryWrapper);

    /**
     * 多表查询总记录数
     * @param queryWrapper 条件包装
     * @return 总记录数
     */
    List<Map<String, Object>> joinSelectMaps(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    /**
     * 查询所有
     * @see Wrappers#emptyWrapper()
     * @return 返回包装类型的对象list
     */
    default List<T> selectList() {
        return selectList(Wrappers.emptyWrapper());
    }

    /**
     * 查询总记录数
     * @see Wrappers#emptyWrapper()
     * @return 总记录数
     */
    default long selectCount() {
        return selectCount(Wrappers.emptyWrapper());
    }

    /**
     * 批量插入
     * @param list 实体列表
     * @return int 影响行数
     */
    @Transactional(rollbackFor = Exception.class)
    default int batchInsert(Collection<T> list) {
        return batchInsert(list, DEFAULT_INSERT_BATCH_SIZE);
    }

    /**
     * 批量更新
     * @param list 实体列表
     * @return boolean 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean batchUpdateById(Collection<T> list) {
        return batchUpdateById(list, DEFAULT_UPDATE_BATCH_SIZE);
    }

    /**
     * 批量更新
     * @param list 实体列表
     * @return boolean 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean batchUpdateByIdWithNull(Collection<T> list) {
        return batchUpdateByIdWithNull(list, DEFAULT_UPDATE_BATCH_SIZE);
    }

    /**
     * 删除（根据ID或实体 批量删除）
     *
     * @param idList 主键ID列表或实体列表(不能为 null 以及 empty)
     */
    default int deleteByIds(Collection<?> idList) {
        if (CollUtil.isEmpty(idList)) {
            return 0;
        }
        return deleteBatchIds(idList);
    }

    /**
     * 批量插入
     * @param list 实体列表
     * @param batchSize 一次性插入数量
     * @return int 影响行数
     */
    default int batchInsert(Collection<T> list, int batchSize) {
        Assert.isFalse(batchSize < 1, "batchSize must not be less than one");
        int result = 0;
        if (CollUtil.isEmpty(list)) {
            return result;
        }
        List<List<T>> listList = CollUtil.split(list, batchSize);
        for (List<T> tempEntityList : listList) {
            if (CollUtil.isNotEmpty(tempEntityList)) {
                result += insertList(tempEntityList);
            }
        }
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
        if (CollUtil.isEmpty(list)) {
            return true;
        }
        boolean flag = true;
        List<List<T>> listList = CollUtil.split(list, batchSize);
        for (List<T> tempEntityList : listList) {
            if (CollUtil.isNotEmpty(tempEntityList)) {
                boolean update = updateBatchById(tempEntityList);
                if (!update) {
                    flag = false;
                }
            }
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
        List<List<T>> listList = CollUtil.split(list, batchSize);
        for (List<T> tempEntityList : listList) {
            if (CollUtil.isNotEmpty(tempEntityList)) {
                boolean update = updateBatchByIdWithNull(tempEntityList);
                if (!update) {
                    flag = false;
                }
            }
        }
        return flag;
    }

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
     * 多表查询列表
     * @param queryWrapper 条件包装
     * @param <E> 适配各种传入类型
     * @return 返回包装类型的对象
     */
    default <E> E joinSelectDeletedOne(@Param(Constants.WRAPPER) Wrapper<E> queryWrapper){
        List<E> list = this.joinSelectDeletedList(queryWrapper);
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
     * 多表查询总记录数
     * @param queryWrapper 条件包装
     * @return 总记录数
     */
    default Map<String, Object> joinSelectMap(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper){
        return getObject(this.joinSelectMaps(queryWrapper));
    }

    /**
     * 根据 Wrapper 条件，判断是否存在记录
     * @param queryWrapper 实体对象封装操作类
     * @return boolean
     */
    default boolean joinExists(Wrapper<T> queryWrapper) {
        Long count = this.joinSelectCount(queryWrapper);
        return null != count && count > 0;
    }

    /**
     * 根据 Wrapper 条件，查询sum
     * @param queryWrapper 条件包装
     * @return O
     */
    @SuppressWarnings("unchecked")
    default <O> O getMapValue(Wrapper<T> queryWrapper) {
        ArrayList<Object> values = new ArrayList<>(joinSelectMap(queryWrapper).values());
        return CollUtil.isEmpty(values) ? null : (O) values.get(0);
    }

    /**
     * 取list集合第一条
     * @param list 集合
     * @return E
     */
    default Map<String, Object> getObject(List<Map<String, Object>> list) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyMap();
        }
        int size = list.size();
        Assert.isFalse(size > 1, "Warn: execute Method There are " + size +" results.");
        CollUtil.removeNull(list);
        return CollUtil.isEmpty(list)?Collections.emptyMap():list.get(0);
    }
}