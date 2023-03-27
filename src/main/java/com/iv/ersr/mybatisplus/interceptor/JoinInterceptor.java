package com.iv.ersr.mybatisplus.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.iv.ersr.game.entity.GameRentalDetail;
import com.iv.ersr.mybatisplus.core.conditions.query.JoinLambdaQueryWrapper;
import com.iv.ersr.mybatisplus.core.entity.CollectionResultMap;
import com.iv.ersr.mybatisplus.core.entity.FieldMapping;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.TextSqlNode;
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
        if (CollectionUtils.isNotEmpty(collectionResultMaps)) {
            for (CollectionResultMap collectionResultMap : collectionResultMaps) {
                MappedStatement mappedStatement = ms.getConfiguration().getMappedStatement("com.iv.ersr.game.mapper.GameRentalDetailMapper.joinSelectList");
                BoundSql boundSql = mappedStatement.getBoundSql(MapUtil.builder().put(Constants.WRAPPER, collectionResultMap.getWrapper()).put("param1", collectionResultMap.getWrapper()).build());
                mappedStatement.getConfiguration().addMappedStatement(new MappedStatement.Builder(mappedStatement.getConfiguration(), "111", new DynamicSqlSource(mappedStatement.getConfiguration(), new TextSqlNode("SELECT\n" +
                        " \n" +
                        "t1.id,t1.game_id,t1.game_size,t1.play_modes\n" +
                        " \n" +
                        " \n" +
                        "FROM game_rental_detail AS t1  \n" +
                        " \n" +
                        " WHERE (t1.game_id = #{gameId})")), mappedStatement.getSqlCommandType())
                        .resultMaps(CollUtil.newArrayList(new ResultMap.Builder(ms.getConfiguration(), "com.iv.ersr.game.mapper.GameRentalInfoMapper.mybatis-plus_GameRentalDetail", GameRentalDetail.class, new ArrayList<>(), true).build()))
                        .build());
                ms.getConfiguration().addMappedStatement(new MappedStatement.Builder(mappedStatement.getConfiguration(), "111", new DynamicSqlSource(mappedStatement.getConfiguration(), new TextSqlNode("SELECT\n" +
                        " \n" +
                        "t1.id,t1.game_id,t1.game_size,t1.play_modes\n" +
                        " \n" +
                        " \n" +
                        "FROM game_rental_detail AS t1  \n" +
                        " \n" +
                        " WHERE (t1.game_id = #{gameId})")), mappedStatement.getSqlCommandType())
                        .resultMaps(CollUtil.newArrayList(new ResultMap.Builder(ms.getConfiguration(), "com.iv.ersr.game.mapper.GameRentalInfoMapper.mybatis-plus_GameRentalDetail", GameRentalDetail.class, new ArrayList<>(), true).build()))
                        .build());
                StringBuilder stringBuilder = new StringBuilder();
                List<ResultMapping> composites = new ArrayList<>();
                for (FieldMapping fieldMapping : collectionResultMap.getFieldMappings()) {
                    stringBuilder.append(fieldMapping.getParamName() + "=" + fieldMapping.getColumnName()).append(StringPool.COMMA);
                    composites.add(new ResultMapping.Builder(ms.getConfiguration(),
                            fieldMapping.getParamName(), fieldMapping.getColumnName(), Object.class)
                            .build());
                }
                String collect = CharSequenceUtil.subBefore(stringBuilder.toString(), StringPool.COMMA, true);
                ResultMapping builder = new ResultMapping.Builder(ms.getConfiguration(),
                        collectionResultMap.getPropertyName(), "{" + collect + "}", List.class)
                        .nestedQueryId(collectionResultMap.getId())
                        .composites(composites).build();
                newResultMappings.add(builder);
            }
        }
        return new ResultMap.Builder(ms.getConfiguration(), resultMap.getId(), resultMap.getType(), newResultMappings, true).build();
//        List<ResultMapping> resultMappings = buildResultMapping(configuration, joinLambdaQueryWrapper.getFieldMappingList(), classType);


//        List<OneToOneSelectBuild> oneToOneSelectBuildList = joinLambdaWrapper.getOneToOneSelectBuildList();
//        // 不为空就代表有一对一映射
//        if (CollectionUtils.isNotEmpty(oneToOneSelectBuildList)) {
//            for (OneToOneSelectBuild oneToOneSelectBuild : oneToOneSelectBuildList) {
//                // 构建ResultMap
//                String oneToOneId = id + StringPool.UNDERSCORE + oneToOneSelectBuild.getOneToOneField();
//                oneToOneId = oneToOneId.replaceAll(" ", "");
//                if (!configuration.hasResultMap(oneToOneId)) {
//                    ResultMap oneToOneResultMap = new ResultMap.Builder(configuration, oneToOneId,
//                                                                        oneToOneSelectBuild.getOneToOneClass(),
//                                                                        buildResultMapping(configuration, oneToOneSelectBuild.getBelongsColumns(), oneToOneSelectBuild.getOneToOneClass())).build();
//                    configuration.addResultMap(oneToOneResultMap);
//                }
//                resultMappings.add(new ResultMapping.Builder(configuration, oneToOneSelectBuild.getOneToOneField())
//                                           .javaType(oneToOneSelectBuild.getOneToOneClass()).nestedResultMapId(oneToOneId).build());
//            }
//        }
//
//        List<ManyToManySelectBuild> manyToManySelectBuildList = joinLambdaWrapper.getManyToManySelectBuildList();
//        // 不为空就代表有多对多映射
//        if (CollectionUtils.isNotEmpty(manyToManySelectBuildList)) {
//            for (ManyToManySelectBuild manyToManySelectBuild : manyToManySelectBuildList) {
//                // 构建ResultMap
//                String manyToManyId = id + StringPool.UNDERSCORE + manyToManySelectBuild.getManyToManyField();
//                manyToManyId = manyToManyId.replaceAll(" ", "");
//                if (!configuration.hasResultMap(manyToManyId)) {
//                    ResultMap oneToOneResultMap = new ResultMap.Builder(configuration, manyToManyId, manyToManySelectBuild.getManyToManyClass(),
//                                                                        buildResultMapping(configuration, manyToManySelectBuild.getBelongsColumns(),
//                                                                                           manyToManySelectBuild.getManyToManyClass())).build();
//                    configuration.addResultMap(oneToOneResultMap);
//                }
//                resultMappings.add(new ResultMapping.Builder(configuration, manyToManySelectBuild.getManyToManyField())
//                                           .javaType(manyToManySelectBuild.getManyToManyPropertyType()).nestedResultMapId(manyToManyId).build());
//            }
//        }
    }
}
