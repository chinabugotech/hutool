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

package cn.hutool.v7.db.config;

import cn.hutool.v7.core.lang.Console;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TomlConfigParserTest {
	@Test
	public void parseTest() {
		final DbConfig dbConfig = TomlConfigParser.of().parse("");
		assertEquals("org.sqlite.JDBC", dbConfig.getDriver());
		assertEquals("jdbc:sqlite:test.db", dbConfig.getUrl());
		assertNull(dbConfig.getUser());
		assertNull(dbConfig.getPass());

		assertEquals(1, dbConfig.getConnProps().size());
		assertEquals("true", dbConfig.getConnProps().getProperty("remarks"));
		assertNull(dbConfig.getPoolProps());
	}

	@Test
	void parseOrclTest(){
		final DbConfig dbConfig = TomlConfigParser.of().parse("orcl");
		Console.log(dbConfig);

		assertEquals("oracle.jdbc.OracleDriver", dbConfig.getDriver());
		assertEquals("jdbc:oracle:thin:@//localhost:1521/XEPDB1", dbConfig.getUrl());
		assertEquals("system", dbConfig.getUser());
		assertEquals("123456", dbConfig.getPass());

		assertEquals(1, dbConfig.getConnProps().size());
		assertEquals("true", dbConfig.getConnProps().getProperty("remarks"));
		assertNull(dbConfig.getPoolProps());
	}
}
