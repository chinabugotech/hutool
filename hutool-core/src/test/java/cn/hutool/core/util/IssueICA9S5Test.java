package cn.hutool.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class IssueICA9S5Test {
	@Test
	public void test() {
		String a = "ENUM{\\ndisable ~ 0\\nenable ~ 1\\n}";
		final List<String> split = StrUtil.split(a, "\\n");
		Assertions.assertEquals(4, split.size());
	}
}
