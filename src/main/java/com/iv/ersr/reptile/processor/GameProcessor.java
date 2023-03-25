package com.iv.ersr.reptile.processor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import com.iv.ersr.common.entity.enums.YesNoEnum;
import com.iv.ersr.common.util.Sequence;
import com.iv.ersr.reptile.entity.*;
import com.iv.ersr.reptile.entity.enums.GameTypeEnum;
import com.iv.ersr.reptile.entity.enums.OnlineModeEnum;
import com.iv.ersr.reptile.entity.enums.PlayModeEnum;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 爬取游戏数据处理器
 * </p>
 *
 * @author moxiaoyu
 * @since 2022-05-12
 */
@Slf4j
public class GameProcessor implements PageProcessor {

    /**
     * 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
     */
    private static final Site SITE = Site.me().setCharset("UTF-8").setTimeOut(10000).setRetryTimes(0).setCycleRetryTimes(1).setSleepTime(3000);

    public static final String URL = "https://www.zhirenjoy.com/games?page=";

    /**
     * process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
     * @param page 页面内容
     */
    @Override
    public void process(Page page) {
        if (CharSequenceUtil.startWith(page.getUrl().get(), URL)) {
            List<String> all = page.getHtml().xpath("//span[@class='pic lf']/a/@href").all();
//            page.addTargetRequests(CollUtil.newArrayList(all.get(0)));
            page.addTargetRequests(all);
            return;
        }

        try {
            String chineseName = page.getHtml().xpath("//h1[@class='big-title']/text()").toString();
            String officialName = page.getHtml().xpath("//div[@class='t-rt-p-lf lf']/h2[@class='one']/text()").toString();
            // 图片
            String pictureCoverUrl = page.getHtml().xpath("//div[contains(@class,'t-title cf ty-con')]/img/@src").toString();
            List<String> pictureUrls = page.getHtml().xpath("//div[contains(@class,'swiper-container')][1]/div[contains(@class,'swiper-wrapper')]//img/@src").all();
//            System.setProperty("webdriver.chrome.driver",
//                    "D:\\idea_workplace\\ers-reptile\\src\\main\\resources\\config\\chromedriver.exe");
//            System.getProperties().setProperty("selenuim_config","D:\\idea_workplace\\ers-reptile\\src\\main\\resources\\config\\config.ini");
//            WebDriver driver = new ChromeDriver();
//            driver.get(page.getUrl().get());
//            WebDriverWait wait = new WebDriverWait(driver, 5, 5000);
            // 等待节点出现
            // 轮播图按钮
//            WebElement element = elementExist(wait, By.xpath("//div[@class='fotorama__arr fotorama__arr--next']"));
//            WebElement pictures = elementExist(wait, By.xpath("//div[@class='gallery-placeholder']//div[@class='fotorama__stage__shaft fotorama__grab']"));
//            List<WebElement> elements;
//            // 点击轮播图
//            click(element);
//            click(element);
            log.info("图片封面：{}\n", pictureCoverUrl);
            ArrayList<String> distinctUrls = CollUtil.distinct(pictureUrls);
            for (String distinctUrl : distinctUrls) {
                log.info("图片链接：{}\n", distinctUrl);
            }

            String splitStr = page.getHtml().xpath("//div[@class='t-rt-p-lf lf']/span[2]/text()").toString().trim();
            List<String> split = CharSequenceUtil.splitTrim(splitStr, '/');
            String dateStr = split.get(0);
            String publishCompany = null;
            if (split.size() > 1) {
                publishCompany = split.get(1);
            }
            List<String> playModeList = page.getHtml().xpath("//div[@class='djpm']/div[@class='cf'][1]/span[@class='wz-tp lf']/img/@alt").all();
            String players = page.getHtml().xpath("//div[@class='djpm']/div[@class='cf'][2]/span[@class='wz-tp lf']/text()").toString().trim();
            String onlineModesStr = page.getHtml().xpath("//div[@class='djpm']/div[@class='cf'][3]/span[@class='wz-tp lf']/text()").toString().trim();
            String gameSize = page.getHtml().xpath("//div[@class='djpm']/div[@class='cf'][5]/span[@class='wz-tp lf']/text()").toString().trim();
            List<String> gameTheme = page.getHtml().xpath("//div[@class='djpm']/div[@class='cf'][6]/span[@class='wz-tp lf']/a/text()").all();
            List<String> languagesStr = page.getHtml().xpath("//div[@class='t-rt-p-lf lf']/span[@class='two']/i/text()").all();
            Integer entityCardFlag = page.getHtml().xpath("//div[@class='djpm']/div[@class='cf'][7]/span[@class='wz-tp lf']/text()").toString().contains("实体卡") ? 1 : 0;
            List<String> introductions = page.getHtml().xpath("//div[@class='yxjj']/p/text()").all();
            String simpleIntroduction = "";
            String detailIntroduction = "";
            if (CollUtil.isNotEmpty(introductions)) {
                simpleIntroduction = ObjectUtil.defaultIfNull(introductions.get(0), "").trim();
                if (introductions.size() > 1) {
                    detailIntroduction = ObjectUtil.defaultIfNull(introductions.get(1), "").trim();
                }
            }

            List<OnlineModeEnum> onlineModes = new ArrayList<>();
            if (onlineModesStr.contains("本地")) {
                onlineModes.add(OnlineModeEnum.LOCAL);
            }
            if (onlineModesStr.contains("联网")) {
                onlineModes.add(OnlineModeEnum.ONLINE);
            }
            List<PlayModeEnum> playModes = new ArrayList<>();
            if (CollUtil.isNotEmpty(playModeList)) {
                for (String str : playModeList) {
                    PlayModeEnum playModeEnum = EnumUtil.likeValueOf(PlayModeEnum.class, str);
                    if (playModeEnum!=null) {
                        playModes.add(playModeEnum);
                    }
                }
            }
            page.putField("gameRentalInfo", GameRentalInfo.builder()
                    .chineseName(chineseName)
                    .officialName(officialName)
                    .gameTheme(gameTheme)
                    .releaseDate(DateUtil.parseDate(dateStr).toLocalDateTime().toLocalDate())
                    .publishCompany(publishCompany)
                    .simpleIntroduction(simpleIntroduction)
                    .detailIntroduction(detailIntroduction)
                    .gamePictures(distinctUrls)
                    .gameCoverPicture(pictureCoverUrl)
                    .gameCover(SingleFile.builder().id(Sequence.generateSequenceId()).build())
                    .isSupportChinese(CharSequenceUtil.contains(languagesStr.toString(), "无中文") || !CharSequenceUtil.contains(languagesStr.toString(), "中文") ? YesNoEnum.NO : YesNoEnum.YES)
                    .exclusive(YesNoEnum.NO).advert(YesNoEnum.NO).gameStatus(YesNoEnum.NO)
                    .maxAccountBooking(2).recommend(YesNoEnum.NO).recommendedRate(80)
                    .gameAttribute(GameAttribute.builder().gamePicture(new ArrayList<>()).gameVideo(new ArrayList<>()).build())
                    .gameType(GameTypeEnum.SWITCH)
                    .build());
            page.putField("gameRentalDetail", GameRentalDetail.builder()
                    .gameSize(CharSequenceUtil.replace(gameSize, " ", ""))
                    .isHaveDemo(YesNoEnum.NO)
                    .playModes(playModes)
                    .onlineModes(onlineModes)
                    .players(CharSequenceUtil.strip(players, "Player").trim())
                    .otherAttribute(GameOtherAttribute.builder().entityCardFlag(entityCardFlag).build())
                    .lowestPrice(YesNoEnum.NO)
                    .build());

            List<GameRentalAreaInfo> gameRentalAreaInfos = new ArrayList<>();
            // 地区信息
            List<String> allArea = page.getHtml().xpath("//div[@class='index-lf-p']/div[@class='djpm']/div[@class='cf']/span[@class='icon lf']").all();
            for (int i = 1; i < allArea.size() + 1; i++) {
                String areaName = page.getHtml().xpath("//div[@class='index-lf-p']/div[@class='djpm']/div[@class='cf'][" + i + "]/span[@class='icon lf']/i/text()").get();
                List<String> languages = page.getHtml().xpath("//div[@class='index-lf-p']/div[@class='djpm']/div[@class='cf'][" + i + "]/span[@class='yy lf']/i/text()").all();
                String priceStr = page.getHtml().xpath("//div[@class='index-lf-p']/div[@class='djpm']/div[@class='cf'][" + i + "]/span[@class='jg rt']/em/text()").get();

                BigDecimal price = BigDecimal.ZERO;
                try {
                    price = new BigDecimal(CharSequenceUtil.subAfter(priceStr, ' ', true));
                } catch (Exception e) {
                    log.error(areaName + "地区价格转换失败！");
                }
                if (CharSequenceUtil.isNotEmpty(areaName) && CollUtil.isNotEmpty(languages)) {
                    gameRentalAreaInfos.add(GameRentalAreaInfo.builder()
                            .releaseArea(areaName)
                            .languages(languages)
                            .isSupportChinese(languages.contains("中文")?YesNoEnum.YES:YesNoEnum.NO)
                            .price(price)
                            .build());
                }
            }
            List<String> allArea2 = page.getHtml().xpath("//div[@class='index-lf-p']/div[@class='element']/div[@class='djpm']/div[@class='cf']/span[@class='icon lf']").all();
            for (int i = 1; i < allArea2.size() + 1; i++) {
                log.info("内容--------- : " + allArea2.get(i-1));
                String areaName = page.getHtml().xpath("//div[@class='index-lf-p']/div[@class='element']/div[@class='djpm']/div[@class='cf'][" + i + "]/span[@class='icon lf']/i/text()").get();
                List<String> languages = page.getHtml().xpath("//div[@class='index-lf-p']/div[@class='element']/div[@class='djpm']/div[@class='cf'][" + i + "]/span[@class='yy lf']/i/text()").all();
                String priceStr = page.getHtml().xpath("//div[@class='index-lf-p']/div[@class='element']/div[@class='djpm']/div[@class='cf'][" + i + "]/span[@class='jg rt']/em/text()").get();

                BigDecimal price = BigDecimal.ZERO;
                try {
                    price = new BigDecimal(CharSequenceUtil.subAfter(priceStr, ' ', true));
                } catch (Exception e) {
                    log.error(areaName + "地区价格转换失败！");
                }
                if (CharSequenceUtil.isNotEmpty(areaName) && CollUtil.isNotEmpty(languages)) {
                    gameRentalAreaInfos.add(GameRentalAreaInfo.builder()
                            .releaseArea(areaName)
                            .languages(languages)
                            .isSupportChinese(languages.contains("中文")?YesNoEnum.YES:YesNoEnum.NO)
                            .price(price)
                            .build());
                }
            }
            page.putField("gameRentalAreaInfos", gameRentalAreaInfos);

            // 关闭窗口
//            driver.close();
//        } catch (InterruptedException e) {
//            log.error("异常：", e);
//            FileWriter fileWriter = FileWriter.create(FileUtil.touch("d:/upload/error.txt"));
//            fileWriter.write(page.getUrl().get() +"\n");
//            Thread.currentThread().interrupt();
//        }
        } catch (Exception e) {
            log.error("异常：", e);
            FileWriter fileWriter = FileWriter.create(FileUtil.touch("d:/upload/error.txt"));
            fileWriter.write(page.getUrl().get() +"\n");
        }
    }

    private void click(WebElement element) throws InterruptedException {
        if (element != null) {
            try {
                element.click();
            } catch (Exception e) {
                log.info("點擊失敗！！！！！");
            }
            Thread.sleep(1000);
        }
    }

    @Override
    public Site getSite() {
        return SITE;
    }

    public WebElement elementExist(WebDriverWait wait, By locator) {
        try {
            return wait.until(ExpectedConditions.
                    presenceOfElementLocated(locator));
        } catch (Exception e) {
            return null;
        }
    }
}
