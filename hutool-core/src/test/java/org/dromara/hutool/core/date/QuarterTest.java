package org.dromara.hutool.core.date;

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

		assertThrows(IllegalArgumentException.class, () -> {
			Quarter.valueOf("Abc");
		});

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
		assertEquals(4, quarter.getValue());
		assertEquals("Q4", quarter.name());

		assertThrows(IllegalArgumentException.class, () -> {
			Quarter.valueOf("Q5");
		});

		final Month firstMonth = quarter.firstMonth();
		assertEquals(Month.OCTOBER, firstMonth);

		final Month lastMonth = quarter.lastMonth();
		assertEquals(Month.DECEMBER, lastMonth);
	}

}
