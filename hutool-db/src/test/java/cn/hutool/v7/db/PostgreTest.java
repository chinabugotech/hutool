/*
 * Copyright (c) 2013-2025 Hutool Team and hutool.cn
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

package cn.hutool.v7.db;

import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.core.map.MapUtil;
import cn.hutool.v7.db.sql.NamedSql;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

/**
 * PostgreSQL 单元测试
 *
 * @author Looly
 */
public class PostgreTest {

	@Test
	@Disabled
	public void insertTest() {
		for (int id = 100; id < 200; id++) {
			Db.of("postgre").insert(Entity.of("user")//
				.set("id", id)//
				.set("name", "测试用户" + id)//
			);
		}
	}

	@Test
	@Disabled
	public void pageTest() {
		final PageResult<Entity> result = Db.of("postgre").page(Entity.of("user"), new Page(2, 10));
		for (final Entity entity : result) {
			Console.log(entity.get("id"));
		}
	}

	@Test
	@Disabled
	public void upsertTest() {
		String createTableStr = """
				CREATE TABLE
				IF
					NOT EXISTS "ctest" (
						"id" serial4,
						"t1" VARCHAR ( 255 ) COLLATE "pg_catalog"."default",
						"t2" VARCHAR ( 255 ) COLLATE "pg_catalog"."default",
						"t3" VARCHAR ( 255 ) COLLATE "pg_catalog"."default",
					CONSTRAINT "ctest_pkey" PRIMARY KEY ( "id" )\s
					)
			""";

		final Db db = Db.of("postgre");
		db.executeBatch("drop table if exists ctest", createTableStr);
		db.insert(Entity.of("ctest").set("id", 1).set("t1", "111").set("t2", "222").set("t3", "333"));
		db.upsert(Entity.of("ctest").set("id", 1).set("t1", "new111").set("t2", "new222").set("t3", "bew333"), "id");
		final Entity et = db.get(Entity.of("ctest").set("id", 1));
		Assertions.assertEquals("new111", et.getStr("t1"));
	}

	@Test
	@Disabled
	void selectInTest() {
		final Db db = Db.of("postgre");
		final List<Entity> query = db.query("select * from ctest where id in(?, ?, ?)", 1, 2, 3);
		Console.log(query);
	}

	/**
	 * <a href="https://github.com/chinabugotech/hutool/issues/4111">issue#4111</a>
	 */
	@Test
	@Disabled
	void selectCaseWhenTest() {
		final Db db = Db.of("postgre");
		final List<Entity> query = db.query("select case when 2 = any(ARRAY[?]) and 1 in (1) then 1 else 0 end", new Object[]{new int[]{1, 2, 3}});
		Console.log(query);
	}

	@Test
	@Disabled
	void namedSqlWithInTest() {
		final HashMap<String, Object> paramMap = MapUtil.of("number", new int[]{1, 2, 3});
		NamedSql namedSql = new NamedSql("select case when 2 = any(ARRAY[:number]) and 1 in (1) then 1 else 0 end", paramMap);
		final Db db = Db.of("postgre");
		final List<Entity> query = db.query(namedSql.getSql(), namedSql.getParamArray());
		Console.log(query);
	}
}
