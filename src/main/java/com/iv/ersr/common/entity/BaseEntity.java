package com.iv.ersr.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 所有实体的超类
 * </p>
 *
 * @author IVI04
 * @since 2021-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField(fill = FieldFill.INSERT)
    private Long id;

    @TableField(exist = false)
    private Integer size = 10;

    @TableField(exist = false)
    private Integer page = 1;

    @TableField(exist = false)
    private List<SortItem> sortItems;
}
