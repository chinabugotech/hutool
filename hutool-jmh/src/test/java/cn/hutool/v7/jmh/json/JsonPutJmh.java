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

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import cn.hutool.v7.core.util.RandomUtil;
import cn.hutool.v7.json.JSONObject;
import org.openjdk.jmh.annotations.*;

import java.util.HashMap;
import java.util.Map;
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
public class JsonPutJmh {

	Map<String, String> testData;
	private JSONObject hutoolJSON;
	private JsonObject gson;
	private com.alibaba.fastjson2.JSONObject fastJSON;
	private ObjectNode jackson;
	private HashMap<String, Object> hashMap;


	@Setup
	public void setup() {
		testData = new HashMap<>(100, 1);
		for (int i = 0; i < 100; i++) {
			testData.put(RandomUtil.randomLettersAndNumbers(10), RandomUtil.randomLettersAndNumbers(20));
		}

		hutoolJSON = new JSONObject();
		gson = new JsonObject();
		fastJSON = new com.alibaba.fastjson2.JSONObject();
		jackson = JsonNodeFactory.instance.objectNode();
		hashMap = new HashMap<>();
	}

	@Benchmark
	public void gsonJmh() {
		testData.forEach(gson::addProperty);
	}

	@Benchmark
	public void hutoolJmh() {
		testData.forEach(hutoolJSON::putValue);
		//hutoolJSON.putAllObj(testData);
	}

	@Benchmark
	public void fastJSONJmh() {
		fastJSON.putAll(testData);
	}

	@Benchmark
	public void jacksonJmh(){
		testData.forEach(jackson::put);
	}

	@Benchmark
	public void hashMapJmh(){
		testData.forEach(hashMap::put);
	}
}
