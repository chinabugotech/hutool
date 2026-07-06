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

package cn.hutool.v7.poi.csv;

import cn.hutool.v7.core.annotation.Alias;
import cn.hutool.v7.core.collection.CollUtil;
import cn.hutool.v7.core.io.file.FileUtil;
import cn.hutool.v7.core.io.resource.ResourceUtil;
import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.core.util.CharsetUtil;
import lombok.Data;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SuppressWarnings("resource")
public class CsvReaderTest {

	@Test
	public void readTest() {
		final CsvReader reader = new CsvReader();
		final CsvData data = reader.read(
			ResourceUtil.getReader("test.csv", CharsetUtil.UTF_8), true);
		assertEquals("sss,sss", data.getRow(0).get(0));
		assertEquals(1, data.getRow(0).getOriginalLineNumber());
		assertEquals("性别", data.getRow(0).get(2));
		assertEquals("关注\"对象\"", data.getRow(0).get(3));
	}

	@Test
	public void readMapListTest() {
		final CsvReader reader = CsvUtil.getReader();
		final List<Map<String, String>> result = reader.readMapList(
				ResourceUtil.getUtf8Reader("test_bean.csv"), true);

		assertEquals("张三", result.get(0).get("姓名"));
		assertEquals("男", result.get(0).get("gender"));
		assertEquals("无", result.get(0).get("focus"));
		assertEquals("33", result.get(0).get("age"));

		assertEquals("李四", result.get(1).get("姓名"));
		assertEquals("男", result.get(1).get("gender"));
		assertEquals("好对象", result.get(1).get("focus"));
		assertEquals("23", result.get(1).get("age"));

		assertEquals("王妹妹", result.get(2).get("姓名"));
		assertEquals("女", result.get(2).get("gender"));
		assertEquals("特别关注", result.get(2).get("focus"));
		assertEquals("22", result.get(2).get("age"));
	}

	@Test
	public void readAliasMapListTest() {
		final CsvReadConfig csvReadConfig = CsvReadConfig.of();
		csvReadConfig.addHeaderAlias("姓名", "name");

		final CsvReader reader = CsvUtil.getReader(csvReadConfig);
		final List<Map<String, String>> result = reader.readMapList(
				ResourceUtil.getUtf8Reader("test_bean.csv"), true);

		assertEquals("张三", result.get(0).get("name"));
		assertEquals("男", result.get(0).get("gender"));
		assertEquals("无", result.get(0).get("focus"));
		assertEquals("33", result.get(0).get("age"));

		assertEquals("李四", result.get(1).get("name"));
		assertEquals("男", result.get(1).get("gender"));
		assertEquals("好对象", result.get(1).get("focus"));
		assertEquals("23", result.get(1).get("age"));

		assertEquals("王妹妹", result.get(2).get("name"));
		assertEquals("女", result.get(2).get("gender"));
		assertEquals("特别关注", result.get(2).get("focus"));
		assertEquals("22", result.get(2).get("age"));
	}

	@Test
	public void readBeanListTest() {
		final CsvReader reader = CsvUtil.getReader();
		final List<TestBean> result = reader.read(
				ResourceUtil.getUtf8Reader("test_bean.csv"), true, TestBean.class);

		assertEquals("张三", result.get(0).getName());
		assertEquals("男", result.get(0).getGender());
		assertEquals("无", result.get(0).getFocus());
		assertEquals(Integer.valueOf(33), result.get(0).getAge());

		assertEquals("李四", result.get(1).getName());
		assertEquals("男", result.get(1).getGender());
		assertEquals("好对象", result.get(1).getFocus());
		assertEquals(Integer.valueOf(23), result.get(1).getAge());

		assertEquals("王妹妹", result.get(2).getName());
		assertEquals("女", result.get(2).getGender());
		assertEquals("特别关注", result.get(2).getFocus());
		assertEquals(Integer.valueOf(22), result.get(2).getAge());
	}

	@Data
	private static class TestBean {
		@Alias("姓名")
		private String name;
		private String gender;
		private String focus;
		private Integer age;
	}

	@Test
	@Disabled
	public void readTest2() {
		final CsvReader reader = CsvUtil.getReader();
		final CsvData read = reader.read(FileUtil.file("d:/test/test.csv"));
		for (final CsvRow strings : read) {
			Console.log(strings);
		}
	}

	@Test
	@Disabled
	public void readTest3() {
		final CsvReadConfig csvReadConfig = CsvReadConfig.of();
		csvReadConfig.setContainsHeader(true);
		final CsvReader reader = CsvUtil.getReader(csvReadConfig);
		final CsvData read = reader.read(FileUtil.file("d:/test/ceshi.csv"));
		for (final CsvRow row : read) {
			Console.log(row.getByName("案件ID"));
		}
	}

	@Test
	public void lineNoTest() {
		final CsvReader reader = new CsvReader();
		final CsvData data = reader.read(
			ResourceUtil.getReader("test_lines.csv", CharsetUtil.UTF_8), true);
		assertEquals(1, data.getRow(0).getOriginalLineNumber());
		assertEquals("a,b,c,d", CollUtil.join(data.getRow(0), ","));

		assertEquals(4, data.getRow(2).getOriginalLineNumber());
		assertEquals("q,w,e,r,我是一段\n带换行的内容",
				CollUtil.join(data.getRow(2), ",").replace("\r", ""));

		// 文件中第3行数据，对应原始行号是6（从0开始）
		assertEquals(6, data.getRow(3).getOriginalLineNumber());
		assertEquals("a,s,d,f", CollUtil.join(data.getRow(3), ","));
	}

	@Test
	public void lineLimitTest() {
		// 从原始第2行开始读取
		final CsvReader reader = new CsvReader(CsvReadConfig.of().setBeginLineNo(2));
		final CsvData data = reader.read(
			ResourceUtil.getUtf8Reader("test_lines.csv"), true);

		assertEquals(2, data.getRow(0).getOriginalLineNumber());
		assertEquals("1,2,3,4", CollUtil.join(data.getRow(0), ","));

		assertEquals(4, data.getRow(1).getOriginalLineNumber());
		assertEquals("q,w,e,r,我是一段\n带换行的内容",
				CollUtil.join(data.getRow(1), ",").replace("\r", ""));

		// 文件中第3行数据，对应原始行号是6（从0开始）
		assertEquals(6, data.getRow(2).getOriginalLineNumber());
		assertEquals("a,s,d,f", CollUtil.join(data.getRow(2), ","));
	}

	@Test
	public void lineLimitWithHeaderTest() {
		// 从原始第2行开始读取
		final CsvReader reader = new CsvReader(CsvReadConfig.of().setBeginLineNo(2).setContainsHeader(true));
		final CsvData data = reader.read(
			ResourceUtil.getUtf8Reader("test_lines.csv"), true);

		assertEquals(4, data.getRow(0).getOriginalLineNumber());
		assertEquals("q,w,e,r,我是一段\n带换行的内容",
				CollUtil.join(data.getRow(0), ",").replace("\r", ""));

		// 文件中第3行数据，对应原始行号是6（从0开始）
		assertEquals(6, data.getRow(1).getOriginalLineNumber());
		assertEquals("a,s,d,f", CollUtil.join(data.getRow(1), ","));
	}

	@Test
	public void customConfigTest() {
		final CsvReader reader = CsvUtil.getReader(
				CsvReadConfig.of()
						.setTextDelimiter('\'')
						.setFieldSeparator(';'));
		final CsvData csvRows = reader.readFromStr("123;456;'789;0'abc;");
		final CsvRow row = csvRows.getRow(0);
		assertEquals("123", row.get(0));
		assertEquals("456", row.get(1));
		assertEquals("'789;0'abc", row.get(2));
	}

	@Test
	public void readDisableCommentTest() {
		final CsvReader reader = CsvUtil.getReader(CsvReadConfig.of().disableComment());
		final CsvData read = reader.read(
			ResourceUtil.getUtf8Reader("test.csv"), true);
		final CsvRow row = read.getRow(0);
		assertEquals("# 这是一行注释，读取时应忽略", row.get(0));
	}

	@Test
	@Disabled
	public void streamTest() {
		final CsvReader reader = CsvUtil.getReader(ResourceUtil.getUtf8Reader("test_bean.csv"));
		reader.stream().limit(2).forEach(Console::log);
	}

	@Test
	@Disabled
	public void issue2306Test(){
		final CsvReader reader = CsvUtil.getReader(ResourceUtil.getUtf8Reader("d:/test/issue2306.csv"));
		final CsvData csvData = reader.read();
		for (final CsvRow csvRow : csvData) {
			Console.log(csvRow);
		}
	}

	@Test
	public void csvRowGetNegativeIndexReturnsNull() {
		// CsvRow.get(int) should return null for any out-of-bounds index, including negative.
		final CsvReader reader = new CsvReader();
		final CsvData data = reader.readFromStr("a,b,c\n");
		final CsvRow row = data.getRow(0);
		// Negative index should return null, not throw IndexOutOfBoundsException
		assertNull(row.get(-1));
		assertNull(row.get(-100));
		// Positive out-of-bounds already works
		assertNull(row.get(10));
	}
}
