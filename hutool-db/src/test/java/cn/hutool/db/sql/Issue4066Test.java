package cn.hutool.db.sql;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Issue4066Test {
	/**
	 * 基础测试：简单的 ORDER BY 语句
	 */
	@Test
	public void removeOuterOrderByTest1() {
		// 测试基本的ORDER BY移除
		final String sql = "SELECT * FROM users ORDER BY name";
		final String result = SqlUtil.removeOuterOrderBy(sql);

		assertEquals("SELECT * FROM users", result);
	}

	/**
	 * 多字段 ORDER BY 测试：包含多个排序字段的复杂 ORDER BY语句
	 */
	@Test
	public void removeOuterOrderByTest2() {
		// 测试多字段ORDER BY移除
		final String sql = "SELECT id, name, age FROM users WHERE status = 'active' ORDER BY name ASC, age DESC, created_date";
		final String result = SqlUtil.removeOuterOrderBy(sql);

		assertEquals("SELECT id, name, age FROM users WHERE status = 'active'", result);
	}

	/**
	 * 测试不含Order by的语句
	 */
	@Test
	public void removeOuterOrderByTest3() {
		final String sql = "SELECT * FROM users";
		final String result = SqlUtil.removeOuterOrderBy(sql);

		assertEquals("SELECT * FROM users", result);
	}
}
