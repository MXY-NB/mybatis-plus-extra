package com.iv.ersr.common.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 文件后缀 枚举
 * </p>
 *
 * @author liweimin
 * @since 2021-10-12
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum FileExtensionEnum {
    /**
     * 网页
     */
    HTML("html"),
    /**
     * 网页
     */
    XML("xml"),
    /**
     * 文档
     */
    DOC("doc"),
    /**
     * 文档
     */
    DOCX("docx"),
    /**
     * 表格
     */
    XLS("xls"),
    /**
     * 表格
     */
    XLSX("xlsx"),
    /**
     * ppt
     */
    PPT("ppt"),
    /**
     * ppt
     */
    PPTX("pptx"),
    /**
     * pdf
     */
    PDF("pdf"),
    /**
     * 文本文件
     */
    TXT("txt"),
    /**
     * 图片
     */
    JPG("jpg"),
    /**
     * 图片
     */
    GIF("gif"),
    /**
     * 图片
     */
    JPEG("jpeg"),
    /**
     * 图片
     */
    PNG("png"),
    /**
     * 图片
     */
    BMP("bmp"),
    /**
     * 图片
     */
    VSD("vsd"),
    /**
     * 音乐
     */
    MP3("mp3"),
    /**
     * 视频
     */
    MP4("mp4"),
    /**
     * 视频
     */
    MKV("mkv"),
    /**
     * 视频
     */
    AVI("avi"),
    /**
     * 视频
     */
    WMV("wmv"),
    /**
     * 视频
     */
    FLV("flv"),
    /**
     * 压缩包
     */
    ZIP("zip");

    private String value;
}
