package cn.hutool.json;

import lombok.Data;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Pr1431Test {
	@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
	@Test
	void filterTest() {
		UserC user = new UserC();
		user.setId(1);
		user.setName("张三");
		user.setProp("123456");

		final JSONObject entries = new JSONObject(user, JSONConfig.create(), pair -> !"prop".equals(pair.getKey()));
		assertEquals(2, entries.size());
		assertTrue(entries.containsKey("id"));
		assertTrue(entries.containsKey("name"));
	}

	@Data
	static class UserC {
		private int id;
		private String name;
		private String prop;
	}
}
