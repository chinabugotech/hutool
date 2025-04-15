/*
 * Copyright (c) 2025 Hutool Team and hutool.cn
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
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import cn.hutool.v7.json.JSONObject;
import cn.hutool.v7.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/**
 * 测试JSON树结构转JSON字符串性能
 */
@BenchmarkMode(Mode.AverageTime)//每次执行平均花费时间
@Warmup(iterations = 1, time = 1) //预热5次调用
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS) // 执行5此，每次1秒
@Threads(1) //单线程
@Fork(1) //
@OutputTimeUnit(TimeUnit.NANOSECONDS) // 单位：纳秒
@State(Scope.Benchmark) // 共享域
public class JsonToStringJmh {

	private JSONObject hutoolJSON;
	private JsonElement gson;
	private com.alibaba.fastjson2.JSONObject fastJSON;


	@Setup
	public void setup() {
		final String jsonStr = "{\"name\":\"张三\",\"age\":18,\"birthday\":\"2020-01-01\"}";
		hutoolJSON = JSONUtil.parseObj(jsonStr);
		gson = JsonParser.parseString(jsonStr);
		fastJSON = JSON.parseObject(jsonStr);
	}

	@Benchmark
	public void gsonJmh() {
		final String jsonStr = gson.toString();
		Assertions.assertNotNull(jsonStr);
	}

	@Benchmark
	public void hutoolJmh() {
		final String jsonStr = hutoolJSON.toString();
		Assertions.assertNotNull(jsonStr);
	}

	@Benchmark
	public void fastJSONJmh() {
		final String jsonStr = fastJSON.toString();
		Assertions.assertNotNull(jsonStr);
	}
}
