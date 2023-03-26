package com.iv.ersr.game.service.impl;

import com.iv.ersr.game.entity.GameRentalInfo;
import com.iv.ersr.game.mapper.GameRentalInfoMapper;
import com.iv.ersr.game.service.IGameRentalInfoService;
import com.iv.ersr.mybatisplus.service.impl.ServiceImplPlus;
import org.springframework.stereotype.Service;

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
}
