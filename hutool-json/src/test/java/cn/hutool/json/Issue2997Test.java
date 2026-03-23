package cn.hutool.json;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Issue2997Test {
	@Test
	public void toBeanTest() {
		// https://github.com/chinabugotech/hutool/issues/2997
		final Object o = JSONUtil.toBean("{}", Object.class);
		assertEquals(JSONObject.class, o.getClass());
	}
}
