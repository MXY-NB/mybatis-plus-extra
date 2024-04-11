package com.qingyu.mo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
* <p>
* 通用属性
* </p>
*
* @author IVI00
* @since 2022-08-24
*/
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommonModEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime modTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.UPDATE)
    private String modifier;
}