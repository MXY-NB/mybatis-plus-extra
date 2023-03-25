package com.iv.ersr.mybatisplus.core.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * MybatisPlus 支持 SQL 方法
 *
 * @author MXY
 * @since 2023-03-22
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum SqlMethod {

    /**
     * 插入n条数据
     */
    INSERT_LIST("insertList", "插入n条数据(选择字段插入)", "<script>\nINSERT INTO %s %s VALUES %s\n</script>"),

    /**
     * 更新n条数据
     */
    UPDATE_BATCH_BY_ID("updateBatchById", "根据IDS 选择修改数据", "<script>\nUPDATE %s %s WHERE %s=#{%s} %s\n</script>"),

    /**
     * 更新n条数据
     */
    UPDATE_BATCH_BY_ID_WITH_NULL("updateBatchByIdWithNull", "根据IDS 选择修改数据(允许字段赋值null)", "<script>\nUPDATE %s %s WHERE %s=#{%s} %s\n</script>"),

    /**
     * 查询满足条件所有数据
     */
    JOIN_SELECT_LIST("joinSelectList", "查询满足条件所有数据", "<script>%s SELECT %s FROM %s %s %s %s\n</script>"),

    /**
     * 查询满足条件的总数
     */
    JOIN_SELECT_COUNT("joinSelectCount", "查询满足条件的总数", "<script>%s SELECT COUNT(1) FROM %s %s %s %s\n</script>"),

    /**
     * 查询满足条件的列表分页
     */
    JOIN_SELECT_PAGE("joinSelectPage", "查询满足条件的列表分页", "<script>%s SELECT %s FROM %s %s %s %s\n</script>"),

    /**
     * 查询满足条件的单个对象
     */
    JOIN_SELECT_ONE("joinSelectOne", "查询满足条件的单个对象", "<script>%s SELECT %s FROM %s %s %s %s \n</script>");

    /**
     * 名称
     */
    private String method;
    private String desc;
    private String sql;
}