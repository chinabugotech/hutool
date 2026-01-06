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

package cn.hutool.v7.core.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 组合单元测试
 *
 * @author Looly
 *
 */
public class CombinationTest {

	@Test
	public void countTest() {
		long result = Combination.count(5, 2);
		assertEquals(10, result);

		result = Combination.count(5, 5);
		assertEquals(1, result);

		result = Combination.count(5, 0);
		assertEquals(1, result);

		final long resultAll = Combination.countAll(5);
		assertEquals(31, resultAll);
	}

	@Test
	public void selectTest() {
		final Combination combination = new Combination(new String[] { "1", "2", "3", "4", "5" });
		final List<String[]> list = combination.select(2);
		assertEquals(Combination.count(5, 2), list.size());

		Assertions.assertArrayEquals(new String[] {"1", "2"}, list.get(0));
		Assertions.assertArrayEquals(new String[] {"1", "3"}, list.get(1));
		Assertions.assertArrayEquals(new String[] {"1", "4"}, list.get(2));
		Assertions.assertArrayEquals(new String[] {"1", "5"}, list.get(3));
		Assertions.assertArrayEquals(new String[] {"2", "3"}, list.get(4));
		Assertions.assertArrayEquals(new String[] {"2", "4"}, list.get(5));
		Assertions.assertArrayEquals(new String[] {"2", "5"}, list.get(6));
		Assertions.assertArrayEquals(new String[] {"3", "4"}, list.get(7));
		Assertions.assertArrayEquals(new String[] {"3", "5"}, list.get(8));
		Assertions.assertArrayEquals(new String[] {"4", "5"}, list.get(9));

		final List<String[]> selectAll = combination.selectAll();
		assertEquals(Combination.countAll(5), selectAll.size());

		final List<String[]> list2 = combination.select(0);
		assertEquals(1, list2.size());
	}

	// -----------------------------
	// countBig() 正确性测试
	// -----------------------------
	@Test
	void testCountBig_basicCases() {
		assertEquals(BigInteger.ONE, Combination.countBig(5, 0));
		assertEquals(BigInteger.ONE, Combination.countBig(5, 5));
		assertEquals(BigInteger.valueOf(10), Combination.countBig(5, 3));
		assertEquals(BigInteger.valueOf(10), Combination.countBig(5, 2));
	}

	@Test
	void testCountBig_mGreaterThanN() {
		assertEquals(BigInteger.ZERO, Combination.countBig(5, 6));
	}

	@Test
	void testCountBig_negativeInput() {
		assertThrows(IllegalArgumentException.class, () -> Combination.countBig(-1, 3));
		assertThrows(IllegalArgumentException.class, () -> Combination.countBig(5, -2));
	}

	@Test
	void testCountBig_symmetry() {
		assertEquals(Combination.countBig(20, 3), Combination.countBig(20, 17));
	}

	@Test
	void testCountBig_largeNumbers() {
		// C(50, 3) = 19600
		assertEquals(new BigInteger("19600"), Combination.countBig(50, 3));

		// C(100, 50) 的确切值（重要测试）
		final BigInteger expected = new BigInteger(
			"100891344545564193334812497256"
		);
		assertEquals(expected, Combination.countBig(100, 50));
	}

	@Test
	void testCountBig_veryLargeCombination() {
		// 不比较具体值，只断言不要抛错
		final BigInteger result = Combination.countBig(2000, 1000);
		assertTrue(result.signum() > 0);
	}

	// -----------------------------
	// count(long) 兼容性测试
	// -----------------------------
	@Test
	void testCount_basic() {
		assertEquals(10L, Combination.count(5, 3));
		assertEquals(1L, Combination.count(5, 0));
		assertEquals(0L, Combination.count(5, 6));
	}

	// -----------------------------
	// countSafe() 安全 long 版本测试
	// -----------------------------
	@Test
	void testCountSafe_exactFitsLong() {
		// C(50, 3) = 19600 fits long
		assertEquals(19600L, Combination.count(50, 3));
	}

	@Test
	void testCountSafe_overflowThrows() {
		// C(100, 50) 超出 long → 应抛 ArithmeticException
		assertThrows(ArithmeticException.class, () -> Combination.count(100, 50));
	}

	@Test
	void testCountSafe_invalidInput() {
		assertThrows(IllegalArgumentException.class, () -> Combination.count(-1, 3));
		assertThrows(IllegalArgumentException.class, () -> Combination.count(3, -1));
	}
}
