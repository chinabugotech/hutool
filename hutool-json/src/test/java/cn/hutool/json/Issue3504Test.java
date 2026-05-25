package cn.hutool.json;

import lombok.Data;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * https://github.com/chinabugotech/hutool/issues/3504
 */
public class Issue3504Test {

	@Test
	public void test3504() {
		JsonBean jsonBean = new JsonBean();
		jsonBean.setName("test");
		jsonBean.setClasses(new Class[]{String.class});
		String huToolJsonStr = JSONUtil.toJsonStr(jsonBean);
		final JsonBean bean = JSONUtil.toBean(huToolJsonStr, JsonBean.class);
		assertNotNull(bean);
		assertEquals("test", bean.getName());
	}

	@Data
	public static class JsonBean {
		private String name;
		private Class<?>[] classes;
	}
}
