/*
 * Copyright (c) 2013-2026 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.hutool.v7.core.convert;

import cn.hutool.v7.core.reflect.TypeReference;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 测试使用 TypeReference 注册自定义转换器
 *
 * @author rongx563647
 */
class IssueIJ879QTypeReferenceTest {

	@Test
	void registerConverterWithTypeReferenceTest() {
		final Type listStringType = new TypeReference<List<String>>() {}.getType();

		final CompositeConverter converter = CompositeConverter.getInstance();
		converter.register(listStringType, new ListStringConverter());

		final String input = "a,b,c";
		@SuppressWarnings("unchecked")
		final List<String> result = (List<String>) converter.convert(listStringType, input);

		assertNotNull(result);
		assertEquals(Arrays.asList("A", "B", "C"), result);
	}

	@Test
	void registerConverterWithTypeReferenceInstanceTest() {
		final CompositeConverter converter = CompositeConverter.getInstance();

		converter.register(new TypeReference<List<Integer>>() {}, new ListIntegerConverter());

		final String input = "1,2,3";
		@SuppressWarnings("unchecked")
		final List<Integer> result = (List<Integer>) converter.convert(new TypeReference<List<Integer>>() {}, input);

		assertNotNull(result);
		assertEquals(Arrays.asList(10, 20, 30), result);
	}

	public static class ListStringConverter implements Converter {
		@Override
		public Object convert(final Type targetType, final Object value) throws ConvertException {
			final String str = value.toString();
			final String[] parts = str.split(",");
			return Arrays.stream(parts).map(String::toUpperCase).toList();
		}
	}

	public static class ListIntegerConverter implements Converter {
		@Override
		public Object convert(final Type targetType, final Object value) throws ConvertException {
			final String str = value.toString();
			final String[] parts = str.split(",");
			return Arrays.stream(parts).mapToInt(Integer::parseInt).map(i -> i * 10).boxed().toList();
		}
	}
}
