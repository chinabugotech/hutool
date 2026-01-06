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

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import cn.hutool.v7.json.JSONObject;
import cn.hutool.v7.json.JSONUtil;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * JSON将字符串解析为树结构的性能对比测试
 *
 * @author Looly
 */
@BenchmarkMode(Mode.AverageTime)//每次执行平均花费时间
@Warmup(iterations = 5, time = 1) //预热5次调用
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS) // 执行5此，每次1秒
@Threads(1) //单线程
@Fork(1) //
@OutputTimeUnit(TimeUnit.NANOSECONDS) // 单位：纳秒
@State(Scope.Benchmark) // 共享域
public class ParseTreeJmh {

	private String jsonStr;

	@Setup
	public void setup() {
		jsonStr = JSONJmhData.jsonStr;
	}

	@Benchmark
	public void gsonJmh() {
		final JsonElement jsonElement = JsonParser.parseString(jsonStr);
		assertNotNull(jsonElement);
	}

	@Benchmark
	public void hutoolJmh() {
		final JSONObject parse = JSONUtil.parseObj(jsonStr);
		assertNotNull(parse);
	}

	@Benchmark
	public void fastJSONJmh() {
		final com.alibaba.fastjson2.JSONObject jsonObject = JSON.parseObject(jsonStr);
		assertNotNull(jsonObject);
	}

	@Benchmark
	public void jacksonJmh() throws JsonProcessingException {
		final ObjectMapper mapper = new ObjectMapper();
		final JsonNode jsonNode = mapper.readTree(jsonStr);
		assertNotNull(jsonNode);
	}
}
