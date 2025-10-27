package cn.hutool.v7.db.config;

import cn.hutool.v7.core.lang.Console;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TomlConfigParserTest {
	@Test
	public void parseTest() {
		DbConfig dbConfig = TomlConfigParser.of().parse("");
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
		DbConfig dbConfig = TomlConfigParser.of().parse("orcl");
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
