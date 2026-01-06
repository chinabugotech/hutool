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

package cn.hutool.v7.core.date;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class QuarterTest {
	@Test
	void testQ1() {
		final Quarter quarter = Quarter.of(1);
		assertSame(Quarter.Q1, quarter);
		assertSame(quarter, Quarter.valueOf("Q1"));
		assertEquals(1, Objects.requireNonNull(quarter).getValue());
		assertEquals("Q1", quarter.name());

		assertNull(Quarter.of(0));

		final Month firstMonth = quarter.firstMonth();
		assertEquals(Month.JANUARY, firstMonth);

		final Month lastMonth = quarter.lastMonth();
		assertEquals(Month.MARCH, lastMonth);
	}

	@Test
	void testQ2() {
		final Quarter quarter = Quarter.of(2);
		assertSame(Quarter.Q2, quarter);
		assertSame(quarter, Quarter.valueOf("Q2"));
		assertEquals(2, Objects.requireNonNull(quarter).getValue());
		assertEquals("Q2", quarter.name());

		assertNull(Quarter.of(5));

		// ==========

		final Month firstMonth = quarter.firstMonth();
		assertEquals(Month.APRIL, firstMonth);

		// ==========

		final Month lastMonth = quarter.lastMonth();
		assertEquals(Month.JUNE, lastMonth);
	}

	@Test
	void testQ3() {
		final Quarter quarter = Quarter.of(3);
		assertSame(Quarter.Q3, quarter);
		assertSame(quarter, Quarter.valueOf("Q3"));
		assertEquals(3, Objects.requireNonNull(quarter).getValue());
		assertEquals("Q3", quarter.name());

		assertThrows(IllegalArgumentException.class, () -> Quarter.valueOf("Abc"));

		final Month firstMonth = quarter.firstMonth();
		assertEquals(Month.JULY, firstMonth);

		final Month lastMonth = quarter.lastMonth();
		assertEquals(Month.SEPTEMBER, lastMonth);
	}

	@Test
	void testQ4() {
		final Quarter quarter = Quarter.of(4);
		assertSame(Quarter.Q4, quarter);
		assertSame(quarter, Quarter.valueOf("Q4"));
		assertEquals(4, Objects.requireNonNull(quarter).getValue());
		assertEquals("Q4", quarter.name());

		assertThrows(IllegalArgumentException.class, () -> Quarter.valueOf("Q5"));

		final Month firstMonth = quarter.firstMonth();
		assertEquals(Month.OCTOBER, firstMonth);

		final Month lastMonth = quarter.lastMonth();
		assertEquals(Month.DECEMBER, lastMonth);
	}

}
