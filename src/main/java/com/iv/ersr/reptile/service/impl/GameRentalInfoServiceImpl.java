package com.iv.ersr.reptile.service.impl;

import com.iv.ersr.mybatisplus.service.impl.ServiceImplPlus;
import com.iv.ersr.reptile.entity.GameRentalInfo;
import com.iv.ersr.reptile.mapper.GameRentalInfoMapper;
import com.iv.ersr.reptile.service.IGameRentalInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 游戏租赁信息 服务实现类
 * </p>
 *
 * @author moxiaoyu
 * @since 2022-05-12
 */
@Service
public class GameRentalInfoServiceImpl extends ServiceImplPlus<GameRentalInfoMapper, GameRentalInfo> implements IGameRentalInfoService {

    /**
     * 物理删除
     * @author MXY
     * @date 2021/12/31
     * @param gameIds 传入参数
     * @return Boolean
     */
    @Override
    public Boolean deleteByIds(List<Long> gameIds) {
        return this.baseMapper.deleteByIds(gameIds);
    }
}
