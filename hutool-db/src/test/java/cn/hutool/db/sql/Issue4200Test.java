package cn.hutool.db.sql;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Issue4200Test {

	/**
	 * issues中提及的SQL
	 */
	private final static String TEST_SQL = "select case when  1 in (1) and 2 = any(:number) then 1 else 0 end";


	/**
	 * 基础测试：in子句测试
	 */
	@Test
	public void isInClauseTest0() {
		final String sql = "select case when 1=1 and 2 in ( ";
		assertTrue(SqlUtil.isInClause(sql));
	}

	/**
	 * 基础测试：in子句测试
	 */
	@Test
	public void isInClauseTest1() {
		final String sql = "select case when 1=1 and 2 in (";
		assertTrue(SqlUtil.isInClause(sql));
	}

	/**
	 * 基础测试：非in子句测试
	 */
	@Test
	public void isInClauseTest2() {
		// 测试基本的ORDER BY移除
		//final String sql = "select case when  1 in (1) and 2 = any(:number) then 1 else 0 end";
		final String sql = "select case when 1=1 and 2 = any(";

		assertFalse(SqlUtil.isInClause(sql));
	}

	/**
	 * 测试 包含in但非param对应的in子句测试
	 */
	@Test
	public void isInClauseTest3() {
		// 截断前sql
		//final String sql =
		final String sql = "select case when 1 in (?,?,?) and 2 = any(";

		assertFalse(SqlUtil.isInClause(sql));
	}

	/**
	 * 测试 包含in但非param对应的in子句测试
	 */
	@Test
	public void isInClauseTest4() {
		// 截断前sql
		//final String sql =
		final String sql = "select case when 1 in (?,?,?) and 2 = any(";

		assertFalse(SqlUtil.isInClause(sql));
	}

}
