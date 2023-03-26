package com.iv.ersr.mybatisplus.interceptor;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.iv.ersr.mybatisplus.exception.Exceptions;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.InterceptorChain;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * join拦截器配置类，用来重新排列拦截器顺序，保证类型拦截器先执行
 * </p>
 *
 * @author moxiaoyu
 * @since 2023-03-26
 */
@SuppressWarnings("all")
public class MybatisPlusJoinInterceptor implements InitializingBean {

    @Resource
    private List<SqlSessionFactory> sqlSessionFactoryList;

    private final JoinInterceptor joinInterceptor;

    public MybatisPlusJoinInterceptor (JoinInterceptor joinInterceptor) {
        this.joinInterceptor = joinInterceptor;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (CollectionUtils.isNotEmpty(sqlSessionFactoryList) && Objects.nonNull(joinInterceptor)) {
            try {
                for (SqlSessionFactory factory : sqlSessionFactoryList) {
                    /*
                    有人可能不明白这一系列反射操作是为啥？
                    直接factory.getConfiguration().getInterceptors()不就可以了吗？
                    因为 他返回的是Collections.unmodifiableList(interceptors) 通过这个集合转换了
                    这个集合则是一个不可以修改的集合，但是我们要调整顺序，所以说呢，直接用反射取修改他的实体属性
                     */
                    Field interceptorChain = Configuration.class.getDeclaredField("interceptorChain");
                    interceptorChain.setAccessible(true);
                    InterceptorChain chain = (InterceptorChain) interceptorChain.get(factory.getConfiguration());
                    Field oldInterceptors = InterceptorChain.class.getDeclaredField("interceptors");
                    oldInterceptors.setAccessible(true);
                    List<Interceptor> list = (List<Interceptor>) oldInterceptors.get(chain);
                    if (CollectionUtils.isNotEmpty(list)) {
                        if (list.get(list.size() - 1) != joinInterceptor) {
                            list.removeIf(i -> i == joinInterceptor);
                            list.add(joinInterceptor);
                        }
                    }
                    list.add(joinInterceptor);
                }
            } catch (Exception e) {
                throw Exceptions.t("注入mybatis-plus-join 拦截器失败！", e);
            }
        }
    }
}
