package com.iv.ersr.mybatisplus.interceptor;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.iv.ersr.mybatisplus.core.conditions.query.JoinLambdaQueryWrapper;
import com.iv.ersr.mybatisplus.core.entity.CollectionResultMap;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.result.DefaultResultHandler;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * mybatisResultSet结果集拦截器
 */
@Intercepts(@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class}))
public class JoinInterceptor2 implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        ResultSetHandler resultSetHandler = (ResultSetHandler) invocation.getTarget();
        Field mappedStatementField = ReflectionUtils.findField(DefaultResultSetHandler.class, "mappedStatement");
        Field parameterHandlerField = ReflectionUtils.findField(DefaultResultSetHandler.class, "parameterHandler");
        Field executorField = ReflectionUtils.findField(DefaultResultSetHandler.class, "executor");
        mappedStatementField.setAccessible(true);
        parameterHandlerField.setAccessible(true);
        executorField.setAccessible(true);
        MappedStatement ms = (MappedStatement) ReflectionUtils.getField(mappedStatementField, resultSetHandler);
        ParameterHandler parameterHandler = (ParameterHandler) ReflectionUtils.getField(parameterHandlerField, resultSetHandler);
        CachingExecutor executor = (CachingExecutor) ReflectionUtils.getField(executorField, resultSetHandler);
        if (parameterHandler.getParameterObject() instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) parameterHandler.getParameterObject();
            Object o = map.get(Constants.WRAPPER);
            if (o instanceof JoinLambdaQueryWrapper) {
                JoinLambdaQueryWrapper<?> wrapper = (JoinLambdaQueryWrapper<?>) o;
                List<CollectionResultMap> collectionResultMaps = wrapper.getCollectionResultMaps();

                Object result = invocation.proceed();
                if (result instanceof List) {
                    List resList = (List) result;
                    for (Object res : resList) {
                        if (CollectionUtils.isNotEmpty(collectionResultMaps)) {
                            for (CollectionResultMap collectionResultMap : collectionResultMaps) {
                                MappedStatement mappedStatement = ms.getConfiguration().getMappedStatement("com.iv.ersr.game.mapper.GameRentalDetailMapper.joinSelectList");
                                Map<String, Object> parameter = MapUtil.<String, Object>builder().put(Constants.WRAPPER, null).build();
                                assert executor != null;
                                List<Object> query = executor.query(mappedStatement, parameter, RowBounds.DEFAULT, new DefaultResultHandler());
                                ReflectUtil.setFieldValue(res, "gameRentalDetails", query);
                            }
                        }
                    }
                }
                return result;
            }
        }

        return invocation.proceed();

    }
}
