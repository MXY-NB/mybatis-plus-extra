package com.iv.ersr.reptile.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.iv.ersr.common.entity.CommonEntity;
import com.iv.ersr.common.entity.enums.YesNoEnum;
import com.iv.ersr.common.typehandler.StringListTypeHandler;
import com.iv.ersr.reptile.entity.enums.GameTypeEnum;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
* <p>
* 游戏租赁信息
* </p>
*
* @author moxiaoyu
* @since 2022-05-12
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "game_rental_info", autoResultMap = true)
public class GameRentalInfo extends CommonEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 游戏中文名
     */
    private String chineseName;

    /**
     * 游戏官方名
     */
    private String officialName;

    /**
     * 游戏主题类型 如 动作,冒险,策略等,由配置表维护标签名称
     */
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> gameTheme;

    /**
     * 游戏类型 0-Switch 1-Steam 2-PS4 3-PS5 4-Xbox
     */
    private GameTypeEnum gameType;

    /**
     * 是否独占 0-否 1-是
     */
    private YesNoEnum exclusive;

    /**
     * 是否为广告位 0-否 1-是
     */
    private YesNoEnum advert;

    /**
     * 发布日期
     */
    private LocalDate releaseDate;

    /**
     * 游戏属性：{"game_picture": ["2022/sd/dfdf/xx.jpg","2022/sd/dfdf/xx2.jpg"],"game_video": ["2022/sd/dfdf/xx.mp4","2022/sd/dfdf/xx2.mp4"]}
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private GameAttribute gameAttribute;

    /**
     * 图片封面
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private SingleFile gameCover;

    /**
     * 推荐率
     */
    private Integer recommendedRate;

    /**
     * 发布公司
     */
    private String publishCompany;

    /**
     * 简介
     */
    private String simpleIntroduction;

    /**
     * 详细介绍
     */
    private String detailIntroduction;

    /**
     * 是否支持中文 0-否 1-是
     */
    private YesNoEnum isSupportChinese;

    /**
     * 是否精选推荐
     */
    private YesNoEnum recommend;

    /**
     * 游戏支持最大预约数
     */
    private Integer maxAccountBooking;

    /**
     * 支持的续租次数
     */
    private Integer supportRenewNumber;

    /**
     * 游戏状态 0-锁定 1-有效
     */
    private YesNoEnum gameStatus;

    /**
     * 是否已删除 0-否 1-是
     */
    @TableLogic
    private YesNoEnum deleted;

    /**
     * 游戏封面 {"id":1,"name":"xxx.jpg","url":"xxx"}
     */
    @TableField(exist = false)
    private List<String> gamePictures;

    /**
     * 游戏封面 {"id":1,"name":"xxx.jpg","url":"xxx"}
     */
    @TableField(exist = false)
    private String gameCoverPicture;


    /**
     * 游戏id
     */
    @TableField(exist = false)
    private Long gameId;

    /**
     * 游戏大小(单位：GB)
     */
    @TableField(exist = false)
    private String gameSize;
}