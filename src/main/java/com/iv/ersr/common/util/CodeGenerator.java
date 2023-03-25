package com.iv.ersr.common.util;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.iv.ersr.common.controller.BaseController;
import com.iv.ersr.common.entity.BaseEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * MybatisPlus 代码生成器
 * </p>
 *
 * @author IVI04
 * @since 2021-12-24
 */
@Slf4j
public class CodeGenerator {
    /**
     * 数据源配置
     */
    private static final DataSourceConfig.Builder DATA_SOURCE_CONFIG = new DataSourceConfig
            .Builder("jdbc:mysql://localhost:3306/kx_game?&allowMultiQueries=true&autoReconnect=true&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai", "root", "123456");

    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir");
        FastAutoGenerator.create(DATA_SOURCE_CONFIG)
                // 全局配置
                .globalConfig((scanner, builder) -> builder
                        .author(System.getProperty("user.name"))
                        .outputDir(projectPath + "/src/main/java")
                        .disableOpenDir())
                // 包配置
                .packageConfig((scanner, builder) -> builder.parent("com.iv.ersr").moduleName(scanner.apply("请输入包名？")).pathInfo(MapUtil.of(OutputFile.xml, projectPath + "/src/main/resources/mapper/")))
                // 策略配置
                .strategyConfig((scanner, builder) -> builder.addInclude(getTables(scanner.apply("请输入表名，多个英文逗号分隔？所有输入 all")))
                        .controllerBuilder().superClass(BaseController.class).enableRestStyle().enableHyphenStyle().build()
                        .mapperBuilder().build()
                        .entityBuilder().addSuperEntityColumns("id").enableLombok().superClass(BaseEntity.class).naming(NamingStrategy.underline_to_camel).columnNaming(NamingStrategy.underline_to_camel).enableRemoveIsPrefix().versionColumnName("version").logicDeleteColumnName("deleted").build()
                )
                .templateEngine(new FreemarkerTemplateEngine())
                .templateConfig((scanner, builder) -> builder.disable(TemplateType.ENTITY).entity("/templates/entity.java").build())
                /*
                    模板引擎配置，默认 Velocity 可选模板引擎 Beetl 或 Freemarker
                   .templateEngine(new BeetlTemplateEngine())

                 */
                .execute();
    }

    /**
     * 处理 all 情况
     */
    protected static List<String> getTables(String tables) {
        return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }
}