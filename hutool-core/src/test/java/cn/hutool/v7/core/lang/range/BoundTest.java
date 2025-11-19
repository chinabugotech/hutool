/*
 * Copyright (c) 2013-2025 Hutool Team and hutool.cn
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

package cn.hutool.v7.core.lang.range;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test for {@link Bound}
 */
@SuppressWarnings("EqualsWithItself")
public class BoundTest {

	@Test
	@DisplayName("测试相邻区间合并")
	void testUnionIfIntersectedWithAdjacentRanges() {
		final BoundedRange<Integer> range1 = BoundedRange.close(1, 3);
		final BoundedRange<Integer> range2 = BoundedRange.close(3, 5);
		final BoundedRange<Integer> result = BoundedRangeOperation.unionIfIntersected(range1, range2);

		assertEquals(Bound.atLeast(1), result.getLowerBound());
		assertEquals(Bound.atMost(5), result.getUpperBound());
	}

	@Test
	void isDisjointTest(){
		final BoundedRange<Integer> range1 = BoundedRange.close(1, 3);
		final BoundedRange<Integer> range2 = BoundedRange.close(3, 5);
		// 点重合，相交
		assertFalse(range1.isDisjoint(range2));
	}

	@Test
	public void testEquals() {
		final Bound<Integer> bound = new FiniteBound<>(1, BoundType.OPEN_UPPER_BOUND);
		assertEquals(bound, bound);
		assertEquals(new FiniteBound<>(1, BoundType.OPEN_UPPER_BOUND), bound);
		assertNotEquals(new FiniteBound<>(2, BoundType.OPEN_UPPER_BOUND), bound);
		assertNotEquals(new FiniteBound<>(1, BoundType.OPEN_LOWER_BOUND), bound);
		assertNotEquals(null, bound);
	}

	@Test
	public void testHashCode() {
		final int hashCode = new FiniteBound<>(1, BoundType.OPEN_UPPER_BOUND).hashCode();
		assertEquals(hashCode, new FiniteBound<>(1, BoundType.OPEN_UPPER_BOUND).hashCode());
		assertNotEquals(hashCode, new FiniteBound<>(2, BoundType.OPEN_UPPER_BOUND).hashCode());
		assertNotEquals(hashCode, new FiniteBound<>(1, BoundType.OPEN_LOWER_BOUND).hashCode());
	}

	@Test
	public void testNoneLowerBound() {
		final Bound<Integer> bound = Bound.noneLowerBound();
		// negate
		assertEquals(bound, bound.negate());
		// test
		assertTrue(bound.test(Integer.MAX_VALUE));
		// getType
		assertEquals(BoundType.OPEN_LOWER_BOUND, bound.getType());
		// getValue
		assertNull(bound.getValue());
		// toString
		assertEquals("(" + "-∞", bound.descBound());
		// compareTo
		assertEquals(0, bound.compareTo(bound));
		assertEquals(-1, bound.compareTo(Bound.atMost(1)));

		assertEquals(BoundedRange.all(), bound.toRange());
		assertEquals("{x | x > -∞}", bound.toString());
	}

	@Test
	public void testNoneUpperBound() {
		final Bound<Integer> bound = Bound.noneUpperBound();
		// negate
		assertEquals(bound, bound.negate());
		// test
		assertTrue(bound.test(Integer.MAX_VALUE));
		// getType
		assertEquals(BoundType.OPEN_UPPER_BOUND, bound.getType());
		// getValue
		assertNull(bound.getValue());
		// toString
		assertEquals("+∞" + ")", bound.descBound());
		// compareTo
		assertEquals(0, bound.compareTo(bound));
		assertEquals(1, bound.compareTo(Bound.atMost(1)));

		assertEquals(BoundedRange.all(), bound.toRange());
		assertEquals("{x | x < +∞}", bound.toString());
	}

	@Test
	public void testGreatThan() {
		// { x | x > 0}
		Bound<Integer> bound = Bound.greaterThan(0);

		// test
		assertTrue(bound.test(1));
		assertFalse(bound.test(0));
		assertFalse(bound.test(-1));
		// getType
		assertEquals(BoundType.OPEN_LOWER_BOUND, bound.getType());
		// getValue
		assertEquals((Integer)0, bound.getValue());
		// toString
		assertEquals("(0", bound.descBound());
		assertEquals("{x | x > 0}", bound.toString());

		// compareTo
		assertEquals(0, bound.compareTo(bound));
		assertEquals(-1, bound.compareTo(Bound.noneUpperBound()));
		assertEquals(1, bound.compareTo(Bound.atLeast(-1)));
		assertEquals(-1, bound.compareTo(Bound.atLeast(2)));
		assertEquals(1, bound.compareTo(Bound.lessThan(0)));
		assertEquals(1, bound.compareTo(Bound.atMost(0)));
		assertEquals(-1, bound.compareTo(Bound.atMost(2)));
		assertEquals(1, bound.compareTo(Bound.noneLowerBound()));

		// { x | x >= 0}
		bound = bound.negate();
		assertEquals((Integer)0, bound.getValue());
		assertEquals(BoundType.CLOSE_UPPER_BOUND, bound.getType());

		assertNotNull(bound.toRange());
	}

	@Test
	public void testAtLeast() {
		// { x | x >= 0}
		Bound<Integer> bound = Bound.atLeast(0);

		// test
		assertTrue(bound.test(1));
		assertTrue(bound.test(0));
		assertFalse(bound.test(-1));
		// getType
		assertEquals(BoundType.CLOSE_LOWER_BOUND, bound.getType());
		// getValue
		assertEquals((Integer)0, bound.getValue());
		// toString
		assertEquals("[0", bound.descBound());
		assertEquals("{x | x >= 0}", bound.toString());

		// compareTo
		assertEquals(0, bound.compareTo(bound));
		assertEquals(-1, bound.compareTo(Bound.noneUpperBound()));
		assertEquals(1, bound.compareTo(Bound.greaterThan(-1)));
		assertEquals(-1, bound.compareTo(Bound.greaterThan(0)));
		assertEquals(1, bound.compareTo(Bound.lessThan(0)));
		// pr#1385@gitee 特殊情况：右闭区间与左闭区间在同一点时，认为这个边界相等
		assertEquals(0, bound.compareTo(Bound.atMost(0)));
		assertEquals(-1, bound.compareTo(Bound.atMost(2)));
		assertEquals(1, bound.compareTo(Bound.noneLowerBound()));

		// { x | x < 0}
		bound = bound.negate();
		assertEquals((Integer)0, bound.getValue());
		assertEquals(BoundType.OPEN_UPPER_BOUND, bound.getType());

		assertNotNull(bound.toRange());
	}

	@Test
	public void testLessThan() {
		// { x | x < 0}
		Bound<Integer> bound = Bound.lessThan(0);

		// test
		assertFalse(bound.test(1));
		assertFalse(bound.test(0));
		assertTrue(bound.test(-1));
		// getType
		assertEquals(BoundType.OPEN_UPPER_BOUND, bound.getType());
		// getValue
		assertEquals((Integer)0, bound.getValue());
		// toString
		assertEquals("0)", bound.descBound());
		assertEquals("{x | x < 0}", bound.toString());

		// compareTo
		assertEquals(0, bound.compareTo(bound));
		assertEquals(-1, bound.compareTo(Bound.noneUpperBound()));
		assertEquals(1, bound.compareTo(Bound.greaterThan(-1)));
		assertEquals(-1, bound.compareTo(Bound.greaterThan(0)));
		assertEquals(1, bound.compareTo(Bound.lessThan(-1)));
		assertEquals(-1, bound.compareTo(Bound.atMost(0)));
		assertEquals(1, bound.compareTo(Bound.atMost(-1)));
		assertEquals(1, bound.compareTo(Bound.noneLowerBound()));

		// { x | x >= 0}
		bound = bound.negate();
		assertEquals((Integer)0, bound.getValue());
		assertEquals(BoundType.CLOSE_LOWER_BOUND, bound.getType());

		assertNotNull(bound.toRange());
	}

	@Test
	public void testAtMost() {
		// { x | x <= 0}
		Bound<Integer> bound = Bound.atMost(0);

		// test
		assertFalse(bound.test(1));
		assertTrue(bound.test(0));
		assertTrue(bound.test(-1));
		// getType
		assertEquals(BoundType.CLOSE_UPPER_BOUND, bound.getType());
		// getValue
		assertEquals((Integer)0, bound.getValue());
		// toString
		assertEquals("0]", bound.descBound());
		assertEquals("{x | x <= 0}", bound.toString());

		// compareTo
		assertEquals(0, bound.compareTo(bound));
		assertEquals(-1, bound.compareTo(Bound.noneUpperBound()));
		assertEquals(1, bound.compareTo(Bound.greaterThan(-1)));
		assertEquals(-1, bound.compareTo(Bound.greaterThan(0)));
		assertEquals(1, bound.compareTo(Bound.atMost(-1)));
		assertEquals(1, bound.compareTo(Bound.lessThan(0)));
		assertEquals(1, bound.compareTo(Bound.lessThan(-1)));
		assertEquals(1, bound.compareTo(Bound.noneLowerBound()));

		// { x | x > 0}
		bound = bound.negate();
		assertEquals((Integer)0, bound.getValue());
		assertEquals(BoundType.OPEN_LOWER_BOUND, bound.getType());

		assertNotNull(bound.toRange());
	}

}
