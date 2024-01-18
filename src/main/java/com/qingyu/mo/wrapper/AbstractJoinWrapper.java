package com.qingyu.mo.wrapper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.segments.ColumnSegment;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.enums.SqlLike;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlUtils;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.qingyu.mo.constant.ConstantPlus;
import com.qingyu.mo.entity.JoinResultMap;
import com.qingyu.mo.exception.Exceptions;
import com.qingyu.mo.func.JoinCompare;
import com.qingyu.mo.func.JoinFunc;
import com.qingyu.mo.func.JoinMethod;
import com.qingyu.mo.utils.JoinSqlScriptUtil;
import com.qingyu.mo.wrapper.query.JoinLambdaQueryWrapper;
import lombok.Getter;

import java.util.*;
import java.util.function.Consumer;

import static com.baomidou.mybatisplus.core.enums.SqlKeyword.*;
import static java.util.stream.Collectors.joining;

/**
 * Lambda 语法使用 JoinWrapper
 * <p>统一处理解析 lambda 获取 column</p>
 *
 * @author qingyu-mo
 * @since 2023-12-19
 */
public abstract class AbstractJoinWrapper<T, Children extends AbstractJoinWrapper<T, Children>>
        extends AbstractWrapper<T, SFunction<T, ?>, Children>
        implements JoinCompare<Children, SFunction<T, ?>, T>, JoinFunc<Children, SFunction<T, ?>>, JoinMethod<Children, SFunction<T, ?>> {

    /**
     * 查询字段
     */
    protected List<SharedString> sqlSelect;

    /**
     * 不想要查询的字段
     */
    protected List<SharedString> noSqlSelect;

    /**
     * join查询字段
     */
    protected List<SharedString> sqlJoinSelect;

    /**
     * join语句
     */
    protected LinkedList<SharedString> sqlJoinList;

    /**
     * join的class别名map
     */
    protected Map<Class<?>, String> joinClassAliasMap;

    /**
     * join的class别名map
     */
    protected Integer joinNumber;

    /**
     * 要联表的字段缓存
     */
    protected Map<Class<?>, Map<String, ColumnCache>> joinClassColumnMap;

    /**
     * 查询字段
     */
    @Getter
    protected List<JoinResultMap> joinResultMaps;

    /**
     * 主表别名
     */
    @Getter
    protected String masterTableAlias;

    /**
     * 主表别名
     */
    @Getter
    protected Boolean isFormatParam = true;

    @Override
    protected void initNeed() {
        super.initNeed();
        sqlJoinList = new LinkedList<>();
        joinClassAliasMap = new HashMap<>(16);
        joinNumber = 1;
        joinClassColumnMap = new HashMap<>(16);
        joinResultMaps = new ArrayList<>();
        sqlSelect = new ArrayList<>();
        noSqlSelect = new ArrayList<>();
        sqlJoinSelect = new ArrayList<>();
        masterTableAlias = ConstantPlus.DEFAULT_TABLE_ALIAS_NAME;
    }

    protected Integer getJoinNumber(){
        return ++joinNumber;
    }

    /**
     * 多重嵌套查询条件
     * @param consumer 消费函数
     */
    protected Children getInstance(Consumer<Children> consumer) {
        final Children instance = instance();
        consumer.accept(instance);
        return instance;
    }

    /**
     * 获取表别名
     * @param alias 别名
     * @return String
     */
    protected String getAlias(String alias) {
        return CharSequenceUtil.isEmpty(alias) ? "t" + getJoinNumber() : alias;
    }

    @Override
    protected String columnToString(SFunction<T, ?> column) {
        return columnToString(column, null);
    }

    protected String columnToString(boolean onlyColumn, SFunction<T, ?> column) {
        return columnToString(onlyColumn, column, null);
    }

    protected String columnToString(SFunction<T, ?> column, String alias) {
        return columnToString(false, column, alias);
    }

    protected String columnToString(boolean onlyColumn, SFunction<T, ?> column, String alias) {
        return columnToString(onlyColumn, true, column, alias);
    }

    protected <J> String joinColumnToString(SFunction<J, ?> column) {
        return joinColumnToString(column, null);
    }

    protected <J> String joinColumnToString(boolean onlyColumn, SFunction<J, ?> column) {
        return joinColumnToString(onlyColumn, column, null);
    }

    protected <J> String joinColumnToString(SFunction<J, ?> column, String alias) {
        return joinColumnToString(false, column, alias);
    }

    protected <J> String joinColumnToString(boolean onlyColumn, SFunction<J, ?> column, String alias) {
        return columnToString(onlyColumn, false, column, alias);
    }

    protected String columnToString(SFunction<?, ?> column, boolean isMainTable) {
        return columnToString(false, isMainTable, column, null);
    }

    @SafeVarargs
    @Override
    protected final String columnsToString(SFunction<T, ?>... columns) {
        return columnsToString(false, columns);
    }

    @Override
    protected final String columnsToString(List<SFunction<T, ?>> columns) {
        return columnsToString(false, columns);
    }

    @SafeVarargs
    protected final String columnsToString(boolean onlyColumn, SFunction<T, ?>... columns) {
        return columnsToString(onlyColumn, Arrays.asList(columns));
    }

    protected final String columnsToString(boolean onlyColumn, List<SFunction<T, ?>> columns) {
        return columns.stream().map(i -> columnToString(onlyColumn, i, null)).collect(joining(StringPool.COMMA));
    }

    @SafeVarargs
    protected final <J> String joinColumnsToString(SFunction<J, ?>... columns) {
        return joinColumnsToString(false, columns);
    }

    protected final <J> String joinColumnsToString(List<SFunction<J, ?>> columns) {
        return joinColumnsToString(false, columns);
    }

    @SafeVarargs
    protected final <J> String joinColumnsToString(boolean onlyColumn, SFunction<J, ?>... columns) {
        return joinColumnsToString(onlyColumn, Arrays.asList(columns));
    }

    protected final <J> String joinColumnsToString(boolean onlyColumn, List<SFunction<J, ?>> columns) {
        return columns.stream().map(i -> joinColumnToString(onlyColumn, i, null)).collect(joining(StringPool.COMMA));
    }

    /**
     * 获取字段名称
     * @param onlyColumn 只需要字段名称
     * @param mainTable 是否主表
     * @param column 字段
     * @param alias 别名
     * @return String
     */
    protected abstract String columnToString(boolean onlyColumn, boolean mainTable, SFunction<?, ?> column, String alias);

    protected final <J> ColumnSegment joinColumnToSqlSegment(SFunction<J, ?> column) {
        return () -> joinColumnToString(column);
    }

    protected final ColumnSegment columnToSqlSegment(String template, Object... params) {
        return () -> String.format(template, params);
    }

    protected final ColumnSegment strToSqlSegment(String str) {
        return () -> str;
    }

    protected final ColumnSegment paramToSqlSegment(Object val) {
        return () -> formatParam(null, val);
    }

    protected void appendSelectSqlSegments(ISqlSegment... sqlSegments) {
        for (ISqlSegment sqlSegment : sqlSegments) {
            sqlSelect.add(new SharedString(sqlSegment.getSqlSegment()));
        }
    }

    protected void appendJoinSelectSqlSegments(ISqlSegment... sqlSegments) {
        for (ISqlSegment sqlSegment : sqlSegments) {
            sqlJoinSelect.add(new SharedString(sqlSegment.getSqlSegment()));
        }
    }

    protected void appendNoSelectSqlSegments(ISqlSegment... sqlSegments) {
        for (ISqlSegment sqlSegment : sqlSegments) {
            noSqlSelect.add(new SharedString(sqlSegment.getSqlSegment()));
        }
    }

    // ======================= JoinCompare ======================== //

    @Override
    public <J> Children eq(boolean condition, SFunction<T, ?> column, SFunction<J, ?> column2) {
        return addCondition(condition, column, EQ, column2);
    }

    @Override
    public <J> Children eq(Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer, Object val) {
        appendSqlSegments(() -> addChildSelect(entityClass, consumer), EQ, paramToSqlSegment(val));
        return typedThis;
    }

    @Override
    public <J> Children ne(boolean condition, SFunction<T, ?> column, SFunction<J, ?> column2) {
        return addCondition(condition, column, NE, column2);
    }

    @Override
    public <J> Children gt(boolean condition, SFunction<T, ?> column, SFunction<J, ?> column2) {
        return addCondition(condition, column, GT, column2);
    }

    @Override
    public <J> Children ge(boolean condition, SFunction<T, ?> column, SFunction<J, ?> column2) {
        return addCondition(condition, column, GE, column2);
    }

    @Override
    public <J> Children lt(boolean condition, SFunction<T, ?> column, SFunction<J, ?> column2) {
        return addCondition(condition, column, LT, column2);
    }

    @Override
    public <J> Children le(boolean condition, SFunction<T, ?> column, SFunction<J, ?> column2) {
        return addCondition(condition, column, LE, column2);
    }

    protected <J> Children addCondition(boolean condition, SFunction<T, ?> column, SqlKeyword sqlKeyword, SFunction<J, ?> column2) {
        return maybeDo(condition, () -> appendSqlSegments(columnToSqlSegment(column), sqlKeyword, joinColumnToSqlSegment(column2)));
    }

    @Override
    public <J> Children jEq(boolean condition, SFunction<J, ?> column, Object val) {
        return joinAddCondition(condition, column, EQ, val);
    }

    @Override
    public <J> Children jNe(boolean condition, SFunction<J, ?> column, Object val) {
        return joinAddCondition(condition, column, NE, val);
    }

    @Override
    public <J> Children jGt(boolean condition, SFunction<J, ?> column, Object val) {
        return joinAddCondition(condition, column, GT, val);
    }

    @Override
    public <J> Children jGe(boolean condition, SFunction<J, ?> column, Object val) {
        return joinAddCondition(condition, column, GE, val);
    }

    @Override
    public <J> Children jLt(boolean condition, SFunction<J, ?> column, Object val) {
        return joinAddCondition(condition, column, LT, val);
    }

    @Override
    public <J> Children jLe(boolean condition, SFunction<J, ?> column, Object val) {
        return joinAddCondition(condition, column, LE, val);
    }

    protected <J> Children joinAddCondition(boolean condition, SFunction<J, ?> column, SqlKeyword sqlKeyword, Object val) {
        return maybeDo(condition, () -> appendSqlSegments(joinColumnToSqlSegment(column), sqlKeyword, () -> formatParam(null, val)));
    }

    @Override
    public <J> Children jEq(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2) {
        return joinAddCondition(condition, column, EQ, column2);
    }

    @Override
    public <J> Children jNe(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2) {
        return joinAddCondition(condition, column, NE, column2);
    }

    @Override
    public <J> Children jGt(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2) {
        return joinAddCondition(condition, column, GT, column2);
    }

    @Override
    public <J> Children jGe(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2) {
        return joinAddCondition(condition, column, GE, column2);
    }

    @Override
    public <J> Children jLt(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2) {
        return joinAddCondition(condition, column, LT, column2);
    }

    @Override
    public <J> Children jLe(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2) {
        return joinAddCondition(condition, column, LE, column2);
    }

    protected <J> Children joinAddCondition(boolean condition, SFunction<J, ?> column, SqlKeyword sqlKeyword, SFunction<J, ?> column2) {
        return maybeDo(condition, () -> appendSqlSegments(joinColumnToSqlSegment(column), sqlKeyword, joinColumnToSqlSegment(column2)));
    }

    @Override
    public Children sumEq(boolean condition, SFunction<T, ?> column, Object val) {
        return addSumBeforeCondition(condition, column, EQ, val);
    }

    @Override
    public <J> Children sumEq(boolean condition, SFunction<T, ?> column, SFunction<J, ?> column2) {
        return addSumBeforeCondition(condition, column, EQ, column2);
    }

    @Override
    public Children sumNe(boolean condition, SFunction<T, ?> column, Object val) {
        return addSumBeforeCondition(condition, column, NE, val);
    }

    @Override
    public <J> Children sumNe(boolean condition, SFunction<T, ?> column, SFunction<J, ?> column2) {
        return addSumBeforeCondition(condition, column, NE, column2);
    }

    @Override
    public Children sumGt(boolean condition, SFunction<T, ?> column, Object val) {
        return addSumBeforeCondition(condition, column, GT, val);
    }

    @Override
    public <J> Children sumGt(boolean condition, SFunction<T, ?> column, SFunction<J, ?> column2) {
        return addSumBeforeCondition(condition, column, GT, column2);
    }

    @Override
    public Children sumGe(boolean condition, SFunction<T, ?> column, Object val) {
        return addSumBeforeCondition(condition, column, GE, val);
    }

    @Override
    public <J> Children sumGe(boolean condition, SFunction<T, ?> column, SFunction<J, ?> column2) {
        return addSumBeforeCondition(condition, column, GE, column2);
    }

    @Override
    public Children sumLt(boolean condition, SFunction<T, ?> column, Object val) {
        return addSumBeforeCondition(condition, column, LT, val);
    }

    @Override
    public <J> Children sumLt(boolean condition, SFunction<T, ?> column, SFunction<J, ?> column2) {
        return addSumBeforeCondition(condition, column, LT, column2);
    }

    @Override
    public Children sumLe(boolean condition, SFunction<T, ?> column, Object val) {
        return addSumBeforeCondition(condition, column, LE, val);
    }

    @Override
    public <J> Children sumLe(boolean condition, SFunction<T, ?> column, SFunction<J, ?> column2) {
        return addSumBeforeCondition(condition, column, LE, column2);
    }

    protected Children addSumBeforeCondition(boolean condition, SFunction<T, ?> column, SqlKeyword sqlKeyword, Object val) {
        return maybeDo(condition, () -> appendSqlSegments(columnToSqlSegment(ConstantPlus.SUM_IF_NULL, columnToString(column)), sqlKeyword, paramToSqlSegment(val)));
    }

    protected <J> Children addSumBeforeCondition(boolean condition, SFunction<T, ?> column, SqlKeyword sqlKeyword, SFunction<J, ?> column2) {
        return maybeDo(condition, () -> appendSqlSegments(columnToSqlSegment(ConstantPlus.SUM_IF_NULL, columnToString(column)), sqlKeyword, joinColumnToSqlSegment(column2)));
    }

    @Override
    public <J> Children jSumEq(boolean condition, SFunction<J, ?> column, Object val) {
        return joinAddSumBeforeCondition(condition, column, EQ, val);
    }

    @Override
    public <J> Children jSumEq(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2) {
        return joinAddSumBeforeCondition(condition, column, EQ, column2);
    }

    @Override
    public <J> Children jSumNe(boolean condition, SFunction<J, ?> column, Object val) {
        return joinAddSumBeforeCondition(condition, column, NE, val);
    }

    @Override
    public <J> Children jSumNe(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2) {
        return joinAddSumBeforeCondition(condition, column, NE, column2);
    }

    @Override
    public <J> Children jSumGt(boolean condition, SFunction<J, ?> column, Object val) {
        return joinAddSumBeforeCondition(condition, column, GT, val);
    }

    @Override
    public <J> Children jSumGt(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2) {
        return joinAddSumBeforeCondition(condition, column, GT, column2);
    }

    @Override
    public <J> Children jSumGe(boolean condition, SFunction<J, ?> column, Object val) {
        return joinAddSumBeforeCondition(condition, column, GE, val);
    }

    @Override
    public <J> Children jSumGe(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2) {
        return joinAddSumBeforeCondition(condition, column, GE, column2);
    }

    @Override
    public <J> Children jSumLt(boolean condition, SFunction<J, ?> column, Object val) {
        return joinAddSumBeforeCondition(condition, column, LT, val);
    }

    @Override
    public <J> Children jSumLt(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2) {
        return joinAddSumBeforeCondition(condition, column, LT, column2);
    }

    @Override
    public <J> Children jSumLe(boolean condition, SFunction<J, ?> column, Object val) {
        return joinAddSumBeforeCondition(condition, column, LE, val);
    }

    @Override
    public <J> Children jSumLe(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2) {
        return joinAddSumBeforeCondition(condition, column, LE, column2);
    }

    protected <J> Children joinAddSumBeforeCondition(boolean condition, SFunction<J, ?> column, SqlKeyword sqlKeyword, Object val) {
        return maybeDo(condition, () -> appendSqlSegments(columnToSqlSegment(ConstantPlus.SUM_IF_NULL, joinColumnToString(column)), sqlKeyword, paramToSqlSegment(val)));
    }

    protected <J> Children joinAddSumBeforeCondition(boolean condition, SFunction<J, ?> column, SqlKeyword sqlKeyword, SFunction<J, ?> column2) {
        return maybeDo(condition, () -> appendSqlSegments(columnToSqlSegment(ConstantPlus.SUM_IF_NULL, joinColumnToString(column)), sqlKeyword, joinColumnToSqlSegment(column2)));
    }

    @Override
    public Children eqSum(boolean condition, Object val, SFunction<T, ?> column) {
        return addSumAfterCondition(condition, column, EQ, val);
    }

    @Override
    public <J> Children eqSum(boolean condition, SFunction<T, ?> column, SFunction<J, ?> column2) {
        return addSumAfterCondition(condition, column, EQ, column2);
    }

    @Override
    public Children neSum(boolean condition, Object val, SFunction<T, ?> column) {
        return addSumAfterCondition(condition, column, NE, val);
    }

    @Override
    public <J> Children neSum(boolean condition, SFunction<T, ?> column, SFunction<J, ?> column2) {
        return addSumAfterCondition(condition, column, NE, column2);
    }

    @Override
    public Children gtSum(boolean condition, Object val, SFunction<T, ?> column) {
        return addSumAfterCondition(condition, column, GT, val);
    }

    @Override
    public <J> Children gtSum(boolean condition, SFunction<T, ?> column, SFunction<J, ?> column2) {
        return addSumAfterCondition(condition, column, GT, column2);
    }

    @Override
    public Children geSum(boolean condition, Object val, SFunction<T, ?> column) {
        return addSumAfterCondition(condition, column, GE, val);
    }

    @Override
    public <J> Children geSum(boolean condition, SFunction<T, ?> column, SFunction<J, ?> column2) {
        return addSumAfterCondition(condition, column, GE, column2);
    }

    @Override
    public Children ltSum(boolean condition, Object val, SFunction<T, ?> column) {
        return addSumAfterCondition(condition, column, LT, val);
    }

    @Override
    public <J> Children ltSum(boolean condition, SFunction<T, ?> column, SFunction<J, ?> column2) {
        return addSumAfterCondition(condition, column, LT, column2);
    }

    @Override
    public Children leSum(boolean condition, Object val, SFunction<T, ?> column) {
        return addSumAfterCondition(condition, column, LE, val);
    }

    @Override
    public <J> Children leSum(boolean condition, SFunction<T, ?> column, SFunction<J, ?> column2) {
        return addSumAfterCondition(condition, column, LE, column2);
    }

    protected Children addSumAfterCondition(boolean condition, SFunction<T, ?> column, SqlKeyword sqlKeyword, Object val) {
        return maybeDo(condition, () -> appendSqlSegments(paramToSqlSegment(val), sqlKeyword, columnToSqlSegment(ConstantPlus.SUM_IF_NULL, columnToString(column))));
    }

    protected <J> Children addSumAfterCondition(boolean condition, SFunction<T, ?> column, SqlKeyword sqlKeyword, SFunction<J, ?> column2) {
        return maybeDo(condition, () -> appendSqlSegments(columnToSqlSegment(column), sqlKeyword, columnToSqlSegment(ConstantPlus.SUM_IF_NULL, joinColumnToString(column2))));
    }

    @Override
    public <J> Children jEqSum(boolean condition, Object val, SFunction<J, ?> column) {
        return joinAddSumAfterCondition(condition, column, EQ, val);
    }

    @Override
    public <J> Children jEqSum(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2) {
        return joinAddSumAfterCondition(condition, column, EQ, column2);
    }

    @Override
    public <J> Children jNeSum(boolean condition, Object val, SFunction<J, ?> column) {
        return joinAddSumAfterCondition(condition, column, NE, val);
    }

    @Override
    public <J> Children jNeSum(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2) {
        return joinAddSumAfterCondition(condition, column, NE, column2);
    }

    @Override
    public <J> Children jGtSum(boolean condition, Object val, SFunction<J, ?> column) {
        return joinAddSumAfterCondition(condition, column, GT, val);
    }

    @Override
    public <J> Children jGtSum(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2) {
        return joinAddSumAfterCondition(condition, column, GT, column2);
    }

    @Override
    public <J> Children jGeSum(boolean condition, Object val, SFunction<J, ?> column) {
        return joinAddSumAfterCondition(condition, column, GE, val);
    }

    @Override
    public <J> Children jGeSum(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2) {
        return joinAddSumAfterCondition(condition, column, GE, column2);
    }

    @Override
    public <J> Children jLtSum(boolean condition, Object val, SFunction<J, ?> column) {
        return joinAddSumAfterCondition(condition, column, LT, val);
    }

    @Override
    public <J> Children jLtSum(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2) {
        return joinAddSumAfterCondition(condition, column, LT, column2);
    }

    @Override
    public <J> Children jLeSum(boolean condition, Object val, SFunction<J, ?> column) {
        return joinAddSumAfterCondition(condition, column, LE, val);
    }

    @Override
    public <J> Children jLeSum(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2) {
        return joinAddSumAfterCondition(condition, column, LE, column2);
    }

    protected <J> Children joinAddSumAfterCondition(boolean condition, SFunction<J, ?> column, SqlKeyword sqlKeyword, Object val) {
        return maybeDo(condition, () -> appendSqlSegments(paramToSqlSegment(val), sqlKeyword, columnToSqlSegment(ConstantPlus.SUM_IF_NULL, joinColumnToString(column))));
    }

    protected <J> Children joinAddSumAfterCondition(boolean condition, SFunction<J, ?> column, SqlKeyword sqlKeyword, SFunction<J, ?> column2) {
        return maybeDo(condition, () -> appendSqlSegments(joinColumnToSqlSegment(column), sqlKeyword, columnToSqlSegment(ConstantPlus.SUM_IF_NULL, joinColumnToString(column2))));
    }

    @Override
    public Children countGt(boolean condition, SFunction<T, ?> column, Object val) {
        return addCountCondition(condition, column, GT, val);
    }

    @Override
    public Children countGe(boolean condition, SFunction<T, ?> column, Object val) {
        return addCountCondition(condition, column, GE, val);
    }

    @Override
    public Children countLt(boolean condition, SFunction<T, ?> column, Object val) {
        return addCountCondition(condition, column, LT, val);
    }

    @Override
    public Children countLe(boolean condition, SFunction<T, ?> column, Object val) {
        return addCountCondition(condition, column, LE, val);
    }

    protected Children addCountCondition(boolean condition, SFunction<T, ?> column, SqlKeyword sqlKeyword, Object val) {
        return maybeDo(condition, () -> appendSqlSegments(strToSqlSegment(String.format(ConstantPlus.COUNT, columnToString(column))), sqlKeyword, paramToSqlSegment(val)));
    }

    @Override
    public <J> Children jCountGt(boolean condition, SFunction<J, ?> column, Object val) {
        return jAddCountCondition(condition, column, GT, val);
    }

    @Override
    public <J> Children jCountGe(boolean condition, SFunction<J, ?> column, Object val) {
        return jAddCountCondition(condition, column, GE, val);
    }

    @Override
    public <J> Children jCountLt(boolean condition, SFunction<J, ?> column, Object val) {
        return jAddCountCondition(condition, column, LT, val);
    }

    @Override
    public <J> Children jCountLe(boolean condition, SFunction<J, ?> column, Object val) {
        return jAddCountCondition(condition, column, LE, val);
    }

    protected <J> Children jAddCountCondition(boolean condition, SFunction<J, ?> column, SqlKeyword sqlKeyword, Object val) {
        return maybeDo(condition, () -> appendSqlSegments(strToSqlSegment(String.format(ConstantPlus.COUNT, joinColumnToString(column))), sqlKeyword, paramToSqlSegment(val)));
    }

    @Override
    public <J> Children jBetween(boolean condition, SFunction<J, ?> column, Object val1, Object val2) {
        return maybeDo(condition, () -> appendSqlSegments(joinColumnToSqlSegment(column), BETWEEN,
                paramToSqlSegment(val1), AND, paramToSqlSegment(val2)));
    }

    @Override
    public <J> Children jNotBetween(boolean condition, SFunction<J, ?> column, Object val1, Object val2) {
        return maybeDo(condition, () -> appendSqlSegments(joinColumnToSqlSegment(column), NOT_BETWEEN,
                paramToSqlSegment(val1), AND, paramToSqlSegment(val2)));
    }

    @Override
    public <J> Children jLike(boolean condition, SFunction<J, ?> column, Object val) {
        return joinLikeValue(condition, column, val, SqlLike.DEFAULT);
    }

    @Override
    public <J> Children jLikeLeft(boolean condition, SFunction<J, ?> column, Object val) {
        return joinLikeValue(condition, column, val, SqlLike.LEFT);
    }

    @Override
    public <J> Children jLikeRight(boolean condition, SFunction<J, ?> column, Object val) {
        return joinLikeValue(condition, column, val, SqlLike.RIGHT);
    }

    protected <J> Children joinLikeValue(boolean condition, SFunction<J, ?> column, Object val, SqlLike sqlLike) {
        return maybeDo(condition, () -> appendSqlSegments(joinColumnToSqlSegment(column), LIKE, paramToSqlSegment(SqlUtils.concatLike(val, sqlLike))));
    }

    @Override
    public Children jsonContain(boolean condition, SFunction<T, ?> column, Collection<?> coll) {
        return maybeDo(condition, () -> appendSqlSegments(columnToSqlSegment(ConstantPlus.JSON_CONTAIN, columnToString(column)),
                inExpression(coll), strToSqlSegment(StringPool.RIGHT_BRACKET)));
    }

    @Override
    public Children jsonContainAny(boolean condition, SFunction<T, ?> column, Collection<?> coll) {
        return maybeDo(condition, () -> appendSqlSegments(columnToSqlSegment(ConstantPlus.JSON_OVERLAPS, columnToString(column)),
                inExpression(coll), strToSqlSegment(StringPool.RIGHT_BRACKET)));
    }

    // ======================= JoinFunc ======================== //

    @Override
    public <J> Children jOrderBy(boolean condition, boolean isAsc, List<SFunction<J, ?>> columns) {
        return maybeDo(condition, () -> columns.forEach(c -> appendSqlSegments(ORDER_BY,
                joinColumnToSqlSegment(c), isAsc ? ASC : DESC)));
    }

    @Override
    public Children orderBySum(boolean condition, boolean isAsc, SFunction<T, ?> column) {
        return maybeDo(condition, () -> appendSqlSegments(ORDER_BY, columnToSqlSegment(ConstantPlus.SUM, columnToString(column)), isAsc ? ASC : DESC));
    }

    @Override
    public <J> Children jOrderBySum(boolean condition, boolean isAsc, SFunction<J, ?> column) {
        return maybeDo(condition, () -> appendSqlSegments(ORDER_BY, columnToSqlSegment(ConstantPlus.SUM, joinColumnToString(column)), isAsc ? ASC : DESC));
    }

    @Override
    public <J> Children jIsNull(boolean condition, SFunction<J, ?> column) {
        return maybeDo(condition, () -> appendSqlSegments(joinColumnToSqlSegment(column), IS_NULL));
    }

    @Override
    public <J> Children jIsNotNull(boolean condition, SFunction<J, ?> column) {
        return maybeDo(condition, () -> appendSqlSegments(joinColumnToSqlSegment(column), IS_NOT_NULL));
    }

    @Override
    public <J> Children in(SFunction<T, ?> column, Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer) {
        appendSqlSegments(columnToSqlSegment(column), IN, () -> addChildSelect(entityClass, consumer));
        return typedThis;
    }

    @Override
    public <J> Children notIn(SFunction<T, ?> column, Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer) {
        appendSqlSegments(columnToSqlSegment(column), NOT_IN, () -> addChildSelect(entityClass, consumer));
        return typedThis;
    }

    @Override
    public <J> Children jIn(boolean condition, SFunction<J, ?> column, Collection<?> coll) {
        return maybeDo(condition, () -> appendSqlSegments(joinColumnToSqlSegment(column), IN, inExpression(coll)));
    }

    @Override
    public <J> Children jNotIn(boolean condition, SFunction<J, ?> column, Collection<?> coll) {
        return maybeDo(condition, () -> appendSqlSegments(joinColumnToSqlSegment(column), NOT_IN, inExpression(coll)));
    }

    @Override
    public Children inMonths(boolean condition, SFunction<T, ?> column, Collection<?> coll) {
        return maybeDo(condition, () -> appendSqlSegments(() -> JoinSqlScriptUtil.getDateFormatSql(columnToString(column)), IN, inExpression(coll)));
    }

    @Override
    public <J> Children jInMonths(boolean condition, SFunction<J, ?> column, Collection<?> coll) {
        return maybeDo(condition, () -> appendSqlSegments(() -> JoinSqlScriptUtil.getDateFormatSql(joinColumnToString(column)), IN, inExpression(coll)));
    }

    @Override
    public Children notInMonths(boolean condition, SFunction<T, ?> column, Collection<?> coll) {
        return maybeDo(condition, () -> appendSqlSegments(strToSqlSegment(JoinSqlScriptUtil.getDateFormatSql(columnToString(column))), NOT_IN, inExpression(coll)));
    }

    @Override
    public <J> Children jNotInMonths(boolean condition, SFunction<J, ?> column, Collection<?> coll) {
        return maybeDo(condition, () -> appendSqlSegments(strToSqlSegment(JoinSqlScriptUtil.getDateFormatSql(joinColumnToString(column))), NOT_IN, inExpression(coll)));
    }

    @Override
    public Children groupByMonth(boolean condition, SFunction<T, ?> column) {
        return maybeDo(condition, () -> appendSqlSegments(GROUP_BY, strToSqlSegment(JoinSqlScriptUtil.getDateFormatSql(columnToString(column)))));
    }

    @Override
    public <J> Children jGroupByMonth(boolean condition, SFunction<J, ?> column) {
        return maybeDo(condition, () -> appendSqlSegments(GROUP_BY, strToSqlSegment(JoinSqlScriptUtil.getDateFormatSql(joinColumnToString(column)))));
    }

    @Override
    public Children jHaving(boolean condition, Consumer<Children> consumer) {
        return maybeDo(condition, () -> appendSqlSegments(HAVING, () -> getInstance(consumer).getSqlSegment()));
    }

    // ======================= JoinMethod ======================== //

    @Override
    public Children joinOn(boolean condition, Consumer<Children> consumer) {
        return maybeDo(condition, () -> appendJoinSql(getInstance(consumer).getSqlSegment()));
    }

    @Override
    public <J> Children joinOn(boolean condition, SFunction<T, ?> column, SFunction<J, ?> column2) {
        return maybeDo(condition, () -> appendJoinSql(columnToString(column) + ConstantPlus.EQUALS_C + joinColumnToString(column2)));
    }

    @Override
    public <J> Children jJoinOn(boolean condition, SFunction<J, ?> column, SFunction<J, ?> column2) {
        return maybeDo(condition, () -> appendJoinSql(joinColumnToString(column) + ConstantPlus.EQUALS_C + joinColumnToString(column2)));
    }

    @Override
    public <J> Children innerJoin(boolean condition, SFunction<T, ?> column, SFunction<J, ?> column2, String alias) {
        return maybeDo(condition, () -> appendJoinSql(ConstantPlus.INNER_JOIN, column, true, column2, alias));
    }

    @Override
    public <J, K> Children jInnerJoin(boolean condition, SFunction<J, ?> column, SFunction<K, ?> column2, String alias) {
        return maybeDo(condition, () -> appendJoinSql(ConstantPlus.INNER_JOIN, column, false, column2, alias));
    }

    @Override
    public <J> Children innerJoin(boolean condition, SFunction<T, ?> column, Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer, String alias, String joinOn) {
        return maybeDo(condition, () -> appendJoinSql(ConstantPlus.INNER_JOIN, column, true, entityClass, consumer, alias, joinOn));
    }

    @Override
    public <J> Children jInnerJoin(boolean condition, SFunction<J, ?> column, Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer, String alias, String joinOn) {
        return maybeDo(condition, () -> appendJoinSql(ConstantPlus.INNER_JOIN, column, false, entityClass, consumer, alias, joinOn));
    }

    @Override
    public <J> Children leftJoin(boolean condition, SFunction<T, ?> column, SFunction<J, ?> column2, String alias) {
        return maybeDo(condition, () -> appendJoinSql(ConstantPlus.LEFT_JOIN, column, true, column2, alias));
    }

    @Override
    public <J, K> Children jLeftJoin(boolean condition, SFunction<J, ?> column, SFunction<K, ?> column2, String alias) {
        return maybeDo(condition, () -> appendJoinSql(ConstantPlus.LEFT_JOIN, column, false, column2, alias));
    }

    @Override
    public <J> Children leftJoin(boolean condition, SFunction<T, ?> column, Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer, String alias, String joinOn) {
        return maybeDo(condition, () -> appendJoinSql(ConstantPlus.LEFT_JOIN, column, true, entityClass, consumer, alias, joinOn));
    }

    @Override
    public <J> Children jLeftJoin(boolean condition, SFunction<J, ?> column, Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer, String alias, String joinOn) {
        return maybeDo(condition, () -> appendJoinSql(ConstantPlus.LEFT_JOIN, column, false, entityClass, consumer, alias, joinOn));
    }

    @Override
    public <J> Children rightJoin(boolean condition, SFunction<T, ?> column, SFunction<J, ?> column2, String alias) {
        return maybeDo(condition, () -> appendJoinSql(ConstantPlus.RIGHT_JOIN, column, true, column2, alias));
    }

    @Override
    public <J, K> Children jRightJoin(boolean condition, SFunction<J, ?> column, SFunction<K, ?> column2, String alias) {
        return maybeDo(condition, () -> appendJoinSql(ConstantPlus.RIGHT_JOIN, column, false, column2, alias));
    }

    @Override
    public <J> Children rightJoin(boolean condition, SFunction<T, ?> column, Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer, String alias, String joinOn) {
        return maybeDo(condition, () -> appendJoinSql(ConstantPlus.RIGHT_JOIN, column, true, entityClass, consumer, alias, joinOn));
    }

    @Override
    public <J> Children jRightJoin(boolean condition, SFunction<J, ?> column, Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer, String alias, String joinOn) {
        return maybeDo(condition, () -> appendJoinSql(ConstantPlus.RIGHT_JOIN, column, false, entityClass, consumer, alias, joinOn));
    }

    /**
     * 多重嵌套查询条件
     * @param entityClass 实体类
     * @param consumer 消费函数
     */
    protected <J> String addChildSelect(Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer) {
        Exceptions.t(getEntityClass() == null, "对象类为空！");
        joinClassAliasMap.put(getEntityClass(), masterTableAlias);
        final JoinLambdaQueryWrapper<J> instance = new JoinLambdaQueryWrapper<>(null, entityClass, paramNameSeq, paramNameValuePairs, new MergeSegments(),
                paramAlias, SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString(), joinClassAliasMap, joinNumber, joinClassColumnMap, getJoinResultMaps(), getAlias(null));
        consumer.accept(instance);
        String sql = instance.getSqlSelect();
        String joinSql = instance.getSqlJoinSelect();
        if (joinSql != null) {
            sql += joinSql;
        }
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entityClass);
        String format = String.format(ConstantPlus.CHILD_SELECT, sql, tableInfo.getTableName(), instance.getMasterTableAlias());
        String childSqlSegment = instance.getSqlSegment();
        if (CharSequenceUtil.isNotEmpty(childSqlSegment)) {
            format += ConstantPlus.WHERE_C + childSqlSegment;
        }
        return JoinSqlScriptUtil.bracket(format);
    }

    protected <J> void appendJoinSql(String sqlExcerpt, SFunction<?, ?> column, boolean isMainTable, SFunction<J, ?> joinTableField, String alias) {
        LambdaMeta lambdaMeta = LambdaUtils.extract(joinTableField);
        Class<?> instantiatedClass = lambdaMeta.getInstantiatedClass();
        alias = getAlias(alias);
        joinClassAliasMap.put(instantiatedClass, alias);
        TableInfo tableInfo = TableInfoHelper.getTableInfo(instantiatedClass);
        Exceptions.t(tableInfo == null, "请在对应实体类上加上TableName注解！");
        String joinTableName = tableInfo.getTableName();
        String sql = String.format(sqlExcerpt, joinTableName, alias);
        sqlJoinList.add(new SharedString(sql + columnToString(column, isMainTable) + ConstantPlus.EQUALS_C + joinColumnToString(joinTableField)));
    }

    protected <J> void appendJoinSql(String sqlJoinOn) {
        if (!sqlJoinList.isEmpty()) {
            sqlJoinList.getLast().setStringValue(sqlJoinList.getLast().getStringValue() + ConstantPlus.AND_C + sqlJoinOn);
        }
    }

    protected <J> void appendJoinSql(String sqlExcerpt, SFunction<?, ?> column, boolean isMainTable, Class<J> entityClass, Consumer<JoinLambdaQueryWrapper<J>> consumer, String alias, String joinOn) {
        String finalAlias = getAlias(alias);
        sqlJoinList.add(new SharedString(String.format(sqlExcerpt,
                addChildSelect(entityClass, consumer), finalAlias) +
                columnToString(column, isMainTable) +
                ConstantPlus.EQUALS_C + finalAlias + StringPool.DOT + joinOn));
    }

    public String getSqlJoin() {
        if (CollUtil.isEmpty(sqlJoinList)) {
            return StringPool.EMPTY;
        } else {
            return StringPool.NEWLINE + sqlJoinList.stream().map(SharedString::getStringValue).collect(joining(StringPool.NEWLINE));
        }
    }

    @Override
    public String getSqlSelect() {
        if (CollUtil.isEmpty(sqlSelect) && CollUtil.isNotEmpty(noSqlSelect)) {
            Exceptions.t(getEntityClass() == null, "对象类为空！");
            TableInfo tableInfo = TableInfoHelper.getTableInfo(getEntityClass());
            String columnsToString = noSqlSelect.stream().map(SharedString::getStringValue).collect(joining(StringPool.COMMA));
            appendSelectSqlSegments(strToSqlSegment(masterTableAlias + StrPool.DOT + tableInfo.getKeySqlSelect()));
            tableInfo.getFieldList().stream().filter(i->!columnsToString.contains(i.getColumn())).forEach(i->appendSelectSqlSegments(strToSqlSegment(masterTableAlias + StrPool.DOT + i.getSqlSelect())));
        }
        String sql = sqlSelect.stream().map(SharedString::getStringValue).collect(joining(StringPool.COMMA));
        return CharSequenceUtil.isEmpty(sql) ? null : sql;
    }

    public String getSqlJoinSelect() {
        String sql = sqlJoinSelect.stream().map(SharedString::getStringValue).collect(joining(StringPool.COMMA));
        return CharSequenceUtil.isEmpty(sql) ? null : StringPool.COMMA + sql;
    }

    @Override
    public void clear() {
        super.clear();
        sqlJoinList.clear();
        joinClassAliasMap.clear();
        joinNumber = 1;
        joinClassColumnMap.clear();
        joinResultMaps.clear();
        sqlSelect.clear();
        sqlJoinSelect.clear();
    }
}
