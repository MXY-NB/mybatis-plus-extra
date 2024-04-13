package com.qingyu.mo.typehandler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * fastjson 序列化枚举
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
@Slf4j
public class FastjsonEnumCodec implements ObjectSerializer {

	@Override
	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
		List<Field> collect = Arrays.stream(object.getClass().getDeclaredFields()).filter(i -> i.getDeclaredAnnotation(JsonValue.class) != null).collect(Collectors.toList());
		if (CollUtil.isNotEmpty(collect)) {
			Field field = collect.get(0);
			serializer.write(ReflectUtil.getFieldValue(object, field));
		} else {
			serializer.write(object);
		}
	}
}