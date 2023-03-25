package com.iv.ersr.mybatisplus.core.toolkit;

import com.iv.ersr.mybatisplus.core.conditions.query.JoinLambdaQueryWrapper;

/**
 * <p>
 * Wrapper 条件构造
 * </p>
 *
 * @author MXY
 * @since 2023-03-22
 */
public final class JoinWrappers {

    private JoinWrappers() {
        // ignore
    }

    public static <T> JoinLambdaQueryWrapper<T> lambdaQuery() {
        return new JoinLambdaQueryWrapper<>();
    }
}
