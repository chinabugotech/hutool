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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;

public class Issue2349Test {

	@Test
	public void issue11986ForJava17Test() {
		// https://github.com/apache/dubbo/issues/11986
		final ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

		// JDK9+ has been resolved JDK-8161372 bug, when cause dead then throw IllegalStateException
		Assertions.assertThrows(IllegalStateException.class, () -> map.computeIfAbsent("AaAa", key -> map.computeIfAbsent("BBBB", key2 -> 42)));
	}
}
