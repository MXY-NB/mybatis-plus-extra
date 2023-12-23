package com.qingyu.mo.toolkit;

import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.qingyu.mo.wrapper.query.JoinLambdaQueryWrapper;

import java.util.Collections;
import java.util.Map;

/**
 * <p>
 * JoinWrapper 条件构造
 * </p>
 *
 * @author qingyu-mo
 * @since 2023-12-19
 */
public final class JoinWrappers {

    private static final JoinLambdaQueryWrapper<?> EMPTY_WRAPPER = new EmptyWrapper<>();

    private JoinWrappers() {
        // ignore
    }

    public static <T> JoinLambdaQueryWrapper<T> lambdaQuery() {
        return new JoinLambdaQueryWrapper<>();
    }

    @SuppressWarnings("unchecked")
    public static <T> JoinLambdaQueryWrapper<T> emptyWrapper() {
        return (JoinLambdaQueryWrapper<T>) EMPTY_WRAPPER;
    }

    /**
     * 一个空的QueryWrapper子类该类不包含任何条件
     * @param <T>
     * @see com.qingyu.mo.wrapper.query.JoinLambdaQueryWrapper
     */
    private static class EmptyWrapper<T> extends JoinLambdaQueryWrapper<T> {

        private static final long serialVersionUID = -2515957613998092273L;

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
