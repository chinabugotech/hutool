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

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.gson.JsonArray;
import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.core.util.RandomUtil;
import cn.hutool.v7.json.JSONArray;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 测试JSON树结构转JSON字符串性能
 */
@BenchmarkMode(Mode.AverageTime)//每次执行平均花费时间
@Warmup(iterations = 1, time = 1) //预热5次调用
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS) // 执行5此，每次1秒
@Threads(1) //单线程
@Fork(1) //
@OutputTimeUnit(TimeUnit.NANOSECONDS) // 单位：纳秒
@State(Scope.Benchmark) // 共享域
public class JsonAddJmh {

	List<String> testData;
	private JSONArray hutoolJSON;
	private JsonArray gson;
	private com.alibaba.fastjson2.JSONArray fastJSON;
	private ArrayNode jackson;
	private ArrayList<Object> arrayList;


	@Setup
	public void setup() {
		Console.log("准备数据。。。");
		testData = new ArrayList<>(10);
		for (int i = 0; i < 10; i++) {
			testData.add(RandomUtil.randomLettersAndNumbers(20));
		}

		hutoolJSON = new JSONArray();
		gson = new JsonArray();
		fastJSON = new com.alibaba.fastjson2.JSONArray();
		jackson = JsonNodeFactory.instance.arrayNode();
		arrayList = new ArrayList<>();
		Console.log("数据完毕");
	}

	@Benchmark
	public void gsonJmh() {
		testData.forEach(gson::add);
	}

	@Benchmark
	public void hutoolJmh() {
		testData.forEach(hutoolJSON::addValue);
		//hutoolJSON.putAllObj(testData);
	}

	@SuppressWarnings("UseBulkOperation")
	@Benchmark
	public void fastJSONJmh() {
		testData.forEach(fastJSON::add);
	}

	@Benchmark
	public void jacksonJmh(){
		testData.forEach(jackson::add);
	}

	@Benchmark
	public void arrayListJmh(){
		testData.forEach(arrayList::add);
	}
}
