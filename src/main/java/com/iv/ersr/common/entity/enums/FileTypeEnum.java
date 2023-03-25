package com.iv.ersr.common.entity.enums;

import cn.hutool.core.collection.CollUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>
 * 文件类型 枚举
 * </p>
 *
 * @author liweimin
 * @since 2021-10-12
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum FileTypeEnum {
    /**
     * 图片
     */
    PICTURE(CollUtil.newArrayList(FileExtensionEnum.JPG, FileExtensionEnum.PNG, FileExtensionEnum.JPEG, FileExtensionEnum.GIF)),
    /**
     * 视频
     */
    VIDEO(CollUtil.newArrayList(FileExtensionEnum.MP4, FileExtensionEnum.MKV, FileExtensionEnum.FLV, FileExtensionEnum.AVI));

    private List<FileExtensionEnum> types;
}
