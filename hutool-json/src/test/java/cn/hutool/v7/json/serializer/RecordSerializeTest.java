package cn.hutool.v7.json.serializer;

import cn.hutool.v7.json.JSON;
import cn.hutool.v7.json.JSONObject;
import cn.hutool.v7.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RecordSerializeTest {

	public record TestRecord(String name, String address){ }

	@Test
	void recordToJsonTest(){
		final JSON parse = JSONUtil.parse(new TestRecord("Tom", "Beijing"));
		Assertions.assertEquals(JSONObject.class, parse.getClass());
		Assertions.assertEquals("{\"name\":\"Tom\",\"address\":\"Beijing\"}", parse.toString());

		final TestRecord bean = parse.toBean(TestRecord.class);
		Assertions.assertEquals("Tom", bean.name());
		Assertions.assertEquals("Beijing", bean.address());
	}
}
