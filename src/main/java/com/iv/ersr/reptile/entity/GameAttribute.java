package com.iv.ersr.reptile.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 游戏视频图片属性实体
 * </p>
 *
 * @author IVI04
 * @since 2021-12-24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameAttribute implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 游戏视频
     */
    private List<GameVideoEntity> gameVideo;

    /**
     * 游戏图片
     */
    private List<SingleFile> gamePicture;
}
