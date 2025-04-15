/*
 * Copyright (c) 2025 Hutool Team and hutool.cn
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

package cn.hutool.v7.core.array;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IssueIAQ16ETest {

	@Test
	void lastIndexOfSubTest() {
		final Integer[] bigBytes = new Integer[]{1, 2, 2, 2, 3, 2, 2, 2, 3};
		final Integer[] subBytes = new Integer[]{2, 2};
		int i = ArrayUtil.lastIndexOfSub(bigBytes, subBytes);
		assertEquals(6, i);

		i = ArrayUtil.lastIndexOfSub(bigBytes, 3, subBytes);
		assertEquals(2, i);
	}

	@Test
	void lastIndexOfSubTest2() {
		final Integer[] bigBytes = new Integer[]{1, 2, 2, 2, 3, 2, 2, 2, 3, 4, 5};
		final Integer[] subBytes = new Integer[]{2, 2, 2, 3};
		final int i = ArrayUtil.lastIndexOfSub(bigBytes, subBytes);
		assertEquals(5, i);
	}

	@Test
	void indexOfSubTest() {
		final Integer[] bigBytes = new Integer[]{1, 2, 2, 2, 3, 2, 2, 2, 3};
		final Integer[] subBytes = new Integer[]{2, 2};
		int i = ArrayUtil.indexOfSub(bigBytes, subBytes);
		assertEquals(1, i);

		i = ArrayUtil.indexOfSub(bigBytes, 3, subBytes);
		assertEquals(5, i);
	}

	@Test
	void indexOfSubTest2() {
		final Integer[] bigBytes = new Integer[]{1, 2, 2, 2, 3, 2, 2, 2, 3, 4, 5};
		final Integer[] subBytes = new Integer[]{2, 2, 2, 3};
		final int i = ArrayUtil.indexOfSub(bigBytes, subBytes);
		assertEquals(1, i);
	}
}
