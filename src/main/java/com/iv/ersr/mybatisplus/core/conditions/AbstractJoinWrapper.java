package com.iv.ersr.mybatisplus.core.conditions;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.ColumnSegment;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.enums.SqlLike;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.iv.ersr.mybatisplus.core.conditions.func.JoinCompare;
import com.iv.ersr.mybatisplus.core.conditions.func.JoinFunc;
import com.iv.ersr.mybatisplus.core.conditions.func.JoinMethodFunc;
import com.iv.ersr.mybatisplus.core.entity.enums.SqlExcerpt;

import java.util.Collection;

import static com.baomidou.mybatisplus.core.enums.SqlKeyword.*;
import static com.iv.ersr.mybatisplus.core.entity.enums.SqlExcerpt.LEFT_JOIN;
import static com.iv.ersr.mybatisplus.core.entity.enums.SqlExcerpt.RIGHT_JOIN;

/**
 * Lambda 语法使用 Wrapper
 * <p>统一处理解析 lambda 获取 column</p>
 *
 * @author moxiaoyu
 * @since 2023-03-22
 */
public abstract class AbstractJoinWrapper<T, Children extends AbstractJoinWrapper<T, Children>>
        extends AbstractWrapper<T, SFunction<T, ?>, Children> implements JoinCompare<Children, SFunction<T, ?>>, JoinFunc<Children, SFunction<T, ?>>, JoinMethodFunc<Children, T> {

    @Override
    public <J> Children leftJoin(boolean condition, SFunction<T, ?> masterTableField, SFunction<J, ?> joinTableField, String alias) {
        return maybeDo(condition, ()->appendSqlSegments(LEFT_JOIN, masterTableField, joinTableField, alias));
    }

    @Override
    public <J> Children rightJoin(boolean condition, SFunction<T, ?> masterTableField, SFunction<J, ?> joinTableField, String alias) {
        return maybeDo(condition, ()->appendSqlSegments(RIGHT_JOIN, masterTableField, joinTableField, alias));
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

    @Override
    public <J> Children jLike(boolean condition, SFunction<J, ?> column, Object val) {
        return joinLikeValue(condition, column, LIKE, val, SqlLike.DEFAULT);
    }

    @Override
    public <J> Children jLikeLeft(boolean condition, SFunction<J, ?> column, Object val) {
        return joinLikeValue(condition, column, LIKE, val, SqlLike.LEFT);
    }

    @Override
    public <J> Children jLikeRight(boolean condition, SFunction<J, ?> column, Object val) {
        return joinLikeValue(condition, column, LIKE, val, SqlLike.RIGHT);
    }

    @Override
    public <J> Children jIn(boolean condition, SFunction<J, ?> column, Collection<?> coll) {
        return maybeDo(condition, () -> appendSqlSegments(joinColumnToSqlSegment(column), IN, inExpression(coll)));
    }

    @Override
    public <J> Children jIn(boolean condition, SFunction<J, ?> column, Object... values) {
        return maybeDo(condition, () -> appendSqlSegments(joinColumnToSqlSegment(column), IN, inExpression(values)));
    }

    @Override
    public <J> Children jNotIn(boolean condition, SFunction<J, ?> column, Collection<?> coll) {
        return maybeDo(condition, () -> appendSqlSegments(joinColumnToSqlSegment(column), NOT_IN, inExpression(coll)));
    }

    @Override
    public <J> Children jNotIn(boolean condition, SFunction<J, ?> column, Object... values) {
        return maybeDo(condition, () -> appendSqlSegments(joinColumnToSqlSegment(column), NOT_IN, inExpression(values)));
    }

    protected <J> Children joinLikeValue(boolean condition, SFunction<J, ?> column, SqlKeyword keyword, Object val, SqlLike sqlLike) {
        return maybeDo(condition, () -> appendSqlSegments(joinColumnToSqlSegment(column), keyword,
                () -> formatParam(null, SqlUtils.concatLike(val, sqlLike))));
    }

    protected <J> Children joinAddCondition(boolean condition, SFunction<J, ?> column, SqlKeyword sqlKeyword, Object val) {
        return maybeDo(condition, () -> appendSqlSegments(joinColumnToSqlSegment(column), sqlKeyword,
                () -> formatParam(null, val)));
    }

    protected final <J> ColumnSegment joinColumnToSqlSegment(SFunction<J, ?> column) {
        return () -> joinColumnToString(column);
    }

    protected <J> String joinColumnToString(SFunction<J, ?> column) { return null; }

    protected <J> void appendSqlSegments(SqlExcerpt sqlExcerpt, SFunction<T, ?> masterTableField, SFunction<J, ?> joinTableField, String alias) {}
}
