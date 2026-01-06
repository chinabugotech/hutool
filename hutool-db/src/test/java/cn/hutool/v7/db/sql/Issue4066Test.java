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
