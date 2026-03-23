package cn.hutool.db;

import cn.hutool.core.map.MapUtil;
import cn.hutool.db.sql.NamedSql;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class NamedSqlTest {

	@Test
	public void parseTest() {
		String sql = "select * from table where id=@id and name = @name1 and nickName = :subName";

		Map<String, Object> paramMap = MapUtil
				.builder("name1", (Object)"张三")
				.put("age", 12)
				.put("subName", "小豆豆")
				.build();

		NamedSql namedSql = new NamedSql(sql, paramMap);
		//未指定参数原样输出
		assertEquals("select * from table where id=@id and name = ? and nickName = ?", namedSql.getSql());
		assertEquals("张三", namedSql.getParams()[0]);
		assertEquals("小豆豆", namedSql.getParams()[1]);
	}

	@Test
	public void parseTest2() {
		String sql = "select * from table where id=@id and name = @name1 and nickName = :subName";

		Map<String, Object> paramMap = MapUtil
				.builder("name1", (Object)"张三")
				.put("age", 12)
				.put("subName", "小豆豆")
				.put("id", null)
				.build();

		NamedSql namedSql = new NamedSql(sql, paramMap);
		assertEquals("select * from table where id=? and name = ? and nickName = ?", namedSql.getSql());
		//指定了null参数的依旧替换，参数值为null
		assertNull(namedSql.getParams()[0]);
		assertEquals("张三", namedSql.getParams()[1]);
		assertEquals("小豆豆", namedSql.getParams()[2]);
	}

	@Test
	public void parseTest3() {
		// 测试连续变量名出现是否有问题
		String sql = "SELECT to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') as sysdate FROM dual";

		Map<String, Object> paramMap = MapUtil
				.builder("name1", (Object)"张三")
				.build();

		NamedSql namedSql = new NamedSql(sql, paramMap);
		assertEquals(sql, namedSql.getSql());
	}

	@Test
	public void parseTest4() {
		// 测试postgre中形如data_value::numeric是否出错
		String sql = "select device_key, min(data_value::numeric) as data_value from device";

		Map<String, Object> paramMap = MapUtil
				.builder("name1", (Object)"张三")
				.build();

		NamedSql namedSql = new NamedSql(sql, paramMap);
		assertEquals(sql, namedSql.getSql());
	}

	@Test
	public void parseInTest(){
		String sql = "select * from user where id in (:ids)";
		final HashMap<String, Object> paramMap = MapUtil.of("ids", new int[]{1, 2, 3});

		NamedSql namedSql = new NamedSql(sql, paramMap);
		assertEquals("select * from user where id in (?,?,?)", namedSql.getSql());
		assertEquals(1, namedSql.getParams()[0]);
		assertEquals(2, namedSql.getParams()[1]);
		assertEquals(3, namedSql.getParams()[2]);
	}

	@Test
	public void queryTest() throws SQLException {
		Map<String, Object> paramMap = MapUtil
				.builder("name1", (Object)"王五")
				.put("age1", 18).build();
		String sql = "select * from user where name = @name1 and age = @age1";

		List<Entity> query = Db.use().query(sql, paramMap);
		assertEquals(1, query.size());

		// 采用传统方式查询是否能识别Map类型参数
		query = Db.use().query(sql, new Object[]{paramMap});
		assertEquals(1, query.size());
	}

	@Test
	public void parseInTest2() {
		// 测试表名包含"in"但不是IN子句的情况
		final String sql = "select * from information where info_data = :info";
		final HashMap<String, Object> paramMap = MapUtil.of("info", new int[]{10, 20});

		final NamedSql namedSql = new NamedSql(sql, paramMap);
		// sql语句不包含IN子句，不会展开数组
		assertEquals("select * from information where info_data = ?", namedSql.getSql());
		assertArrayEquals(new int[]{10, 20}, (int[]) namedSql.getParams()[0]);
	}

	@Test
	public void parseInTest3() {
		// 测试字符串中包含"in"关键字但不是IN子句的情况
		final String sql = "select * from user where comment = 'include in text' and id = :id";
		final HashMap<String, Object> paramMap = MapUtil.of("id", new int[]{5, 6});

		final NamedSql namedSql = new NamedSql(sql, paramMap);
		// sql语句不包含IN子句，不会展开数组
		assertEquals("select * from user where comment = 'include in text' and id = ?", namedSql.getSql());
		assertArrayEquals(new int[]{5, 6}, (int[]) namedSql.getParams()[0]);
	}

	@Test
	void selectCaseInTest() {
		final HashMap<String, Object> paramMap = MapUtil.of("number", new int[]{1, 2, 3});

		NamedSql namedSql = new NamedSql("select case when 2 = any(ARRAY[:number]) and 1 in (1) then 1 else 0 end", paramMap);
		assertEquals("select case when 2 = any(ARRAY[?]) and 1 in (1) then 1 else 0 end", namedSql.getSql());
		assertArrayEquals(new int[]{1, 2, 3}, (int[])namedSql.getParams()[0]);
	}

	@Test
	public void parseInsertMultiRowTest() {
		// 多行 INSERT 语句
		final Map<String, Object> paramMap = new LinkedHashMap<>();
		paramMap.put("user1", new Object[]{1, "looly"});
		paramMap.put("user2", new Object[]{2, "xxxtea"});

		String sql = "INSERT INTO users (id, name) VALUES (:user1), (:user2)";
		NamedSql namedSql = new NamedSql(sql, paramMap);

		assertEquals("INSERT INTO users (id, name) VALUES (?), (?)", namedSql.getSql());
		assertArrayEquals(new Object[]{new Object[]{1, "looly"}, new Object[]{2, "xxxtea"}}, namedSql.getParams());
	}
}
