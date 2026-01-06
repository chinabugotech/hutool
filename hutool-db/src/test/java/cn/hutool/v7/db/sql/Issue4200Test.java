/*
 * Copyright (c) 2013-2026 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.hutool.v7.db.sql;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
