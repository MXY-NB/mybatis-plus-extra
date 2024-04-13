package com.qingyu.mo.typehandler;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * <p>
 * fastjson 序列化枚举
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
@Slf4j
public class FastjsonEnumDeCodec implements ObjectDeserializer {

	@Override
	public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
		return likeValueOf((Class<?>) type, parser.lexer);
	}

	@Override
	public int getFastMatchToken() {
		return JSONToken.LITERAL_INT;
	}

	public <E> E likeValueOf(Class<?> enumClass, JSONLexer lexer) {
		final Field[] fields = ReflectUtil.getFields(enumClass);
		final Enum<?>[] enums = (Enum<?>[]) enumClass.getEnumConstants();
		final int token = lexer.token();
		String fieldName;
		for (Field field : fields) {
			fieldName = field.getName();
			if (field.getType().isEnum() || "ENUM$VALUES".equals(fieldName) || "ordinal".equals(fieldName)) {
				// 跳过一些特殊字段
				continue;
			}
			for (Enum<?> enumObj : enums) {
				if (token == JSONToken.LITERAL_INT && ObjectUtil.equal(lexer.intValue(), ReflectUtil.getFieldValue(enumObj, field))) {
					return (E) enumObj;
				}
				if (token == JSONToken.LITERAL_STRING && ObjectUtil.equal(lexer.stringVal(), ReflectUtil.getFieldValue(enumObj, field))) {
					return (E) enumObj;
				}
			}
		}
		return null;
	}
}