package com.qingyu.mo.mybatisplus.interceptor;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.qingyu.mo.mybatisplus.entity.JoinResultMap;
import com.qingyu.mo.mybatisplus.wrapper.query.JoinLambdaQueryWrapper;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * join返回类型拦截器
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.6.2
 */
@SuppressWarnings("all")
@Order(Integer.MIN_VALUE)
@Intercepts(@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}))
public class JoinInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        if (args[0] instanceof MappedStatement) {
            MappedStatement ms = (MappedStatement) args[0];
            if (args[1] instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) args[1];
                Object ew = map.containsKey(Constants.WRAPPER) ? map.get(Constants.WRAPPER) : null;
                // 如果说 Wrapper 是JoinLambdaWrapper类型就代表可能需要解析多表映射
                if (ew instanceof JoinLambdaQueryWrapper) {
                    JoinLambdaQueryWrapper joinLambdaQueryWrapper = (JoinLambdaQueryWrapper) ew;
                    args[0] = newMappedStatement(ms, joinLambdaQueryWrapper);
                }
            }
        }
        return invocation.proceed();
    }

    /**
     * 构建新的MappedStatement
     */
    private MappedStatement newMappedStatement(MappedStatement ms, JoinLambdaQueryWrapper joinLambdaQueryWrapper) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), ms.getSqlSource(), ms.getSqlCommandType())
                .resource(ms.getResource())
                .fetchSize(ms.getFetchSize())
                .statementType(ms.getStatementType())
                .keyGenerator(ms.getKeyGenerator())
                .timeout(ms.getTimeout())
                .parameterMap(ms.getParameterMap())
                .resultSetType(ms.getResultSetType())
                .cache(ms.getCache())
                .flushCacheRequired(ms.isFlushCacheRequired())
                .useCache(ms.isUseCache());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            builder.keyProperty(String.join(StringPool.COMMA, ms.getKeyProperties()));
        }
        builder.resultMaps(CollUtil.toList(newResultMap(ms, joinLambdaQueryWrapper)));
        return builder.build();
    }

    /**
     * 构建resultMap
     */
    private ResultMap newResultMap(MappedStatement ms, JoinLambdaQueryWrapper<?> joinLambdaQueryWrapper) {
        List<ResultMap> resultMaps = ms.getResultMaps();
        List<ResultMap> newResultMaps = new ArrayList<>(resultMaps);
        ResultMap resultMap = resultMaps.get(0);
        List<ResultMapping> resultMappings = resultMap.getResultMappings();
        List<ResultMapping> newResultMappings = new ArrayList<>(resultMappings);
        List<JoinResultMap> joinResultMaps = joinLambdaQueryWrapper.getJoinResultMaps();
        for (JoinResultMap joinResultMap : joinResultMaps) {
            ResultMapping builder = new ResultMapping.Builder(ms.getConfiguration(),
                    joinResultMap.getProperty(), joinResultMap.getColumn(), joinResultMap.getTypeHandler())
                    .build();
            newResultMappings.add(builder);
        }
        return new ResultMap.Builder(ms.getConfiguration(), resultMap.getId(), resultMap.getType(), newResultMappings, true).build();
    }
}
