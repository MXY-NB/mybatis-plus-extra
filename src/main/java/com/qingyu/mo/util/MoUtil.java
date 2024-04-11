package com.qingyu.mo.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.func.Func0;
import cn.hutool.core.lang.func.VoidFunc0;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cronutils.model.Cron;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import com.fasterxml.jackson.annotation.JsonValue;
import com.qingyu.mo.annotaion.Min;
import com.qingyu.mo.annotaion.NotEmpty;
import com.qingyu.mo.annotaion.NotNull;
import com.qingyu.mo.constant.Constant;
import com.qingyu.mo.entity.BaseEntity;
import com.qingyu.mo.exception.HandlerException;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.cronutils.model.CronType.QUARTZ;

/**
 * <p>
 * 工具类
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
@Slf4j
@SuppressWarnings("all")
public class MoUtil {

    private MoUtil(){}

    private static final String MSG_1 = "对应参数为空！";

    private static final CronDefinition CRON_DEFINITION = CronDefinitionBuilder.instanceDefinitionFor(QUARTZ);

    public static final String NORM_MINUTE_PATTERN = "HH:mm";

    public static final DateTimeFormatter NORM_MINUTE_FORMATTER = DatePattern.createFormatter(NORM_MINUTE_PATTERN);

    public static <T extends BaseEntity> List<Map<String, Object>> getDataListMap(List<T> list, Class<T> clazz) {
        List<Field> fields = Arrays.stream(ReflectUtil.getFields(clazz)).filter(i -> {
            if ("serialVersionUID".equals(i.getName())) {
                return false;
            }
            TableField annotation = i.getAnnotation(TableField.class);
            if (annotation == null) {
                return true;
            } else {
                return annotation.exist();
            }
        }).collect(Collectors.toList());
        List<Map<String, Object>> map = new ArrayList<>();
        for (T t : list) {
            Map<String, Object> dataMap = new HashMap<>(16);
            for (Field field : fields) {
                Object fieldValue = ReflectUtil.getFieldValue(t, field);
                if (field.getType().isEnum()) {
                    Field[] ss = field.getType().getDeclaredFields();
                    List<Field> collect = Arrays.stream(ss).filter(i -> i.getDeclaredAnnotation(JsonValue.class) != null).collect(Collectors.toList());
                    if (CollUtil.isNotEmpty(collect)) {
                        Field value = collect.get(0);
                        if (fieldValue == null) {
                            dataMap.put(field.getName(), null);
                        } else {
                            dataMap.put(field.getName(), ReflectUtil.getFieldValue(fieldValue, value));
                        }
                    } else {
                        dataMap.put(field.getName(), fieldValue);
                    }
                } else {
                    dataMap.put(field.getName(), fieldValue);
                }
            }
            map.add(dataMap);
        }
        return map;
    }

    /**
     * 获取最后一次执行时间
     * @param expression cron表达式
     * @return LocalDate
     */
    public static LocalDate lastExecution(String expression) {
        CronParser parser = new CronParser(CRON_DEFINITION);
        Cron quartzCron = parser.parse(expression);
        ZonedDateTime nowTime = ZonedDateTime.now();
        ExecutionTime executionTime = ExecutionTime.forCron(quartzCron);
        Optional<ZonedDateTime> zonedDateTimeOptional = executionTime.lastExecution(nowTime);
        if (zonedDateTimeOptional.isPresent()) {
            ZonedDateTime zonedDateTime = zonedDateTimeOptional.get();
            return zonedDateTime.toLocalDate();
        }
        return null;
    }

    /**
     * 年月日格式 yyyy-MM-dd
     * @param text 日期时间字符串
     * @return LocalDate
     */
    public static LocalDate parseDate(CharSequence text) {
        return LocalDateTimeUtil.parseDate(text, DatePattern.NORM_DATE_FORMATTER);
    }

    /**
     * 年月格式 yyyy-MM
     * @param text 日期时间字符串
     * @return LocalDate
     */
    public static LocalDate parseMonth(CharSequence text) {
        return LocalDateTimeUtil.parseDate(text, DatePattern.NORM_MONTH_FORMATTER);
    }

    /**
     * 年月格式 yyyy-MM
     * @param text 日期时间字符串
     * @return YearMonth
     */
    public static YearMonth parsePurMonth(CharSequence text) {
        return YearMonth.parse(text, DatePattern.NORM_MONTH_FORMATTER);
    }

    /**
     * 格式化日期时间 yyyy-MM-dd
     * @param localDate 日期时间
     * @return String
     */
    public static String formatDate(LocalDate localDate) {
        return LocalDateTimeUtil.format(localDate, DatePattern.NORM_DATE_FORMATTER);
    }

    /**
     * 格式化日期时间 yyyy-MM
     * @param localDate 日期时间
     * @return String
     */
    public static String formatMonth(LocalDate localDate) {
        return LocalDateTimeUtil.format(localDate, DatePattern.NORM_MONTH_FORMATTER);
    }

    /**
     * 格式化日期时间 yyyy-MM
     * @param yearMonth 日期时间
     * @return String
     */
    public static String formatMonth(YearMonth yearMonth) {
        return yearMonth.format(DatePattern.NORM_MONTH_FORMATTER);
    }

    /**
     * 格式化日期时间 HH:mm:ss
     * @param localTime 日期时间
     * @return String
     */
    public static String formatTime(LocalTime localTime) {
        return localTime.format(DatePattern.NORM_TIME_FORMATTER);
    }

    /**
     * 格式化日期时间 HH:mm
     * @param localDateTime 日期时间
     * @return String
     */
    public static String formatMinute(LocalDateTime localDateTime) {
        return formatMinute(localDateTime.toLocalTime());
    }

    /**
     * 格式化日期时间 HH:mm
     * @param localTime 日期时间
     * @return String
     */
    public static String formatMinute(LocalTime localTime) {
        return localTime.format(NORM_MINUTE_FORMATTER);
    }

    public static <T extends BaseEntity> boolean isEmpty(T t) {
        return t == null || t.getId() == null;
    }

    public static <T extends BaseEntity> boolean isNotEmpty(T t) {
        return !isEmpty(t);
    }

    public static <T> boolean isEmpty(T t) {
        if (t == null) {
            return true;
        } else if (t instanceof String) {
            return CharSequenceUtil.isEmpty((String) t);
        } else if (ObjectUtil.isBasicType(t)) {
            return ObjectUtil.isEmpty(t);
        }
        return false;
    }

    public static <T> boolean isNotEmpty(T t) {
        return !isEmpty(t);
    }

    @SafeVarargs
    public static <T extends BaseEntity> void checkIsEmpty(T t, SFunction<T, ?>... sFunctions) {
        for (SFunction<T, ?> sFunction : sFunctions) {
            LambdaMeta meta = LambdaUtils.extract(sFunction);
            Object value = ReflectUtil.invoke(t, meta.getImplMethodName());
            if (value instanceof String) {
                if (CharSequenceUtil.isEmpty((String) value)) {
                    String fieldName = PropertyNamer.methodToProperty(meta.getImplMethodName());
                    throw new HandlerException(fieldName + MSG_1);
                }
            } else if (value instanceof Collection) {
                if (CollUtil.isEmpty((Collection<?>) value)) {
                    String fieldName = PropertyNamer.methodToProperty(meta.getImplMethodName());
                    throw new HandlerException(fieldName + MSG_1);
                }
            } else if (value instanceof Map) {
                if (MapUtil.isEmpty((Map<?,?>) value)) {
                    String fieldName = PropertyNamer.methodToProperty(meta.getImplMethodName());
                    throw new HandlerException(fieldName + MSG_1);
                }
            } else {
                if (ObjectUtil.isEmpty(value)) {
                    String fieldName = PropertyNamer.methodToProperty(meta.getImplMethodName());
                    throw new HandlerException(fieldName + MSG_1);
                }
            }
        }
    }

    public static <T extends BaseEntity> void checkId(T t) {
        if (ObjectUtil.isEmpty(t.getId())) {
            throw new HandlerException("id为空！");
        }
    }

    public static <T extends BaseEntity> void checkIds(T t) {
        if (CollUtil.isEmpty(t.getIds())) {
            throw new HandlerException("ids为空！");
        }
    }

    public static <T> T getToDefault(T v, T defaultV) {
        if (v instanceof String) {
            return CharSequenceUtil.isEmpty((String)v) ? defaultV : v;
        } else {
            return v == null ? defaultV : v;
        }
    }

    public static <K, V> V getOrPut(Map<K, V> map, K k, Func0<V> action) {
        if (map.containsKey(k)) {
            return map.get(k);
        } else {
            V v = tThrowE(action);
            map.put(k, v);
            return v;
        }
    }

    public static <K, V extends BaseEntity> V getOrPut(Map<K, V> map, K k, Func0<V> action, String message) {
        if (map.containsKey(k)) {
            return map.get(k);
        } else {
            V v = tThrowE(action);
            tThrowE(isEmpty(v), message);
            map.put(k, v);
            return v;
        }
    }

    public static <T> T getIfTrue(boolean codition, T trueValue, T falseValue) {
        return codition ? trueValue : falseValue;
    }

    public static BigDecimal checkLtZero(BigDecimal amount) {
        return amount == null || amount.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : amount;
    }

    public static BigDecimal checkLeZeroToOne(BigDecimal amount) {
        return amount == null || isEqZero(amount) ? BigDecimal.ONE : amount;
    }

    public static Integer checkLtZero(Integer amount) {
        return amount == null || amount < 0 ? 0 : amount;
    }

    public static boolean isGtZero(BigDecimal amount) {
        return isGt(amount, BigDecimal.ZERO);
    }

    public static boolean isLtZero(BigDecimal amount) {
        return isLt(amount, BigDecimal.ZERO);
    }

    public static boolean isLeZero(BigDecimal amount) {
        return isLe(amount, BigDecimal.ZERO);
    }

    public static boolean isGeZero(BigDecimal amount) {
        return isGe(amount, BigDecimal.ZERO);
    }

    public static boolean isEqZero(BigDecimal amount) {
        return isEq(amount, BigDecimal.ZERO);
    }

    public static boolean isNe(BigDecimal amount, BigDecimal bigDecimal) {
        return amount != null && bigDecimal != null && amount.compareTo(bigDecimal) != 0;
    }

    public static boolean isGt(BigDecimal amount, BigDecimal bigDecimal) {
        return amount != null && bigDecimal != null && amount.compareTo(bigDecimal) > 0;
    }

    public static boolean isGe(BigDecimal amount, BigDecimal bigDecimal) {
        return amount != null && bigDecimal != null && amount.compareTo(bigDecimal) >= 0;
    }

    public static boolean isLt(BigDecimal amount, BigDecimal bigDecimal) {
        return amount != null && bigDecimal != null && amount.compareTo(bigDecimal) < 0;
    }

    public static boolean isLe(BigDecimal amount, BigDecimal bigDecimal) {
        return amount != null && bigDecimal != null && amount.compareTo(bigDecimal) <= 0;
    }

    public static boolean isNotEq(BigDecimal amount, BigDecimal bigDecimal) {
        return !isEq(amount, bigDecimal);
    }

    public static boolean isEq(BigDecimal amount, BigDecimal bigDecimal) {
        return amount != null && bigDecimal != null && amount.compareTo(bigDecimal) == 0;
    }

    public static boolean isEq(Object val, Object val2) {
        return val != null && val.equals(val2);
    }

    public static <R> R tThrowE(Func0<R> action) {
        return tThrowE(action, Constant.ERROR);
    }

    public static <R> R tThrowE(Func0<R> action, String message) {
        try {
            return action.call();
        } catch (HandlerException e) {
            throw new HandlerException(e.getMessage());
        } catch (Exception e) {
            log.error(ExceptionUtil.stacktraceToString(e));
            throw new HandlerException(message);
        }
    }

    public static void tThrowE(boolean codition, VoidFunc0 action) {
        if (codition) {
            try {
                action.call();
            } catch (HandlerException e) {
                throw new HandlerException(e.getMessage());
            } catch (Exception e) {
                log.error(ExceptionUtil.stacktraceToString(e));
                throw new HandlerException(Constant.ERROR);
            }
        }
    }

    public static void tThrowE(boolean codition, String message) {
        if (codition) {
            throw new HandlerException(message);
        }
    }

    public static void tThrowE(VoidFunc0 action) {
        tThrowE(action, Constant.ERROR);
    }

    public static void tThrowE(VoidFunc0 action, String message) {
        try {
            action.call();
        } catch (Exception e) {
            throw new HandlerException(message);
        }
    }

    @SafeVarargs
    public static <T extends Enum<T>> boolean notInEnums(T t, T... enums) {
        return notInValues(t, enums);
    }

    @SafeVarargs
    public static <T> boolean notInValues(T t, T... values) {
        return ArrayUtil.isNotEmpty(values) && !inValues(t, values);
    }

    @SafeVarargs
    public static <T extends Enum<T>> boolean inEnums(T t, T... enums) {
        return inValues(t, enums);
    }

    @SafeVarargs
    public static <T> boolean inValues(T t, T... values) {
        return ArrayUtil.isNotEmpty(values) && inValues(t, Arrays.stream(values).collect(Collectors.toList()));
    }

    public static <T extends Enum<T>> boolean notInEnums(T t, Collection<T> enums) {
        return notInValues(t, enums);
    }

    public static <T> boolean notInValues(T t, Collection<T> values) {
        return !inValues(t, values);
    }

    public static <T extends Enum<T>> boolean inEnums(T t, Collection<T> enums) {
        return inValues(t, enums);
    }

    public static <T> boolean inValues(T t, Collection<T> values) {
        return t != null && CollUtil.isNotEmpty(values) && values.contains(t);
    }

    public static <T extends BaseEntity> T nullToException(T t, Long id) {
        return nullToException(t, id, Constant.USER_ERROR);
    }

    public static <T extends BaseEntity> T nullToException(T t, Long id, String errorMsg) {
        tThrowE(isEmpty(t), ()->{
            log.error("未找到对应id：" + id + "的数据！");
            throw new HandlerException(errorMsg);
        });
        return t;
    }

    public static boolean nullToException(boolean exist, Long id) {
        return nullToException(exist, id, Constant.USER_ERROR);
    }

    public static boolean nullToException(boolean exist, Long id, String errorMsg) {
        tThrowE(!exist, ()->{
            log.error("未找到对应id：" + id + "的数据！");
            throw new HandlerException(errorMsg);
        });
        return true;
    }

    @SafeVarargs
    public static <S extends IService<T>, T extends BaseEntity> T getByIdThrowException(S s, T t, SFunction<T, ?>... sFunctions) {
        return getByIdThrowException(s, t.getId(), sFunctions);
    }

    @SafeVarargs
    public static <S extends IService<T>, T extends BaseEntity> T getByIdThrowException(S s, T t, String errorMsg, SFunction<T, ?>... sFunctions) {
        return getByIdThrowException(s, t.getId(), errorMsg, sFunctions);
    }

    @SafeVarargs
    public static <S extends IService<T>, T extends BaseEntity> T getByIdThrowException(S s, Long id, SFunction<T, ?>... sFunctions) {
        return nullToException(getById(s, id, sFunctions), id);
    }

    @SafeVarargs
    public static <S extends IService<T>, T extends BaseEntity> T getByIdThrowException(S s, Long id, String errorMsg, SFunction<T, ?>... sFunctions) {
        return nullToException(getById(s, id, sFunctions), id, errorMsg);
    }

    @SafeVarargs
    public static <S extends IService<T>, T extends BaseEntity> List<T> getByIds(S s, T t, SFunction<T, ?>... sFunctions) {
        return getByIds(s, t.getIds(), sFunctions);
    }

    @SafeVarargs
    public static <S extends IService<T>, T extends BaseEntity> T getById(S s, T t, SFunction<T, ?>... sFunctions) {
        return getById(s, t.getId(), sFunctions);
    }

    @SafeVarargs
    public static <S extends IService<T>, T extends BaseEntity> List<T> getByIds(S s, Collection<Long> ids, SFunction<T, ?>... sFunctions) {
        if (ArrayUtil.isNotEmpty(sFunctions)) {
            List<SFunction<T, ?>> collect = Arrays.stream(sFunctions).collect(Collectors.toList());
            collect.add(T::getId);
            return s.list(Wrappers.<T>lambdaQuery().select(collect).in(T::getId, ids));
        }
        return s.listByIds(ids);
    }

    @SafeVarargs
    public static <S extends IService<T>, T extends BaseEntity> T getById(S s, Long id, SFunction<T, ?>... sFunctions) {
        if (ArrayUtil.isNotEmpty(sFunctions)) {
            List<SFunction<T, ?>> collect = Arrays.stream(sFunctions).collect(Collectors.toList());
            collect.add(T::getId);
            return s.getOne(Wrappers.<T>lambdaQuery().select(collect).eq(T::getId, id));
        }
        return s.getById(id);
    }

    @SafeVarargs
    public static <M extends BaseMapper<T>, T extends BaseEntity> T getByIdThrowException(M m, T t, SFunction<T, ?>... sFunctions) {
        return getByIdThrowException(m, t.getId(), sFunctions);
    }

    @SafeVarargs
    public static <M extends BaseMapper<T>, T extends BaseEntity> T getByIdThrowException(M m, T t, String errorMsg, SFunction<T, ?>... sFunctions) {
        return getByIdThrowException(m, t.getId(), errorMsg, sFunctions);
    }

    @SafeVarargs
    public static <M extends BaseMapper<T>, T extends BaseEntity> T getByIdThrowException(M m, Long id, SFunction<T, ?>... sFunctions) {
        return nullToException(getById(m, id, sFunctions), id);
    }

    public static <M extends BaseMapper<T>, T extends BaseEntity> boolean existThrowException(M m, Class<T> tClass, Long id) {
        return nullToException(m.exists(Wrappers.<T>lambdaQuery().setEntityClass(tClass).eq(T::getId, id)), id);
    }

    @SafeVarargs
    public static <M extends BaseMapper<T>, T extends BaseEntity> T getByIdThrowException(M m, Long id, String errorMsg, SFunction<T, ?>... sFunctions) {
        return nullToException(getById(m, id, sFunctions), id, errorMsg);
    }

    @SafeVarargs
    public static <M extends BaseMapper<T>, T extends BaseEntity> List<T> getByIds(M m, T t, SFunction<T, ?>... sFunctions) {
        return getByIds(m, t.getIds(), sFunctions);
    }

    @SafeVarargs
    public static <M extends BaseMapper<T>, T extends BaseEntity> T getById(M m, T t, SFunction<T, ?>... sFunctions) {
        return getById(m, t.getId(), sFunctions);
    }

    @SafeVarargs
    public static <M extends BaseMapper<T>, T extends BaseEntity> List<T> getByIds(M m, Collection<Long> ids, SFunction<T, ?>... sFunctions) {
        if (ArrayUtil.isNotEmpty(sFunctions)) {
            List<SFunction<T, ?>> collect = Arrays.stream(sFunctions).collect(Collectors.toList());
            collect.add(T::getId);
            return m.selectList(Wrappers.<T>lambdaQuery().select(collect).in(T::getId, ids));
        }
        return m.selectBatchIds(ids);
    }

    @SafeVarargs
    public static <M extends BaseMapper<T>, T extends BaseEntity> T getById(M m, Long id, SFunction<T, ?>... sFunctions) {
        if (ArrayUtil.isNotEmpty(sFunctions)) {
            List<SFunction<T, ?>> collect = Arrays.stream(sFunctions).collect(Collectors.toList());
            collect.add(T::getId);
            return m.selectOne(Wrappers.<T>lambdaQuery().select(collect).eq(T::getId, id));
        }
        return m.selectById(id);
    }

    public static <S extends IService<T>, T extends BaseEntity, R> void updateByIds(S s, T t, SFunction<T, R> sFunction) {
        s.update(Wrappers.<T>lambdaUpdate()
                .in(T::getId, t.getIds())
                .set(sFunction, ReflectUtil.invoke(t, LambdaUtils.extract(sFunction).getImplMethodName()))
        );
    }

    public static <S extends IService<T>, T extends BaseEntity, R, V> void updateByIds(S s, T t, SFunction<T, R> sFunction, V v) {
        s.update(Wrappers.<T>lambdaUpdate()
                .in(T::getId, t.getIds())
                .set(sFunction, v)
        );
    }

    /**
     * 校验审核
     * @param e 状态枚举
     * @param remark 拒绝原因
     * @param enums 枚举
     * @param <T> 泛型
     */
    @SafeVarargs
    public static <T extends Enum<T>> void checkAuditRemark(T e, String remark, T... enums) {
        if (ArrayUtil.isEmpty(enums) || enums.length < Constant.TWO) {
            return;
        }
        if (notInEnums(e, enums)) {
            throw new HandlerException("请选择同意或拒绝！");
        }
        if (e.equals(enums[1]) && CharSequenceUtil.isEmpty(remark)) {
            throw new HandlerException("拒绝请填写原因！");
        }
    }

    /**
     * 自定义校验数据
     * @param checkData 数据
     * @param beanClass 数据class
     * @param <T> 泛型
     */
    public static <T> void checkData(List<T> checkData, Class<T> beanClass) {
        checkData(checkData, beanClass, 0);
    }

    /**
     * 自定义校验数据
     * @param checkData 数据
     * @param beanClass 数据class
     * @param groupId 分组id
     * @param <T> 泛型
     */
    public static <T> void checkData(List<T> checkData, Class<T> beanClass, Integer groupId) {
        if (CollUtil.isEmpty(checkData)) {
            return;
        }
        Assert.notNull(beanClass);
        Field[] fields = ReflectUtil.getFields(beanClass);
        List<String> fieldNames = new ArrayList<>();
        Map<String, NotNull> notNullList = new HashMap<>(16);
        Map<String, NotEmpty> notEmptyList = new HashMap<>(16);
        Map<String, Min> minList = new HashMap<>(16);
        for (Field field : fields) {
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                if (annotation.annotationType().equals(NotNull.class)) {
                    NotNull notNull = (NotNull) annotation;
                    if (notNull.groupId() == groupId) {
                        notNullList.put(field.getName(), notNull);
                        fieldNames.add(field.getName());
                    }
                }
                if (annotation.annotationType().equals(NotEmpty.class)) {
                    NotEmpty notEmpty = (NotEmpty) annotation;
                    if (notEmpty.groupId() == groupId) {
                        notEmptyList.put(field.getName(), notEmpty);
                        fieldNames.add(field.getName());
                    }
                }
                if (annotation.annotationType().equals(Min.class) && field.getType().equals(BigDecimal.class)) {
                    Min min = (Min) annotation;
                    if (min.groupId() == groupId) {
                        minList.put(field.getName(), min);
                        fieldNames.add(field.getName());
                    }
                }
            }
        }
        if (CollUtil.isNotEmpty(fieldNames)) {
            for (T data : checkData) {
                Map<String, Object> fieldMap = new HashMap<>(16);
                for (String fieldName : fieldNames) {
                    Object fieldValue = ReflectUtil.getFieldValue(data, fieldName);
                    fieldMap.put(fieldName, fieldValue);
                }
                notNullList.forEach((fieldName, notNull)->{
                    if (fieldMap.containsKey(fieldName) && ObjectUtil.isEmpty(fieldMap.get(fieldName))) {
                        throw new HandlerException(notNull.value());
                    }
                });
                notEmptyList.forEach((fieldName, notEmpty)->{
                    if (fieldMap.containsKey(fieldName)) {
                        if (fieldMap.get(fieldName) instanceof Map && MapUtil.isEmpty((Map<?, ?>) fieldMap.get(fieldName))) {
                            throw new HandlerException(notEmpty.value());
                        }
                        if (fieldMap.get(fieldName) instanceof Collection && CollUtil.isEmpty((Collection<?>) fieldMap.get(fieldName))) {
                            throw new HandlerException(notEmpty.value());
                        }
                    }
                });
                minList.forEach((fieldName, min)->{
                    if (fieldMap.containsKey(fieldName)) {
                        if (min.isEquals()) {
                            if (ObjectUtil.isNotEmpty(fieldMap.get(fieldName)) && NumberUtil.isLess((BigDecimal) fieldMap.get(fieldName), BigDecimal.valueOf(min.value()))) {
                                throw new HandlerException(min.message());
                            }
                        } else {
                            if (ObjectUtil.isNotEmpty(fieldMap.get(fieldName)) && NumberUtil.isLessOrEqual((BigDecimal) fieldMap.get(fieldName), BigDecimal.valueOf(min.value()))) {
                                throw new HandlerException(min.message());
                            }
                        }
                    }
                });
            }
        }
    }
}