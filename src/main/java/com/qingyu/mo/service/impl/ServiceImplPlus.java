package com.qingyu.mo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingyu.mo.mapper.BaseMapperPlus;
import com.qingyu.mo.service.IServicePlus;

/**
 * <p>
 * 自定义Service
 * </p>
 *
 * @author qingyu-mo
 * @since 2023-12-19
 */
public class ServiceImplPlus<M extends BaseMapperPlus<T>, T> extends ServiceImpl<M, T> implements IServicePlus<T> {
}
