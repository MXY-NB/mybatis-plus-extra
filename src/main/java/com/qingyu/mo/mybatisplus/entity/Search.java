package com.qingyu.mo.mybatisplus.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
* <p>
* 自定义属性
* </p>
*
* @author IVI00
* @since 2022-08-24
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Search implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字段
     */
    private String key;

    /**
     * 方法
     */
    private String method;

    /**
     * 值
     */
    private List<? extends Serializable> values;

    /**
     * 是否为join表
     */
    private Boolean joinTable;

    /**
     * 是否为排序
     */
    private Boolean asc;
}