package com.iv.ersr.reptile.controller;

import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.iv.ersr.common.controller.BaseController;
import com.iv.ersr.common.entity.Response;
import com.iv.ersr.reptile.entity.GameRentalDetail;
import com.iv.ersr.reptile.entity.GameRentalInfo;
import com.iv.ersr.reptile.pipeline.GamePipeline;
import com.iv.ersr.reptile.processor.GameProcessor;
import com.iv.ersr.reptile.service.IGameRentalDetailService;
import com.iv.ersr.reptile.service.IGameRentalInfoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import us.codecraft.webmagic.Spider;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 游戏租赁信息 前端控制器
 * </p>
 *
 * @author moxiaoyu
 * @since 2022-05-12
 */
@RestController
@RequestMapping("/reptile/game")
public class GameRentalInfoController extends BaseController {

    @Resource
    private GamePipeline gamePipeline;

    @Resource
    private IGameRentalInfoService gameRentalInfoService;

    @Resource
    private IGameRentalDetailService gameRentalDetailService;

    /**
     * 爬取游戏
     * @author MXY
     * @date 2022/05/12
     * @return Object
     */
    @PostMapping(path = "/run")
    public Object run(@RequestParam(value = "first", required = false) Boolean first, @RequestParam(value = "url", required = false) String url, @RequestParam(value = "start", required = false) Integer start, @RequestParam(value = "end", required = false) Integer end) {
        // 当需要使用seleniudownloader类时,加载webmaigc定义的属性参数,自定义指定路径
        ArrayList<String> urls;
        if (BooleanUtil.isTrue(first)) {
            urls = CollUtil.newArrayList("https://www.zhirenjoy.com/games");
        } else {
            urls = CollUtil.newArrayList();
        }

        if (start != null && end != null) {
            for (int i = start; i <= end; i++) {
                urls.add(GameProcessor.URL + i);
            }
        }

        if (url != null) {
            urls.add(url);
        }
        if (CollUtil.isNotEmpty(urls)) {
            Spider.create(new GameProcessor())
                    //从"https://github.com/code4craft"开始抓
                    .addUrl(ArrayUtil.toArray(urls, String.class))
                    .addPipeline(gamePipeline)
                    //设置浏览器核心,当网页使用动态加载图片时需要配置seleniumDownloader
                    //开启5个线程抓取
                    .thread(1)
                    //启动爬虫
                    .run();
        }
        return Response.createSuccResponse();
    }

    /**
     * 爬取游戏
     * @author MXY
     * @date 2022/05/12
     * @return Object
     */
    @PostMapping(path = "/del")
    public Object del() {
        List<GameRentalInfo> gameRentalInfos = gameRentalInfoService.list(Wrappers.<GameRentalInfo>lambdaQuery().eq(GameRentalInfo::getCreator, "System"));
        List<Long> gameIds = CollStreamUtil.toList(gameRentalInfos, GameRentalInfo::getId);
        gameRentalDetailService.remove(Wrappers.<GameRentalDetail>lambdaQuery().in(GameRentalDetail::getGameId, gameIds));
        gameRentalInfoService.deleteByIds(gameIds);
        return Response.createSuccResponse();
    }
}
