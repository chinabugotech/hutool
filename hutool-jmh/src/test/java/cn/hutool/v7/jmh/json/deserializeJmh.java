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
import cn.hutool.v7.json.JSON;
import cn.hutool.v7.json.JSONUtil;
import cn.hutool.v7.json.engine.JSONEngine;
import cn.hutool.v7.json.engine.JSONEngineFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonElement;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)//每次执行平均花费时间
@Warmup(iterations = 5, time = 1) //预热5次调用
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS) // 执行5次，每次1秒
@Threads(1) //单线程
@Fork(1) //
@OutputTimeUnit(TimeUnit.NANOSECONDS) // 单位：纳秒
@State(Scope.Benchmark) // 共享域
public class deserializeJmh {

	private JSONEngine jacksonEngine;
	private JSONEngine gsonEngine;
	private JSONEngine fastJSONEngine;
	private JSONEngine hutoolEngine;
	private JSONEngine wastEngine;

	@Setup
	public void setup() {
		jacksonEngine = JSONEngineFactory.createEngine("jackson");
		gsonEngine = JSONEngineFactory.createEngine("gson");
		fastJSONEngine = JSONEngineFactory.createEngine("fastjson");
		hutoolEngine = JSONEngineFactory.createEngine("hutool");
		wastEngine = JSONEngineFactory.createEngine("wast");
	}

	@Benchmark
	public void jacksonJmh() {
		try(final Reader jsonFileReader = FileUtil.getUtf8Reader(JSONJmhData.OUTPUT_FILE_PATH)){
			jacksonEngine.deserialize(jsonFileReader, JsonNode.class);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Benchmark
	public void gsonJmh() {
		try(final Reader jsonFileReader = FileUtil.getUtf8Reader(JSONJmhData.OUTPUT_FILE_PATH)){
			gsonEngine.deserialize(jsonFileReader, JsonElement.class);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Benchmark
	public void fastJSONJmh() {
		try(final Reader jsonFileReader = FileUtil.getUtf8Reader(JSONJmhData.OUTPUT_FILE_PATH)){
			fastJSONEngine.deserialize(jsonFileReader, com.alibaba.fastjson2.JSON.class);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Benchmark
	public void wastJmh() {
		try(final Reader jsonFileReader = FileUtil.getUtf8Reader(JSONJmhData.OUTPUT_FILE_PATH)){
			wastEngine.deserialize(jsonFileReader, Map.class);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Benchmark
	public void hutoolJSONJmh() {
		try(final Reader jsonFileReader = FileUtil.getUtf8Reader(JSONJmhData.OUTPUT_FILE_PATH)){
			hutoolEngine.deserialize(jsonFileReader, JSON.class);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Benchmark
	public void hutoolJSONParseJmh() {
		try(final Reader jsonFileReader = FileUtil.getUtf8Reader(JSONJmhData.OUTPUT_FILE_PATH)){
			JSONUtil.parse(jsonFileReader);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
}
