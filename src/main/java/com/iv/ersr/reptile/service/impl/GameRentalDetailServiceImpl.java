package com.iv.ersr.reptile.service.impl;

import com.iv.ersr.reptile.entity.GameRentalDetail;
import com.iv.ersr.reptile.mapper.GameRentalDetailMapper;
import com.iv.ersr.reptile.service.IGameRentalDetailService;
import icu.mhb.mybatisplus.plugln.base.service.impl.JoinServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 游戏详细信息 服务实现类
 * </p>
 *
 * @author moxiaoyu
 * @since 2022-05-12
 */
@Service
public class GameRentalDetailServiceImpl extends JoinServiceImpl<GameRentalDetailMapper, GameRentalDetail> implements IGameRentalDetailService {

}
