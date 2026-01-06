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

import cn.hutool.v7.json.*;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class JSONArrayCollectorTest {

	private final JSONFactory factory = JSONFactory.of(JSONConfig.of());

	@Test
	void testSupplier() {
		final JSONArrayCollector collector = new JSONArrayCollector(factory);
		final JSONArray array = collector.supplier().get();
		assertTrue(array.isEmpty());
	}

	@Test
	void testAccumulator() {
		final JSONArrayCollector collector = new JSONArrayCollector(factory);
		final JSONArray array = collector.supplier().get();
		final JSONObject obj = factory.ofObj();
		obj.putValue("key", "value");

		collector.accumulator().accept(array, obj);
		assertEquals(1, array.size());
		assertEquals(obj, array.get(0));
	}

	@Test
	void testCombiner() {
		final JSONArrayCollector collector = new JSONArrayCollector(factory);
		final JSONArray left = factory.ofArray();
		final JSONArray right = factory.ofArray();

		final JSONObject obj1 = factory.ofObj();
		obj1.putValue("key1", "value1");
		left.add(obj1);

		final JSONObject obj2 = factory.ofObj();
		obj2.putValue("key2", "value2");
		right.add(obj2);

		final JSONArray combined = collector.combiner().apply(left, right);
		assertEquals(2, combined.size());
		assertEquals(obj1, combined.get(0));
		assertEquals(obj2, combined.get(1));
	}

	@Test
	void testFinisher() {
		final JSONArrayCollector collector = new JSONArrayCollector(factory);
		final JSONArray array = factory.ofArray();
		final JSONArray result = collector.finisher().apply(array);
		assertSame(array, result);
	}

	@Test
	void testCharacteristics() {
		final JSONArrayCollector collector = new JSONArrayCollector(factory);
		final Set<Collector.Characteristics> characteristics = collector.characteristics();
		assertEquals(2, characteristics.size());
		assertTrue(characteristics.contains(Collector.Characteristics.IDENTITY_FINISH));
		assertTrue(characteristics.contains(Collector.Characteristics.CONCURRENT));
	}

	@Test
	void testFullCollectionProcess() {
		final JSONObject obj1 = factory.ofObj();
		obj1.putValue("key1", "value1");
		final JSONObject obj2 = factory.ofObj();
		obj2.putValue("key2", "value2");

		final JSONArray result = Stream.of(obj1, obj2)
			.collect(JSONArrayCollector.toJSONArray(factory));

		assertEquals(2, result.size());
		assertEquals(obj1, result.get(0));
		assertEquals(obj2, result.get(1));
	}

	@SuppressWarnings("RedundantOperationOnEmptyContainer")
	@Test
	void testEmptyCollection() {
		final JSONArray result = Collections.<JSON>emptyList().stream()
			.collect(JSONArrayCollector.toJSONArray(factory));

		assertTrue(result.isEmpty());
	}
}
