package cn.hutool.core.util;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.exceptions.UtilException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VersionUtilTest {

	@Test
	void isGreaterThan() {
		String currentVersion = " 1.0.2";
		assertTrue(VersionUtil.isGreaterThan(currentVersion, "1.0.1"));
		assertTrue(VersionUtil.isGreaterThan(currentVersion, "1"));
		assertFalse(VersionUtil.isGreaterThan(currentVersion, "1.1"));
	}

	@Test
	void isGreaterThanOrEqual() {
		String currentVersion = "1.0.2 ";
		assertTrue(VersionUtil.isGreaterThanOrEqual(currentVersion, "1.0.1"));
		assertTrue(VersionUtil.isGreaterThanOrEqual(currentVersion, "1.0.2"));
		assertFalse(VersionUtil.isGreaterThanOrEqual(currentVersion, "1.1"));
	}

	@Test
	void isLessThan() {
		String currentVersion = "1.0.2";
		assertTrue(VersionUtil.isLessThan(currentVersion, "1.0.3"));
		assertFalse(VersionUtil.isLessThan(currentVersion, "1"));
		assertTrue(VersionUtil.isLessThan(currentVersion, "1.1"));
		assertFalse(VersionUtil.isLessThan(currentVersion, "1.0.2"));
	}

	@Test
	void isLessThanOrEqual() {
		String currentVersion = "1.0.2";
		assertTrue(VersionUtil.isLessThanOrEqual(currentVersion, "1.0.2"));
		assertFalse(VersionUtil.isLessThanOrEqual(currentVersion, "1.0.1"));
		assertTrue(VersionUtil.isLessThanOrEqual(currentVersion, "1.1"));
	}

	@Test
	void matchEl() {
		String currentVersion = "1.0.2";
		assertTrue(VersionUtil.matchEl(currentVersion, "1.0.1;1.0.2"));
		assertFalse(VersionUtil.matchEl(currentVersion, "1.0.1;1.0.3"));
		assertTrue(VersionUtil.matchEl(currentVersion, "1.0.9;1.0.1-1.0.2"));
		assertTrue(VersionUtil.matchEl(currentVersion, "1.0.9;1.0.1-1.0.3"));

		assertTrue(VersionUtil.matchEl(currentVersion, "1.0.9,1.0.1-1.0.3", ","));
	}

	@Test
	void matchEl_Exception_whenVersionDelimiterIllegal() {
		List<String> illegalDelimiters = ListUtil.of("-", ">", ">=", "<", "<=", "≥", "≤", null, "", " ");

		for (String illegalDelimiter : illegalDelimiters) {
			assertThrows(UtilException.class, () -> {
				String currentVersion = "1.0.2";
				VersionUtil.matchEl(currentVersion, "1.0.1;1.0.2", illegalDelimiter);
			});
		}
	}

	@Test
	void anyMatch() {
		String currentVersion = "1.0.2";
		assertTrue(VersionUtil.anyMatch(currentVersion, ListUtil.of("1.0.1", "1.0.3", "1.0.2")));
		assertTrue(VersionUtil.anyMatch(currentVersion, "1.0.1", "1.0.2"));
	}

	@Test
	void testMatchEl() {
	}

	/**
	 * 测试版本范围表达式边界情况
	 * 1. 左边界为空的情况: "-1.0.3" 应该匹配小于等于1.0.3的版本
	 * 2. 右边界为空的情况: "1.0.0-" 应该匹配大于等于1.0.0的版本
	 * 3. 双边界为空的情况: "-" 应该匹配所有版本
	 * 验证 VersionUtil.matchEl 方法对边界值的正确处理
	 */
	@Test
	void matchEl_rangeBoundaryCases() {
		String currentVersion = "1.0.2";

		// 测试左边界为空的情况: "-1.0.3" 应该匹配小于等于1.0.3的版本
		assertTrue(VersionUtil.matchEl(currentVersion, "-1.0.3"));
		assertTrue(VersionUtil.matchEl(currentVersion, "-1.0.2"));
		assertFalse(VersionUtil.matchEl(currentVersion, "-1.0.0"));

		// 测试右边界为空的情况: "1.0.0-" 应该匹配大于等于1.0.0的版本
		assertTrue(VersionUtil.matchEl(currentVersion, "1.0.0-"));
		assertTrue(VersionUtil.matchEl(currentVersion, "1.0.2-"));
		assertFalse(VersionUtil.matchEl(currentVersion, "1.0.3-"));

		// 测试双边为空的情况: "-" 应该匹配所有版本
		assertTrue(VersionUtil.matchEl(currentVersion, "-"));
		assertTrue(VersionUtil.matchEl("0.0.1", "-"));
		assertTrue(VersionUtil.matchEl("999.999.999", "-"));
	}

}
