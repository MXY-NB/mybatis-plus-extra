package com.iv.ersr.mybatisplus.core.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.iv.ersr.mybatisplus.core.annotaion.IgnoreInsert;
import com.iv.ersr.mybatisplus.core.annotaion.IgnoreUpdate;
import com.iv.ersr.mybatisplus.core.injector.methods.InsertList;
import com.iv.ersr.mybatisplus.core.injector.methods.JoinSelectList;
import com.iv.ersr.mybatisplus.core.injector.methods.UpdateBatchById;
import com.iv.ersr.mybatisplus.core.injector.methods.UpdateBatchByIdWithNull;

import java.util.List;

/**
 * <p>
 * 自定义SQL注入器
 * </p>
 *
 * @author IVI04
 * @since 2023-03-22
 */
public class DefaultSqlInjectorPlus extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo){
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        methodList.add(new InsertList(x -> x.getField().getDeclaredAnnotation(IgnoreInsert.class) == null && !x.isLogicDelete()));
        methodList.add(new UpdateBatchByIdWithNull(x -> x.getField().getDeclaredAnnotation(IgnoreUpdate.class) == null));
        methodList.add(new UpdateBatchById(x -> x.getField().getDeclaredAnnotation(IgnoreUpdate.class) == null));
        methodList.add(new JoinSelectList());
        return methodList;
    }
}