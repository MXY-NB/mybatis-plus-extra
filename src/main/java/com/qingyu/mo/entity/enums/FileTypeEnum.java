package com.qingyu.mo.entity.enums;

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
    PICTURE(CollUtil.newArrayList(FileExtensionEnum.JPG, FileExtensionEnum.PNG, FileExtensionEnum.JPEG, FileExtensionEnum.GIF,
            FileExtensionEnum.BMP, FileExtensionEnum.VSD)),

    /**
     * 音乐
     */
    MUSIC(CollUtil.newArrayList(FileExtensionEnum.MP3)),

    /**
     * 视频
     */
    VIDEO(CollUtil.newArrayList(FileExtensionEnum.MP4, FileExtensionEnum.MKV, FileExtensionEnum.FLV, FileExtensionEnum.AVI,
            FileExtensionEnum.WMV)),

    /**
     * 表格
     */
    EXCEL(CollUtil.newArrayList(FileExtensionEnum.XLS, FileExtensionEnum.XLSX)),

    /**
     * 文档
     */
    WORD(CollUtil.newArrayList(FileExtensionEnum.TXT, FileExtensionEnum.DOC, FileExtensionEnum.DOCX, FileExtensionEnum.PDF, FileExtensionEnum.PPT,
            FileExtensionEnum.PPTX)),

    /**
     * 压缩包
     */
    ZIP(CollUtil.newArrayList(FileExtensionEnum.ZIP, FileExtensionEnum.RAR, FileExtensionEnum.SEVEN_Z)),

    /**
     * 通用
     */
    COMMON(CollUtil.newArrayList(CollUtil.union(PICTURE.getTypes(), EXCEL.getTypes(), WORD.getTypes(), ZIP.getTypes())));

    private List<FileExtensionEnum> types;
}
