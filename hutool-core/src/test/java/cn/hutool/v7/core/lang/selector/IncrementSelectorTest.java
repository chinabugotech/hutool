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

package cn.hutool.v7.core.lang.selector;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IncrementSelectorTest {
	/**
	 * 测试正常情况下循环选取元素
	 */
	@Test
	void testSelectNormalCycle() {
		final IncrementSelector<String> selector = new IncrementSelector<>(List.of("A", "B", "C"));

		assertEquals("A", selector.select()); // 第一次调用应返回第一个元素
		assertEquals("B", selector.select()); // 第二次调用应返回第二个元素
		assertEquals("C", selector.select()); // 第三次调用应返回第三个元素
		assertEquals("A", selector.select()); // 循环回第一个元素
	}
}
