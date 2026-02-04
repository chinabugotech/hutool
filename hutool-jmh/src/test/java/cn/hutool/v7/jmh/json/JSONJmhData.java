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

package cn.hutool.v7.jmh.json;

import cn.hutool.v7.core.io.file.FileUtil;
import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.json.JSONArray;
import cn.hutool.v7.json.JSONObject;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class JSONJmhData {
	public static String jsonStr = """
		{
		  "name": "张三",
		  "age": 18,
		  "birthday": "2020-01-01",
		  "booleanValue": true,
		  "jsonObjectSub": {
		    "subStr": "abc",
		    "subNumber": 150343445454,
		    "subBoolean": true
		  },
		  "jsonArraySub": [
		    "abc",
		    123,
		    false
		  ]
		}""";

	@Data
	public static class TestBean{
		private String name;
		private int age;
		private boolean gender;
		private Date createDate;
		private Object nullObj;
		private SubBean jsonObjectSub;
		private List<Object> jsonArraySub;
	}

	@Data
	public static class SubBean{
		private String subStr;
		private Long subNumber;
		private boolean subBoolean;
	}

	@Test
	void generateMookJsonTest() {
		generateMookJson();
	}

	// 配置项：可调整以生成不同大小的 JSON
	private static final int DATA_ITEM_COUNT = 100_000; // 数据条目数（10万条，约几十MB；100万条约几百MB）
	public static final String OUTPUT_FILE_PATH = "D:/test/big_test_data_hutool.json"; // 输出文件路径

	private static File generateMookJson() {
		// 1. 初始化 JSON 数组（存储核心数据）
		final JSONArray jsonArray = new JSONArray();
		final Random random = new Random();

		Console.log("开始生成 JSON 数据...");
		final long startTime = System.currentTimeMillis();

		// 2. 循环生成模拟数据条目
		for (int i = 0; i < DATA_ITEM_COUNT; i++) {
			final JSONObject dataItem = new JSONObject();
			// 基础字段（多种数据类型，贴近真实场景）
			dataItem.putValue("id", i + 1);
			dataItem.putValue("userName", "test_user_" + (i % 1000)); // 避免用户名重复过多
			dataItem.putValue("age", random.nextInt(50) + 20); // 年龄 20-69
			dataItem.putValue("email", "test_" + i + "@example.com");
			dataItem.putValue("phone", "138" + String.format("%08d", random.nextInt(99999999)));
			dataItem.putValue("isVip", random.nextBoolean()); // 布尔类型
			dataItem.putValue("balance", random.nextDouble() * 10000); // 浮点类型（余额）
			dataItem.putValue("registerTime", "202" + (random.nextInt(6)) + "-" +
				String.format("%02d", random.nextInt(12) + 1) + "-" +
				String.format("%02d", random.nextInt(28) + 1)); // 日期字符串

			// 嵌套对象（增加 JSON 复杂度，更贴近真实测试场景）
			final JSONObject address = new JSONObject();
			address.putValue("province", "省份_" + (random.nextInt(34) + 1));
			address.putValue("city", "城市_" + (random.nextInt(10) + 1));
			address.putValue("detail", "详细地址_" + i);
			dataItem.putValue("address", address);

			// 3. 将单个条目添加到 JSON 数组
			jsonArray.add(dataItem);

			// 可选：每生成 1 万条打印进度，避免等待焦虑
			if ((i + 1) % 10000 == 0) {
				Console.log("已生成 " + (i + 1) + " 条数据");
			}
		}

		// 4. 流式写入文件（避免内存积压）
		final File file = FileUtil.file(OUTPUT_FILE_PATH);
		try (final FileWriter fileWriter = new FileWriter(file)) {
			jsonArray.write(fileWriter); // Hutool 内置流式写入，无需手动拼接
			final long endTime = System.currentTimeMillis();
			Console.log("JSON 文件生成完成！");
			Console.log("文件路径：" + OUTPUT_FILE_PATH);
			Console.log("耗时：" + (endTime - startTime) + " 毫秒");
			Console.log("数据条目数：" + DATA_ITEM_COUNT);
		} catch (final IOException e) {
			System.err.println("文件写入失败：" + e.getMessage());
		}
		return file;
	}
}
