package com.iv.ersr.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
* <p>
* 通用属性
* </p>
*
* @author IVI00
* @since 2022-08-24
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommonTimeEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ids【批量操作】
     */
    @TableField(exist = false)
    private List<Long> ids;
}