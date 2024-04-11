package com.qingyu.mo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 多搜索排序对象
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SortArgs {

    private String sortBy;

    private boolean asc = true;
}
