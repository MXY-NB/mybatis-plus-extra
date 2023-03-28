package com.iv.ersr.common.controller;

import cn.hutool.json.JSONUtil;
import com.iv.ersr.common.entity.Response;
import com.iv.ersr.game.entity.GameRentalInfo;
import com.iv.ersr.game.mapper.GameRentalInfoMapper;
import com.iv.ersr.game.service.IGameRentalDetailService;
import com.iv.ersr.game.service.IGameRentalInfoService;
import com.iv.ersr.mybatisplus.core.toolkit.JoinWrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 用户信息 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2019-07-09
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController extends BaseController {

    @Resource
    private IGameRentalInfoService gameRentalInfoService;

    @Resource
    private GameRentalInfoMapper gameRentalInfoMapper;

    @Resource
    private IGameRentalDetailService gameRentalDetailService;

    /**
     * 测试
     * @author MXY
     * @date 2021/12/15
     * @return Object
     */
    @PostMapping(path = "/test")
    public Object test() {
//        List<GameRentalInfo> gameRentalInfos = gameRentalInfoService.list(Wrappers.lambdaQuery(GameRentalInfo.class)
//                .select(GameRentalInfo::getId)
//                .last("LIMIT 10")
//        );
        GameRentalInfo gameRentalInfo2 = gameRentalInfoService.joinGetOne(JoinWrappers.<GameRentalInfo>lambdaQuery()
                        .select(GameRentalInfo::getId, GameRentalInfo::getChineseName)
                        .eq(GameRentalInfo::getId, 1627561102696390658L)
//                .joinEq(Game::getChineseName, 1L)
        );
        if (gameRentalInfo2 != null && gameRentalInfo2.getGameRentalDetails()!= null) {
            log.error("mapper: ================ \n" + JSONUtil.toJsonPrettyStr(gameRentalInfo2));
        }
        return Response.createSuccResponse();
    }
}
