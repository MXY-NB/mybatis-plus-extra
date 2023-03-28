package com.iv.ersr.system.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.iv.ersr.mybatisplus.core.injector.DefaultSqlInjectorPlus;
import com.iv.ersr.mybatisplus.interceptor.JoinInterceptor;
import com.iv.ersr.mybatisplus.interceptor.JoinInterceptor2;
import com.iv.ersr.mybatisplus.interceptor.MybatisPlusJoinInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * <p>
 * MybatisPlusConfig配置
 * </p>
 *
 * @author moxiaoyu
 * @since 2021-12-24
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.iv.ersr.*.mapper*")
public class MybatisPlusConfig {

    @Bean
    public DefaultSqlInjectorPlus mySqlInjector(){
        return new DefaultSqlInjectorPlus();
    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.druid")
    public DataSource dataSourceOne(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    @Bean
    public MybatisPlusJoinInterceptor joinInterceptorConfig() {
        return new MybatisPlusJoinInterceptor(new JoinInterceptor(), new JoinInterceptor2());
    }
}