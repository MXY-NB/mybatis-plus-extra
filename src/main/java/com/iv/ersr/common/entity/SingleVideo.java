package com.iv.ersr.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 游戏视频和视频封面实体
 * </p>
 *
 * @author MXY
 * @since 2021-12-24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SingleVideo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *  游戏视频封面
     */
    private SingleFile videoCover;

    /**
     * 游戏视频
     */
    private SingleFile video;
}
