package com.iv.ersr.mybatisplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iv.ersr.mybatisplus.mapper.BaseMapperPlus;
import com.iv.ersr.mybatisplus.service.IServicePlus;

/**
 * <p>
 * 自定义Service
 * </p>
 *
 * @author moxiaoyu
 * @since 2023-03-22
 */
public class ServiceImplPlus<M extends BaseMapperPlus<T>, T> extends ServiceImpl<M, T> implements IServicePlus<T> {
}
