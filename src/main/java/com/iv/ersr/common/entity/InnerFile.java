package com.iv.ersr.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.Serializable;

/**
 * @author MXY
 * @Description 自定义文件实体
 * @date 2022/02/23
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InnerFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 原上传的文件名
     */
    private String originalName;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件相对路径
     */
    private String path;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 文件实体
     */
    private File file;
}
