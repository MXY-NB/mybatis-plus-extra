package com.iv.ersr.system.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.iv.ersr.common.util.Sequence;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * <p>
 * mybatisPlus属性自动填充配置
 * </p>
 *
 * @author moxiaoyu
 * @since 2022-04-11
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "creator", String.class, "System");
        this.strictInsertFill(metaObject, "id", Long.class, Sequence.generateSequenceId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // method is empty
    }
}