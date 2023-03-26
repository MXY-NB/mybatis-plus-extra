package com.iv.ersr.game.typehandler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.iv.ersr.common.typehandler.ListTypeHandler;
import com.iv.ersr.game.entity.enums.PlayModeEnum;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.List;

/**
 * <p>
 * 数据库数据转换List数组
 * </p>
 *
 * @author moxiaoyu
 * @since 2021-12-24
 */
@MappedJdbcTypes(JdbcType.VARBINARY)
@MappedTypes({List.class})
public class PlayModeEnumListTypeHandler extends ListTypeHandler<PlayModeEnum> {

    @Override
    protected TypeReference<List<PlayModeEnum>> specificType() {
        return new TypeReference<List<PlayModeEnum>>() {};
    }
}