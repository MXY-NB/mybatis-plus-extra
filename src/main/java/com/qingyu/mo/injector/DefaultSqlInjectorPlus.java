package com.qingyu.mo.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.qingyu.mo.annotaion.IgnoreInsert;
import com.qingyu.mo.annotaion.IgnoreUpdate;
import com.qingyu.mo.injector.methods.*;

import java.util.List;

/**
 * <p>
 * 自定义SQL注入器
 * </p>
 *
 * @author qingyu-mo
 * @since 2023-12-19
 */
public class DefaultSqlInjectorPlus extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo){
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        methodList.add(new InsertList(x -> x.getField().getDeclaredAnnotation(IgnoreInsert.class) == null && !x.isLogicDelete()));
        methodList.add(new UpdateBatchByIdWithNull(x -> x.getField().getDeclaredAnnotation(IgnoreUpdate.class) == null));
        methodList.add(new UpdateBatchById(x -> x.getField().getDeclaredAnnotation(IgnoreUpdate.class) == null));
        methodList.add(new JoinSelectList());
        methodList.add(new JoinSelectDeletedList());
        methodList.add(new JoinSelectCount());
        methodList.add(new JoinSelectPage());
        methodList.add(new JoinSelectMaps());
        methodList.add(new PhysicalDelete());
        return methodList;
    }
}