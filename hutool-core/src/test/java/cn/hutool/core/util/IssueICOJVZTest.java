package cn.hutool.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueICOJVZTest {
	@Test
	void toUnderlineTest(){
		String field = "PAGE_NAME";
		field = StrUtil.toUnderlineCase(field).toUpperCase();
		Assertions.assertEquals("PAGE_NAME", field);
	}
}
