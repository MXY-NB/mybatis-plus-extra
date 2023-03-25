package com.iv.ersr.reptile.service;

import com.iv.ersr.mybatisplus.service.IServicePlus;
import com.iv.ersr.reptile.entity.GameRentalInfo;

import java.util.List;

/**
 * <p>
 * 游戏租赁信息 服务类
 * </p>
 *
 * @author moxiaoyu
 * @since 2022-05-12
 */
public interface IGameRentalInfoService extends IServicePlus<GameRentalInfo> {

    /**
     * 物理删除
     * @author MXY
     * @date 2021/12/31
     * @param gameIds 传入参数
     * @return Boolean
     */
    Boolean deleteByIds(List<Long> gameIds);
}
