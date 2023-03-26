package com.iv.ersr.mybatisplus.core.toolkit;

import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.iv.ersr.mybatisplus.core.conditions.query.JoinLambdaQueryWrapper;

import java.util.Collections;
import java.util.Map;

/**
 * <p>
 * Wrapper 条件构造
 * </p>
 *
 * @author moxiaoyu
 * @since 2023-03-22
 */
public final class JoinWrappers {

    private static final JoinLambdaQueryWrapper<?> emptyWrapper = new EmptyWrapper<>();

    private JoinWrappers() {
        // ignore
    }

    public static <T> JoinLambdaQueryWrapper<T> lambdaQuery() {
        return new JoinLambdaQueryWrapper<>();
    }

    public static <T> JoinLambdaQueryWrapper<T> emptyWrapper() {
        return (JoinLambdaQueryWrapper<T>) emptyWrapper;
    }

    /**
     * 一个空的QueryWrapper子类该类不包含任何条件
     *
     * @param <T>
     * @see com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
     */
    private static class EmptyWrapper<T> extends JoinLambdaQueryWrapper<T> {

        private static final long serialVersionUID = -2515957613998092272L;

        @Override
        public T getEntity() {
            return null;
        }

        @Override
        public EmptyWrapper<T> setEntity(T entity) {
            throw new UnsupportedOperationException();
        }

        @Override
        public JoinLambdaQueryWrapper<T> setEntityClass(Class<T> entityClass) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Class<T> getEntityClass() {
            return null;
        }

        @Override
        public String getSqlSelect() {
            return null;
        }

        @Override
        public MergeSegments getExpression() {
            return null;
        }

        @Override
        public boolean isEmptyOfWhere() {
            return true;
        }

        @Override
        public boolean isEmptyOfNormal() {
            return true;
        }

        @Override
        public boolean nonEmptyOfEntity() {
            return !isEmptyOfEntity();
        }

        @Override
        public boolean isEmptyOfEntity() {
            return true;
        }

        @Override
        protected void initNeed() {
            // TODO document why this method is empty
        }

        @Override
        public EmptyWrapper<T> last(boolean condition, String lastSql) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getSqlSegment() {
            return null;
        }

        @Override
        public Map<String, Object> getParamNameValuePairs() {
            return Collections.emptyMap();
        }

        @Override
        protected EmptyWrapper<T> instance() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }
    }
}
