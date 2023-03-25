package com.iv.ersr.reptile.mapper;

import com.iv.ersr.mybatisplus.mapper.BaseMapperPlus;
import com.iv.ersr.reptile.entity.GameRentalInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 游戏租赁信息 Mapper 接口
 * </p>
 *
 * @author moxiaoyu
 * @since 2022-05-12
 */
public interface GameRentalInfoMapper extends BaseMapperPlus<GameRentalInfo> {

    /**
     * 物理删除
     * @param gameIds 传入参数
     * @return Boolean
     */
    Boolean deleteByIds(@Param("gameIds") List<Long> gameIds);
}
