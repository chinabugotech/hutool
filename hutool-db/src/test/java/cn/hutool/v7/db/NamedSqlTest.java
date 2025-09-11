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

import cn.hutool.v7.core.map.MapUtil;
import cn.hutool.v7.db.sql.NamedSql;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NamedSqlTest {

	@Test
	public void parseTest() {
		final String sql = "select * from table where id=@id and name = @name1 and nickName = :subName";

		final Map<String, Object> paramMap = MapUtil
				.builder("name1", (Object)"张三")
				.put("age", 12)
				.put("subName", "小豆豆")
				.build();

		final NamedSql namedSql = new NamedSql(sql, paramMap);
		//未指定参数原样输出
		assertEquals("select * from table where id=@id and name = ? and nickName = ?", namedSql.getSql());
		assertEquals("张三", namedSql.getParamArray()[0]);
		assertEquals("小豆豆", namedSql.getParamArray()[1]);
	}

	@Test
	public void parseTest2() {
		final String sql = "select * from table where id=@id and name = @name1 and nickName = :subName";

		final Map<String, Object> paramMap = MapUtil
				.builder("name1", (Object)"张三")
				.put("age", 12)
				.put("subName", "小豆豆")
				.put("id", null)
				.build();

		final NamedSql namedSql = new NamedSql(sql, paramMap);
		assertEquals("select * from table where id=? and name = ? and nickName = ?", namedSql.getSql());
		//指定了null参数的依旧替换，参数值为null
		Assertions.assertNull(namedSql.getParamArray()[0]);
		assertEquals("张三", namedSql.getParamArray()[1]);
		assertEquals("小豆豆", namedSql.getParamArray()[2]);
	}

	@Test
	public void parseTest3() {
		// 测试连续变量名出现是否有问题
		final String sql = "SELECT to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') as sysdate FROM dual";

		final Map<String, Object> paramMap = MapUtil
				.builder("name1", (Object)"张三")
				.build();

		final NamedSql namedSql = new NamedSql(sql, paramMap);
		assertEquals(sql, namedSql.getSql());
	}

	@Test
	public void parseTest4() {
		// 测试postgre中形如data_value::numeric是否出错
		final String sql = "select device_key, min(data_value::numeric) as data_value from device";

		final Map<String, Object> paramMap = MapUtil
				.builder("name1", (Object)"张三")
				.build();

		final NamedSql namedSql = new NamedSql(sql, paramMap);
		assertEquals(sql, namedSql.getSql());
	}

	@Test
	public void parseInTest(){
		final String sql = "select * from user where id in (:ids)";
		final HashMap<String, Object> paramMap = MapUtil.of("ids", new int[]{1, 2, 3});

		final NamedSql namedSql = new NamedSql(sql, paramMap);
		assertEquals("select * from user where id in (?,?,?)", namedSql.getSql());
		assertEquals(1, namedSql.getParamArray()[0]);
		assertEquals(2, namedSql.getParamArray()[1]);
		assertEquals(3, namedSql.getParamArray()[2]);
	}

	@Test
	public void queryTest() {
		final Map<String, Object> paramMap = MapUtil
				.builder("name1", (Object)"王五")
				.put("age1", 18).build();
		final String sql = "select * from user where name = @name1 and age = @age1";

		List<Entity> query = Db.of().query(sql, paramMap);
		assertEquals(1, query.size());

		// 采用传统方式查询是否能识别Map类型参数
		query = Db.of().query(sql, new Object[]{paramMap});
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
		assertArrayEquals(new int[]{10, 20}, (int[]) namedSql.getParamArray()[0]);
	}

	@Test
	public void parseInTest3() {
		// 测试字符串中包含"in"关键字但不是IN子句的情况
		final String sql = "select * from user where comment = 'include in text' and id = :id";
		final HashMap<String, Object> paramMap = MapUtil.of("id", new int[]{5, 6});

		final NamedSql namedSql = new NamedSql(sql, paramMap);
		// sql语句不包含IN子句，不会展开数组
		assertEquals("select * from user where comment = 'include in text' and id = ?", namedSql.getSql());
		assertArrayEquals(new int[]{5, 6}, (int[]) namedSql.getParamArray()[0]);
	}
}
