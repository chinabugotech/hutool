package cn.hutool.v7.db;

import cn.hutool.v7.core.lang.Console;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HanaTest {
	//@BeforeAll
	public static void createTable() {
		final Db db = Db.of("hana");
		final long count = db.count("SELECT * FROM SYS.TABLES WHERE TABLE_NAME = ? AND SCHEMA_NAME = CURRENT_SCHEMA", "user");
		if (count > 0) {
			db.execute("drop table \"user\"");
		}
		db.execute("CREATE COLUMN TABLE \"user\" (\"id\" INT NOT NULL, \"account\" VARCHAR(255), \"name\" VARCHAR(255), \"text\" VARCHAR(255), \"test1\" VARCHAR(255), \"pass\" VARCHAR(255), PRIMARY KEY (\"id\"))");
	}

	@Test
	@Disabled
	public void insertTest() {
		for (int id = 100; id < 200; id++) {
			Db.of("hana").insert(Entity.of("user")//
				.set("id", id)//
				.set("name", "测试用户" + id)//
				.set("text", "描述" + id)//
				.set("test1", "t" + id)//
			);
		}
	}

	/**
	 * 事务测试<br>
	 * 更新三条信息，低2条后抛出异常，正常情况下三条都应该不变
	 *
	 * @throws SQLException SQL异常
	 */
	@Test
	@Disabled
	public void txTest() throws SQLException {
		Db.of("hana").tx(db -> {
			final int update = db.update(Entity.of("user").set("text", "描述100"), Entity.of().set("id", 100));
			db.update(Entity.of("user").set("text", "描述101"), Entity.of().set("id", 101));
			if (1 == update) {
				// 手动指定异常，然后测试回滚触发
				throw new RuntimeException("Error");
			}
			db.update(Entity.of("user").set("text", "描述102"), Entity.of().set("id", 102));
		});
	}

	@Test
	@Disabled
	public void pageTest() {
		final PageResult<Entity> result = Db.of("hana").page(Entity.of("\"user\""), new Page(2, 10));
		for (final Entity entity : result) {
			Console.log(entity.get("id"));
		}
	}

	@Test
	@Disabled
	public void getTimeStampTest() {
		final List<Entity> all = Db.of("hana").findAll("user");
		Console.log(all);
	}

	@Test
	@Disabled
	public void upsertTest() {
		final Db db = Db.of("hana");
		db.insert(Entity.of("user").set("id", 1).set("account", "ice").set("pass", "123456"));
		db.upsert(Entity.of("user").set("id", 1).set("account", "daoyou").set("pass", "a123456").set("name", "道友"));
		final Entity user = db.get(Entity.of("user").set("id", 1));
		System.out.println("user=======" + user.getStr("account") + "___" + user.getStr("pass"));
		assertEquals("daoyou", user.getStr("account"));
	}
}
