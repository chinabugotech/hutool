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

package cn.hutool.v7.jmh.core;

import cn.hutool.v7.core.io.buffer.FastCharBuffer;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)//每次执行平均花费时间
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS) //预热1次调用
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS) // 执行5此，每次1秒
@Threads(1) //单线程
@Fork(1) //
@OutputTimeUnit(TimeUnit.NANOSECONDS) // 单位：纳秒
@State(Scope.Benchmark) // 共享域
public class CharBufferJmh {

	private final int appendCount = 10000;
	private String str;

	@Setup
	public void setup() {
		str = "abc123你好";
	}

	@SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder")
	@Benchmark
	public void stringBuilderJmh() {
		final StringBuilder stringBuilder = new StringBuilder(1024);
		for (int i = 0; i < appendCount; i++) {
			stringBuilder.append(str);
		}
	}

	@Benchmark
	public void fastCharBufferJmh() {
		final FastCharBuffer fastCharBuffer = new FastCharBuffer(1024);
		for (int i = 0; i < appendCount; i++) {
			fastCharBuffer.append(str);
		}
	}
}
