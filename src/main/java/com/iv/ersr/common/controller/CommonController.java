package com.iv.ersr.common.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.iv.ersr.common.entity.Response;
import com.iv.ersr.game.entity.GameRentalInfo;
import com.iv.ersr.game.mapper.GameRentalInfoMapper;
import com.iv.ersr.game.service.IGameRentalDetailService;
import com.iv.ersr.game.service.IGameRentalInfoService;
import com.iv.ersr.mybatisplus.core.entity.CollectionResultMap;
import com.iv.ersr.mybatisplus.core.entity.FieldMapping;
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
                        .coll(CollectionResultMap.builder().property(GameRentalInfo::getGameRentalDetails).fieldMappings(CollUtil.newArrayList(FieldMapping.builder().column("chinese_name").paramName("chineseName").build())).build())
                        .eq(GameRentalInfo::getId, 1L)
//                .joinEq(Game::getChineseName, 1L)
        );
        GameRentalInfo gameRentalInfo = gameRentalInfoMapper.getOne(new GameRentalInfo());
        log.info("mapper: ================ \n" + JSONUtil.toJsonPrettyStr(gameRentalInfo));
        if (gameRentalInfo2.getGameRentalDetails()!= null) {
            log.error("mapper: ================ \n" + JSONUtil.toJsonPrettyStr(gameRentalInfo2));
        }
        return Response.createSuccResponse();
    }
}
