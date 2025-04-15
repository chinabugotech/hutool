package cn.hutool.v7.core.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueIBP6T1Test {
	@Test
	void isValidCard10Test(){
		Assertions.assertTrue(IdcardUtil.isValidCard10("1608214(1)"));
		Assertions.assertTrue(IdcardUtil.isValidCard10("1608214（1）"));
	}
}
