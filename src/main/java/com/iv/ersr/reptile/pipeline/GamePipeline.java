package com.iv.ersr.reptile.pipeline;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.iv.ersr.common.entity.InnerFile;
import com.iv.ersr.common.entity.enums.FileBusinessTypeEnum;
import com.iv.ersr.common.entity.enums.FileTypeEnum;
import com.iv.ersr.common.exception.HandlerException;
import com.iv.ersr.common.util.CosUtil;
import com.iv.ersr.common.util.MyUtil;
import com.iv.ersr.common.util.Sequence;
import com.iv.ersr.reptile.entity.GameRentalAreaInfo;
import com.iv.ersr.reptile.entity.GameRentalDetail;
import com.iv.ersr.reptile.entity.GameRentalInfo;
import com.iv.ersr.reptile.entity.SingleFile;
import com.iv.ersr.reptile.service.IGameRentalAreaInfoService;
import com.iv.ersr.reptile.service.IGameRentalDetailService;
import com.iv.ersr.reptile.service.IGameRentalInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import javax.annotation.Resource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 抽取结果的保存
 * </p>
 *
 * @author moxiaoyu
 * @since 2022-05-12
 */
@Slf4j
@Component
public class GamePipeline implements Pipeline {

    @Resource
    private IGameRentalInfoService gameRentalInfoService;

    @Resource
    private IGameRentalDetailService gameRentalDetailService;

    @Resource
    private IGameRentalAreaInfoService gameRentalAreaInfoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void process(ResultItems resultItems, Task task) {
        GameRentalInfo gameRentalInfo = resultItems.get("gameRentalInfo");
        GameRentalDetail gameRentalDetail = resultItems.get("gameRentalDetail");
        List<GameRentalAreaInfo> gameRentalAreaInfos = resultItems.get("gameRentalAreaInfos");

        log.info("游戏消息：-----------" + JSONUtil.toJsonPrettyStr(gameRentalInfo));
        log.info("游戏详细消息：-----------" + JSONUtil.toJsonPrettyStr(gameRentalDetail));
        log.info("游戏地区消息：-----------" + JSONUtil.toJsonPrettyStr(gameRentalAreaInfos));
        if (BeanUtil.isNotEmpty(gameRentalInfo) && BeanUtil.isNotEmpty(gameRentalDetail) && CollUtil.isNotEmpty(gameRentalAreaInfos)) {
            GameRentalInfo dbGameRentalInfo = gameRentalInfoService.getOne(Wrappers.<GameRentalInfo>lambdaQuery().eq(GameRentalInfo::getChineseName, gameRentalInfo.getChineseName()));
            if (MyUtil.isEmpty(dbGameRentalInfo)) {
                log.info("开始保存！------------------------------------");
                gameRentalInfoService.save(gameRentalInfo);
                gameRentalDetail.setGameId(gameRentalInfo.getId());
                gameRentalDetailService.save(gameRentalDetail);

                if (CharSequenceUtil.isNotEmpty(gameRentalInfo.getGameCoverPicture())) {
                    MultipartFile multipartFile = urlToMultipartFile(gameRentalInfo.getGameCoverPicture(),  Sequence.generateSequenceIdStr() + ".jpg");
                    InnerFile innerFile = CosUtil.uploadFile(multipartFile, FileBusinessTypeEnum.GAME_COVER, FileTypeEnum.PICTURE);
                    GameRentalInfo updateGameRentalInfo = new GameRentalInfo();
                    updateGameRentalInfo.setId(gameRentalInfo.getId());
                    SingleFile gameCover = new SingleFile();
                    gameCover.setUrl(innerFile.getPath());
                    gameCover.setName(innerFile.getName());
                    gameCover.setId(Sequence.generateSequenceId());
                    updateGameRentalInfo.setGameCover(gameCover);
                    boolean flag = gameRentalInfoService.updateById(updateGameRentalInfo);
                    if (!flag) {
                        log.info("游戏：{}更新保存封面图片失败！", gameRentalInfo.getChineseName());
                    }
                }

                // 保存图片
                if (CollUtil.isNotEmpty(gameRentalInfo.getGamePictures())) {
                    ArrayList<MultipartFile> multipartFiles = new ArrayList<>();
                    for (String gamePicture : gameRentalInfo.getGamePictures()) {
                        MultipartFile multipartFile = urlToMultipartFile(gamePicture,  Sequence.generateSequenceIdStr() + ".jpg");
                        multipartFiles.add(multipartFile);
                    }

                    if (CollUtil.isNotEmpty(multipartFiles)) {
                        GameRentalInfo updateGameRentalInfo = new GameRentalInfo();
//                        multipartFiles = CollUtil.newArrayList(multipartFiles.get(1));
                        MultipartFile[] files = ArrayUtil.toArray(multipartFiles, MultipartFile.class);
                        List<InnerFile> innerFiles = CosUtil.batchUploadFile(files, FileBusinessTypeEnum.GAME_PICTURE, FileTypeEnum.PICTURE);
                        if (CollUtil.isEmpty(innerFiles)) {
                            log.info("cos批量上传返回集合为空！！！");
                            throw new HandlerException("cos批量上传返回集合为空！！！");
                        }
                        List<SingleFile> singleFiles = new ArrayList<>();
                        for (InnerFile innerFile : innerFiles) {
                            singleFiles.add(SingleFile.builder().id(Sequence.generateSequenceId()).name(innerFile.getName()).url(innerFile.getPath()).build());
                        }
                        updateGameRentalInfo.setId(gameRentalInfo.getId());
                        updateGameRentalInfo.setGameAttribute(gameRentalInfo.getGameAttribute());
                        updateGameRentalInfo.getGameAttribute().setGamePicture(singleFiles);
                        boolean flag = gameRentalInfoService.updateById(updateGameRentalInfo);
                        if (!flag) {
                            log.info("游戏：{}更新保存图片失败！", gameRentalInfo.getChineseName());
                        }
                    }
                }
                log.info("保存结束！------------------------------------");
            } else {
                log.info("开始更新！------------------------------------");
                gameRentalInfo.setId(dbGameRentalInfo.getId());
                boolean flag = gameRentalInfoService.updateById(gameRentalInfo);
                if (!flag) {
                    log.info("游戏：{}更新信息失败！", gameRentalInfo.getChineseName());
                }
                GameRentalDetail dbGameRentalDetail = gameRentalDetailService.getOne(Wrappers.<GameRentalDetail>lambdaQuery().eq(GameRentalDetail::getGameId, dbGameRentalInfo.getId()));
                gameRentalDetail.setId(dbGameRentalDetail.getId());
                gameRentalDetail.setGameId(dbGameRentalInfo.getId());
                flag = gameRentalDetailService.updateById(gameRentalDetail);
                if (!flag) {
                    log.info("游戏：{}更新详情信息失败！", gameRentalInfo.getChineseName());
                }
                log.info("更新结束！------------------------------------");
            }

            log.info("开始保存地区信息！------------------------------------");
            for (GameRentalAreaInfo gameRentalAreaInfo : gameRentalAreaInfos) {
                gameRentalAreaInfo.setGameId(gameRentalInfo.getId());
            }
            gameRentalAreaInfoService.saveBatch(gameRentalAreaInfos);
            log.info("保存地区信息结束！------------------------------------");
        }

    }

    public MultipartFile fileToMultipartFile(File file) {
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory(16, null);
        FileItem item = diskFileItemFactory.createItem(file.getName(), "text/plain", true, file.getName());
        int bytesRead;
        byte[] buffer = new byte[8192];
        try {
            try (FileInputStream fis = new FileInputStream(file)) {
                OutputStream os = item.getOutputStream();
                int len = 8192;
                while ((bytesRead = fis.read(buffer, 0, len)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new CommonsMultipartFile(item);
    }

    /**
     * url转MultipartFile
     */
    public MultipartFile urlToMultipartFile(String url, String fileName) {
        File file;
        MultipartFile multipartFile = null;
        try {
            HttpURLConnection httpUrl = (HttpURLConnection) new URL(url).openConnection();
            httpUrl.setRequestProperty("Referer", url);
            httpUrl.connect();
            file = inputStreamToFile(httpUrl.getInputStream(),fileName);
            multipartFile = fileToMultipartFile(file);
            httpUrl.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return multipartFile;
    }

    public File inputStreamToFile(InputStream ins, String name) throws Exception {
        File file = new File(System.getProperty("java.io.tmpdir") + File.separator + name);
        try (OutputStream os = new FileOutputStream(file)) {
            int len = 8192;
            byte[] buffer = new byte[len];
            int bytesRead;
            while ((bytesRead = ins.read(buffer, 0, len)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }
        ins.close();
        return file;
    }
}
