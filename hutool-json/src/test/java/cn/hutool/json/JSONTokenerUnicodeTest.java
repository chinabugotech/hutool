package cn.hutool.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unicode转义转换测试。
 */
public class JSONTokenerUnicodeTest {

	@Test
	public void parseUnicodeTest() {
		final JSONTokener tokener = new JSONTokener("", JSONConfig.create());
		Assertions.assertEquals(0x4e2d, tokener.parseUnicode("4e2d"));
		Assertions.assertEquals(0x00af, tokener.parseUnicode("00AF"));
	}

	@Test
	public void conversionErrorTest() {
		final JSONTokener tokener = new JSONTokener("", JSONConfig.create());
		final JSONException exception = Assertions.assertThrows(JSONException.class,
				() -> tokener.parseUnicode("tree"));
		Assertions.assertEquals("Illegal Unicode escape. at 0 [character 1 line 1]", exception.getMessage());
	}

	@Test
	public void validUnicodeTest() {
		final String json = "{\"\\u006eame\":\"\\u4e2d\\u6587\",\"face\":\"\\uD83D\\uDE00\"}";
		final JSONObject object = JSONUtil.parseObj(json);
		Assertions.assertEquals("中文", object.getStr("name"));
		Assertions.assertEquals("😀", object.getStr("face"));
		Assertions.assertEquals("中文", JSONUtil.toBean(json, UnicodeBean.class).getName());
	}

	public static class UnicodeBean {
		private String name;

		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
