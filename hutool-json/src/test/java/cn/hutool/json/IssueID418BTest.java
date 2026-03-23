package cn.hutool.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueID418BTest {

	@Test
	void booleanToJsonTest() {
		Boolean dd = true;
		String jsonStr = JSONUtil.toJsonStr(dd);
		Assertions.assertEquals("true", jsonStr);
	}
}
