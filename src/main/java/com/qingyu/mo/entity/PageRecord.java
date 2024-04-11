package com.qingyu.mo.entity;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 分页对象
 * </p>
 *
 * @author IVI04
 * @since 2021-12-24
 */
@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PageRecord<T> extends PageDTO<T> {

    public PageRecord(BaseEntity entity){
        super(entity.getPage(), entity.getSize());

        if (CollUtil.isEmpty(entity.getSortArgs())) {
            if (CharSequenceUtil.isEmpty(entity.getSortBy())) {
                entity.setSortBy("id");
            }
            OrderItem item = new OrderItem();
            item.setColumn(entity.getSortBy());
            item.setAsc(entity.isAsc());
            super.addOrder(item);
        } else {
            for (SortArgs sortArg : entity.getSortArgs()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setColumn(sortArg.getSortBy());
                orderItem.setAsc(sortArg.isAsc());
                super.addOrder(orderItem);
            }
        }
    }
}
