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

package cn.hutool.v7.core.map;

import cn.hutool.v7.core.map.multi.CollectionValueMap;
import cn.hutool.v7.core.map.multi.MultiValueMap;
import cn.hutool.v7.core.text.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class CollectionValueMapTest {

	@Test
	public void putTest() {
		final MultiValueMap<Integer, String> map = new CollectionValueMap<>();
		Assertions.assertNull(map.put(1, Arrays.asList("a", "b")));
		final Collection<String> collection = map.put(1, Arrays.asList("c", "d"));
		Assertions.assertEquals(Arrays.asList("a", "b"), collection);
	}

	@Test
	public void putAllTest() {
		final MultiValueMap<Integer, String> map = new CollectionValueMap<>();
		final Map<Integer, Collection<String>> source = new HashMap<>();
		source.put(1, Arrays.asList("a", "b", "c"));
		map.putAll(source);
		Assertions.assertEquals(1, map.size());
		Assertions.assertEquals(Arrays.asList("a", "b", "c"), map.get(1));
	}

	@Test
	public void putValueTest() {
		final MultiValueMap<Integer, String> map = new CollectionValueMap<>();
		Assertions.assertTrue(map.putValue(1, "a"));
		Assertions.assertTrue(map.putValue(1, "b"));
		Assertions.assertTrue(map.putValue(1, "c"));
		Assertions.assertEquals(1, map.size());
		Assertions.assertEquals(Arrays.asList("a", "b", "c"), map.get(1));
	}

	@Test
	public void putAllValueTest() {
		final MultiValueMap<Integer, String> map = new CollectionValueMap<>();
		Assertions.assertTrue(map.putAllValues(1, Arrays.asList("a", "b", "c")));
		Assertions.assertEquals(1, map.size());
		Assertions.assertEquals(Arrays.asList("a", "b", "c"), map.get(1));

		final Map<Integer, Collection<String>> source = new HashMap<>();
		Assertions.assertTrue(map.putValue(1, "e"));
		Assertions.assertTrue(map.putValue(1, "f"));
		map.putAllValues(source);
		Assertions.assertEquals(Arrays.asList("a", "b", "c", "e", "f"), map.get(1));
	}

	@Test
	public void putValuesTest() {
		final MultiValueMap<Integer, String> map = new CollectionValueMap<>();
		Assertions.assertTrue(map.putValues(1, "a", "b", "c"));
		Assertions.assertEquals(Arrays.asList("a", "b", "c"), map.get(1));
	}

	@Test
	public void testFilterAllValues() {
		final MultiValueMap<Integer, String> map = new CollectionValueMap<>();
		Assertions.assertTrue(map.putValues(1, "a", "b", "c"));
		Assertions.assertTrue(map.putValues(2, "a", "b", "c"));

		Assertions.assertEquals(map, map.filterAllValues((k, v) -> StrUtil.equals(v, "a")));
		Assertions.assertEquals(Collections.singletonList("a"), map.getValues(1));
		Assertions.assertEquals(Collections.singletonList("a"), map.getValues(2));

		Assertions.assertEquals(map, map.filterAllValues(v -> !StrUtil.equals(v, "a")));
		Assertions.assertEquals(Collections.emptyList(), map.getValues(1));
		Assertions.assertEquals(Collections.emptyList(), map.getValues(2));
	}

	@Test
	public void testReplaceAllValues() {
		final MultiValueMap<Integer, String> map = new CollectionValueMap<>();
		Assertions.assertTrue(map.putValues(1, "a", "b", "c"));
		Assertions.assertTrue(map.putValues(2, "a", "b", "c"));

		Assertions.assertEquals(map, map.replaceAllValues((k, v) -> v + "2"));
		Assertions.assertEquals(Arrays.asList("a2", "b2", "c2"), map.getValues(1));
		Assertions.assertEquals(Arrays.asList("a2", "b2", "c2"), map.getValues(2));

		Assertions.assertEquals(map, map.replaceAllValues(v -> v + "3"));
		Assertions.assertEquals(Arrays.asList("a23", "b23", "c23"), map.getValues(1));
		Assertions.assertEquals(Arrays.asList("a23", "b23", "c23"), map.getValues(2));
	}

	@Test
	public void removeValueTest() {
		final MultiValueMap<Integer, String> map = new CollectionValueMap<>();
		map.putValues(1, "a", "b", "c");
		Assertions.assertFalse(map.removeValue(1, "d"));
		Assertions.assertTrue(map.removeValue(1, "c"));
		Assertions.assertEquals(Arrays.asList("a", "b"), map.get(1));
	}

	@Test
	public void removeAllValuesTest() {
		final MultiValueMap<Integer, String> map = new CollectionValueMap<>();
		map.putValues(1, "a", "b", "c");
		Assertions.assertFalse(map.removeAllValues(1, Arrays.asList("e", "f")));
		Assertions.assertTrue(map.removeAllValues(1, Arrays.asList("b", "c")));
		Assertions.assertEquals(Collections.singletonList("a"), map.get(1));
	}

	@Test
	public void removeValuesTest() {
		final MultiValueMap<Integer, String> map = new CollectionValueMap<>();
		map.putValues(1, "a", "b", "c");
		Assertions.assertFalse(map.removeValues(1, "e", "f"));
		Assertions.assertTrue(map.removeValues(1, "b", "c"));
		Assertions.assertEquals(Collections.singletonList("a"), map.get(1));
	}

	@Test
	public void getValuesTest() {
		final MultiValueMap<Integer, String> map = new CollectionValueMap<>();
		map.putValues(1, "a", "b", "c");
		Assertions.assertEquals(Collections.emptyList(), map.getValues(2));
		Assertions.assertEquals(Arrays.asList("a", "b", "c"), map.getValues(1));
	}

	@Test
	public void sizeTest() {
		final MultiValueMap<Integer, String> map = new CollectionValueMap<>();
		map.putValues(1, "a", "b", "c");
		Assertions.assertEquals(0, map.size(2));
		Assertions.assertEquals(3, map.size(1));
	}

	@Test
	public void allForEachTest() {
		final MultiValueMap<Integer, String> map = new CollectionValueMap<>();
		map.putValues(1, "a", "b", "c");
		final List<Integer> keys = new ArrayList<>();
		final List<String> values = new ArrayList<>();
		map.allForEach((k, v) -> {
			keys.add(k);
			values.add(v);
		});
		Assertions.assertEquals(Arrays.asList(1, 1, 1), keys);
		Assertions.assertEquals(Arrays.asList("a", "b", "c"), values);
	}

	@Test
	public void allValuesTest() {
		final MultiValueMap<Integer, String> map = new CollectionValueMap<>(new LinkedHashMap<>());
		map.putAllValues(1, Arrays.asList("a", "b", "c"));
		map.putAllValues(2, Arrays.asList("d", "e"));
		Assertions.assertEquals(
			Arrays.asList("a", "b", "c", "d", "e"),
			map.allValues()
		);
	}

}
