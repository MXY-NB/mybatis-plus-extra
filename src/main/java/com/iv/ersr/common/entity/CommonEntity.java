package com.iv.ersr.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
* <p>
* 通用属性
* </p>
*
* @author moxiaoyu
* @since 2022-08-24
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommonEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private String creator;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.UPDATE, insertStrategy = FieldStrategy.NEVER)
    private LocalDateTime modTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.UPDATE, insertStrategy = FieldStrategy.NEVER)
    private String modifier;

    /**
     * ids【批量操作】
     */
    @TableField(exist = false)
    private List<Long> ids;
}