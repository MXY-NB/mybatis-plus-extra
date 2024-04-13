package com.qingyu.mo.mybatisplus.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.qingyu.mo.mybatisplus.annotaion.IgnoreInsert;
import com.qingyu.mo.mybatisplus.annotaion.IgnoreUpdate;
import com.qingyu.mo.mybatisplus.injector.methods.*;

import java.util.List;

/**
 * <p>
 * 自定义SQL注入器
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.6.2
 */
public class DefaultSqlInjectorPlus extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo){
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        methodList.add(new InsertList(x -> x.getField().getDeclaredAnnotation(IgnoreInsert.class) == null && !x.isLogicDelete()));
        methodList.add(new UpdateByIdWithNull(x -> x.getField().getDeclaredAnnotation(IgnoreUpdate.class) == null));
        methodList.add(new UpdateBatchById(x -> x.getField().getDeclaredAnnotation(IgnoreUpdate.class) == null));
        methodList.add(new UpdateBatchByIdWithNull(x -> x.getField().getDeclaredAnnotation(IgnoreUpdate.class) == null));
        methodList.add(new JoinSelectList());
        methodList.add(new JoinSelectDeletedList());
        methodList.add(new JoinUpdate());
        methodList.add(new JoinSelectCount());
        methodList.add(new JoinSelectPage());
        methodList.add(new JoinSelectMaps());
        methodList.add(new PhysicalDelete());
        methodList.add(new PhysicalDeleteById());
        return methodList;
    }
}