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

package cn.hutool.v7.json.issues;

import cn.hutool.v7.core.lang.tuple.Pair;
import cn.hutool.v7.core.lang.tuple.Triple;
import cn.hutool.v7.core.lang.tuple.Tuple;
import cn.hutool.v7.core.reflect.TypeReference;
import cn.hutool.v7.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI7GPGXTest {
	@Test
	public void pairToBeanTest() {
		final Pair<String, Boolean> hutoolPair = Pair.of("test1", true);
		final String a = JSONUtil.toJsonStr(hutoolPair);
		final Pair<String, Boolean> pair = JSONUtil.toBean(a, new TypeReference<Pair<String, Boolean>>() {});
		Assertions.assertEquals(hutoolPair, pair);
	}

	@Test
	void tripleToBeanTest() {
		final Triple<String, Integer, Boolean> hutoolTriple = Triple.of("aaa", 123, true);
		final String a = JSONUtil.toJsonStr(hutoolTriple);
		final Triple<String, Integer, Boolean> pair = JSONUtil.toBean(a, new TypeReference<Triple<String, Integer, Boolean>>() {});
		Assertions.assertEquals(hutoolTriple, pair);
	}

	@Test
	void tupleToBeanTest() {
		final Tuple hutoolTriple = Tuple.of("aaa", 123, true);
		final String a = JSONUtil.toJsonStr(hutoolTriple);
		final Tuple pair = JSONUtil.toBean(a, Tuple.class);
		Assertions.assertEquals(hutoolTriple, pair);
	}
}
