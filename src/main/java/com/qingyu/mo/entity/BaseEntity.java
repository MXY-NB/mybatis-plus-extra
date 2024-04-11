package com.qingyu.mo.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 所有实体的超类
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BaseEntity implements Serializable {

    @ExcelIgnore
    private static final long serialVersionUID = 1L;

    @ExcelIgnore
    private Long id;

    @ExcelIgnore
    @TableField(exist = false)
    @Builder.Default
    private Integer size = 10;

    @ExcelIgnore
    @TableField(exist = false)
    @Builder.Default
    private Integer page = 1;

    @ExcelIgnore
    @TableField(exist = false)
    private String sortBy;

    @ExcelIgnore
    @TableField(exist = false)
    private boolean asc;

    @ExcelIgnore
    @TableField(exist = false)
    private List<SortArgs> sortArgs;

    /**
     * ids【批量操作】
     */
    @TableField(exist = false)
    private List<Long> ids;

    /**
     * 搜索条件
     */
    @TableField(exist = false)
    private Integer offsetDay;

    /**
     * 搜索条件
     */
    @TableField(exist = false)
    private LocalDate businessDate;

    /**
     * 搜索条件
     */
    @TableField(exist = false)
    private String search;
}
