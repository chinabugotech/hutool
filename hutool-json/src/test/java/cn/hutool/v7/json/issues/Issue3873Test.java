package cn.hutool.v7.json.issues;

import lombok.Data;
import cn.hutool.v7.core.array.ArrayUtil;
import cn.hutool.v7.core.reflect.TypeReference;
import cn.hutool.v7.json.JSONConfig;
import cn.hutool.v7.json.JSONUtil;
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
