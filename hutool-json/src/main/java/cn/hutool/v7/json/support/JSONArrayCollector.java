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

package cn.hutool.v7.json.support;

import cn.hutool.v7.json.JSON;
import cn.hutool.v7.json.JSONArray;
import cn.hutool.v7.json.JSONFactory;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * JSONArray Collector
 *
 * @author Looly
 * @since 7.0.0
 */
public class JSONArrayCollector implements Collector<JSON, JSONArray, JSONArray> {

	/**
	 * 返回一个Collector，它将输入元素收集到一个新的JSONArray中
	 *
	 * @param factory JSON 工厂
	 * @return {@link JSONArrayCollector}
	 */
	public static JSONArrayCollector toJSONArray(final JSONFactory factory) {
		return new JSONArrayCollector(factory);
	}

	private final JSONFactory factory;

	/**
	 * 构造
	 *
	 * @param factory JSON 工厂
	 */
	public JSONArrayCollector(final JSONFactory factory) {
		this.factory = factory;
	}

	@Override
	public Supplier<JSONArray> supplier() {
		return this.factory::ofArray;
	}

	@Override
	public BiConsumer<JSONArray, JSON> accumulator() {
		return JSONArray::addValue;
	}

	@Override
	public BinaryOperator<JSONArray> combiner() {
		return (left, right) -> {
			left.addAll(right);
			return left;
		};
	}

	@Override
	public Function<JSONArray, JSONArray> finisher() {
		return Function.identity();
	}

	@Override
	public Set<Characteristics> characteristics() {
		return Collections.unmodifiableSet(EnumSet.of(
			Characteristics.IDENTITY_FINISH,
			Characteristics.CONCURRENT
		));
	}
}
