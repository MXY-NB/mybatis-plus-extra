package com.qingyu.mo.mybatisplus.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * MybatisPlus 支持 SQL 方法
 *
 * @author qingyu-mo
 * @since 1.0.6.2
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
     * 更新数据
     */
    UPDATE_BY_ID_WITH_NULL("updateByIdWithNull", "根据ID 修改数据", "<script>\nUPDATE %s %s WHERE %s=#{%s} %s\n</script>"),

    /**
     * 更新数据
     */
    JOIN_UPDATE("joinUpdate", "根据 whereEntity 条件，更新记录", "<script>\nUPDATE %s %s %s %s\n</script>"),

    /**
     * 更新n条数据
     */
    UPDATE_BATCH_BY_ID("updateBatchById", "根据IDS 选择修改数据", "<script>\n<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\";\">\nupdate %s %s where %s=#{%s} %s\n</foreach>\n</script>"),

    /**
     * 更新n条数据
     */
    UPDATE_BATCH_BY_ID_WITH_NULL("updateBatchByIdWithNull", "根据IDS 选择修改数据(允许字段赋值null)", "<script>\n<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\";\">\nupdate %s %s where %s=#{%s} %s\n</foreach>\n</script>"),

    /**
     * 查询满足条件所有数据
     */
    JOIN_SELECT_LIST("joinSelectList", "查询满足条件所有数据", "<script>\n%s\nSELECT\n%s\n%s\nFROM %s %s %s %s\n</script>"),

    /**
     * 查询满足条件所有数据
     */
    JOIN_SELECT_DELETED_LIST("joinSelectDeletedList", "查询满足条件所有数据", "<script>\n%s\nSELECT\n%s\n%s\nFROM %s %s %s %s\n</script>"),

    /**
     * 查询满足条件的总数
     */
    JOIN_SELECT_COUNT("joinSelectCount", "查询满足条件的总数", "<script>\n%s\nSELECT COUNT(1) FROM %s %s %s %s\n</script>"),

    /**
     * 查询满足条件的列表分页
     */
    JOIN_SELECT_PAGE("joinSelectPage", "查询满足条件的列表分页", "<script>\n%s\nSELECT\n%s\n%s\nFROM %s %s %s %s\n</script>"),

    /**
     * 查询满足条件的总数
     */
    JOIN_SELECT_MAPS("joinSelectMaps", "查询满足条件的总数", "<script>\n%s\nSELECT\n%s\n%s\nFROM %s %s %s %s\n</script>"),

    /**
     * 逻辑删除记录
     */
    JOIN_LOGIC_DELETE("joinDelete", "根据 entity 条件逻辑删除记录", "<script>\nUPDATE %s %s %s %s\n</script>"),

    /**
     * 物理删除记录
     */
    PHYSICAL_DELETE("physicalDelete", "根据 entity 条件物理删除记录", "<script>\nDELETE FROM %s %s %s\n</script>"),

    /**
     * 物理删除记录
     */
    PHYSICAL_DELETE_BY_ID("physicalDeleteById", "根据 id 物理删除记录", "<script>\nDELETE FROM %s WHERE %s=#{%s}\n</script>");

    /**
     * 名称
     */
    private String method;
    private String desc;
    private String sql;
}