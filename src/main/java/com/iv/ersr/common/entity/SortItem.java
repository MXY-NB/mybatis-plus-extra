package com.iv.ersr.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 排序
 * </p>
 *
 * @author IVI04
 * @since 2021-12-24
 */
@Data
public class SortItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private String sortBy;

    @TableField(exist = false)
    private String sortType;
}
