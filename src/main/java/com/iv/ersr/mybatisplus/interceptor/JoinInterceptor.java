package com.iv.ersr.mybatisplus.interceptor;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.iv.ersr.mybatisplus.core.conditions.query.JoinLambdaQueryWrapper;
import com.iv.ersr.mybatisplus.core.entity.CollectionResultMap;
import lombok.extern.slf4j.Slf4j;
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
 * @author moxiaoyu
 * @since 2023-03-26
 */
@Slf4j
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
//                    if (joinLambdaQueryWrapper.isResultMap() && CollectionUtils.isNotEmpty(ms.getResultMaps())) {
                        args[0] = newMappedStatement(ms, joinLambdaQueryWrapper);
//                    }

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
//                .resultMaps(ms.getResultMaps())
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
        builder.resultMaps(CollUtil.newArrayList(newResultMap(ms, joinLambdaQueryWrapper)));
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
        List<CollectionResultMap> collectionResultMaps = joinLambdaQueryWrapper.getCollectionResultMaps();
//        if (CollectionUtils.isNotEmpty(collectionResultMaps)) {
//            for (CollectionResultMap collectionResultMap : collectionResultMaps) {
//                StringBuilder stringBuilder = new StringBuilder();
//                List<ResultMapping> composites = new ArrayList<>();
//                for (FieldMapping fieldMapping : collectionResultMap.getFieldMappings()) {
//                    stringBuilder.append(fieldMapping.getParamName() + "=" + fieldMapping.getColumnName()).append(StringPool.COMMA);
//                    composites.add(new ResultMapping.Builder(ms.getConfiguration(),
//                            fieldMapping.getParamName(), fieldMapping.getColumnName(), Object.class)
//                            .build());
//                }
//                String collect = CharSequenceUtil.subBefore(stringBuilder.toString(), StringPool.COMMA, true);
//                ResultMapping builder = new ResultMapping.Builder(ms.getConfiguration(),
//                        collectionResultMap.getPropertyName(), "{" + collect + "}", List.class)
//                        .nestedQueryId(collectionResultMap.getId())
//                        .composites(composites).build();
//                newResultMappings.add(builder);
//            }
//        }
        return new ResultMap.Builder(ms.getConfiguration(), resultMap.getId(), resultMap.getType(), newResultMappings, true).build();
    }
}
