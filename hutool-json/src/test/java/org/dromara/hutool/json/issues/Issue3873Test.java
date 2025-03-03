package org.dromara.hutool.json.issues;

import lombok.Data;
import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.reflect.TypeReference;
import org.dromara.hutool.json.JSONConfig;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Issue3873Test {
	/**
	 * 类型引用数组泛型丢失
	 */
	@Test
	public void toBeanTest() {
		final String json = "{\"results\":[{\"uid\":\"1\"}],\"offset\":0,\"limit\":20,\"total\":0}";
		final Results<Index> deserialize = JSONUtil.toBean(json, JSONConfig.of(), (new TypeReference<Results<Index>>() {}));

		assertEquals(Results.class, deserialize.getClass());
		assertEquals(ArrayUtil.getArrayType(Index.class), deserialize.results.getClass());
	}

	@Data
	public static class Results<T> {
		public T[] results;
	}

	@Data
	public static class Index {
		public String uid;
	}
}
