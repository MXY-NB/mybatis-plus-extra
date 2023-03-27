package com.iv.ersr.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 单个文件字段实体
 * </p>
 *
 * @author MXY
 * @since 2021-12-24
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SingleFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 原文件名
     */
    private String name;

    /**
     * 文件访问路径
     */
    private String url;
}
