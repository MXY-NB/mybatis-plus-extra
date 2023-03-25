package com.iv.ersr.common.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 文件业务类型 枚举
 * </p>
 *
 * @author liweimin
 * @since 2021-10-12
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum FileBusinessTypeEnum {
    /**
     * 游戏封面
     */
    GAME_COVER(0),
    /**
     * 会员卡封面
     */
    VIP_CARD_COVER(1),
    /**
     * 游戏图片
     */
    GAME_PICTURE(2),
    /**
     * 游戏视频
     */
    GAME_VIDEO(3),
    /**
     * 游戏视频封面
     */
    GAME_VIDEO_COVER(4),
    /**
     * 头像
     */
    USER_HEAD_PORTRAIT(5),
    /**
     * 归还凭证
     */
    RETURN_VOUCHER(6),
    /**
     * 购买协议
     */
    PURCHASE_AGREEMENT(7);

    @EnumValue
    @JsonValue
    private int value;
}
