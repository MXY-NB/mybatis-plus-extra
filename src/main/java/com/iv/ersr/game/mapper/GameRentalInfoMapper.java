package com.iv.ersr.game.mapper;

import com.iv.ersr.game.entity.GameRentalInfo;
import com.iv.ersr.mybatisplus.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 游戏租赁信息 Mapper 接口
 * </p>
 *
 * @author moxiaoyu
 * @since 2022-05-12
 */
public interface GameRentalInfoMapper extends BaseMapperPlus<GameRentalInfo> {

    GameRentalInfo getOne(@Param("GameRentalInfo") GameRentalInfo gameRentalInfo);
}
