package com.iv.ersr.common.controller;

import cn.hutool.json.JSONUtil;
import com.iv.ersr.common.entity.Response;
import com.iv.ersr.mybatisplus.core.toolkit.JoinWrappers;
import com.iv.ersr.reptile.entity.GameRentalDetail;
import com.iv.ersr.reptile.entity.GameRentalInfo;
import com.iv.ersr.reptile.service.IGameRentalDetailService;
import com.iv.ersr.reptile.service.IGameRentalInfoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户信息 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2019-07-09
 */
@RestController
@RequestMapping("/common")
public class CommonController extends BaseController {

    @Resource
    private IGameRentalInfoService gameRentalInfoService;

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
        List<GameRentalInfo> gameRentalInfos2 = gameRentalInfoService.joinList(JoinWrappers.<GameRentalInfo>lambdaQuery()
                .select(GameRentalInfo::getChineseName)
                .eq(GameRentalInfo::getId, 1L)
                .leftJoin(GameRentalInfo::getId, GameRentalDetail::getGameId, "t2")
                .eq(GameRentalDetail::getGameId, 1L)
                .last("LIMIT 10")
        );
        JSONUtil.toJsonPrettyStr(gameRentalInfos2);
        return Response.createSuccResponse();
    }
}
