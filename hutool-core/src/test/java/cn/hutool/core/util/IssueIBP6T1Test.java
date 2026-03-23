package cn.hutool.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueIBP6T1Test {
	@SuppressWarnings("DataFlowIssue")
	@Test
	void isValidCard10Test(){
		Assertions.assertEquals("true", IdcardUtil.isValidCard10("1608214(1)")[2]);
		Assertions.assertEquals("true", IdcardUtil.isValidCard10("1608214（1）")[2]);
	}
}
