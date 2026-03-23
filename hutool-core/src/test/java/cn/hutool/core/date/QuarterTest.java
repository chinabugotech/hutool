package cn.hutool.core.date;

import org.junit.jupiter.api.Test;

import java.time.MonthDay;

import static org.junit.jupiter.api.Assertions.*;

public class QuarterTest {

	@Test
	void testQ1() {
		Quarter quarter = Quarter.of(1);
		assertSame(Quarter.Q1, quarter);
		assertSame(quarter, Quarter.valueOf("Q1"));
		assertEquals(1, quarter.getValue());
		assertEquals("Q1", quarter.name());

		assertNull(Quarter.of(0));

		// ==========

		int firstMonthValue = quarter.firstMonthValue();
		assertEquals(1, firstMonthValue);

		Month firstMonth = quarter.firstMonth();
		assertEquals(Month.JANUARY, firstMonth);

		// ==========

		int lastMonthValue = quarter.lastMonthValue();
		assertEquals(3, lastMonthValue);

		Month lastMonth = quarter.lastMonth();
		assertEquals(Month.MARCH, lastMonth);

		// ==========

		MonthDay firstMonthDay = quarter.firstMonthDay();
		assertEquals(firstMonthDay, MonthDay.of(1, 1));

		MonthDay lastMonthDay = quarter.lastMonthDay();
		assertEquals(lastMonthDay, MonthDay.of(3, 31));
	}

	@Test
	void testQ2() {
		Quarter quarter = Quarter.of(2);
		assertSame(Quarter.Q2, quarter);
		assertSame(quarter, Quarter.valueOf("Q2"));
		assertEquals(2, quarter.getValue());
		assertEquals("Q2", quarter.name());

		assertNull(Quarter.of(5));

		// ==========

		int firstMonthValue = quarter.firstMonthValue();
		assertEquals(4, firstMonthValue);

		Month firstMonth = quarter.firstMonth();
		assertEquals(Month.APRIL, firstMonth);

		// ==========

		int lastMonthValue = quarter.lastMonthValue();
		assertEquals(6, lastMonthValue);

		Month lastMonth = quarter.lastMonth();
		assertEquals(Month.JUNE, lastMonth);

		// ==========

		MonthDay firstMonthDay = quarter.firstMonthDay();
		assertEquals(firstMonthDay, MonthDay.of(4, 1));

		MonthDay lastMonthDay = quarter.lastMonthDay();
		assertEquals(lastMonthDay, MonthDay.of(6, 30));
	}

	@Test
	void testQ3() {
		Quarter quarter = Quarter.of(3);
		assertSame(Quarter.Q3, quarter);
		assertSame(quarter, Quarter.valueOf("Q3"));
		assertEquals(3, quarter.getValue());
		assertEquals("Q3", quarter.name());

		assertThrows(IllegalArgumentException.class, () -> {
			Quarter.valueOf("Abc");
		});

		// ==========

		int firstMonthValue = quarter.firstMonthValue();
		assertEquals(7, firstMonthValue);

		Month firstMonth = quarter.firstMonth();
		assertEquals(Month.JULY, firstMonth);

		// ==========

		int lastMonthValue = quarter.lastMonthValue();
		assertEquals(9, lastMonthValue);

		Month lastMonth = quarter.lastMonth();
		assertEquals(Month.SEPTEMBER, lastMonth);

		// ==========

		MonthDay firstMonthDay = quarter.firstMonthDay();
		assertEquals(firstMonthDay, MonthDay.of(7, 1));

		MonthDay lastMonthDay = quarter.lastMonthDay();
		assertEquals(lastMonthDay, MonthDay.of(9, 30));
	}

	@Test
	void testQ4() {
		Quarter quarter = Quarter.of(4);
		assertSame(Quarter.Q4, quarter);
		assertSame(quarter, Quarter.valueOf("Q4"));
		assertEquals(4, quarter.getValue());
		assertEquals("Q4", quarter.name());

		assertThrows(IllegalArgumentException.class, () -> {
			Quarter.valueOf("Q5");
		});

		// ==========

		int firstMonthValue = quarter.firstMonthValue();
		assertEquals(10, firstMonthValue);

		Month firstMonth = quarter.firstMonth();
		assertEquals(Month.OCTOBER, firstMonth);

		// ==========

		int lastMonthValue = quarter.lastMonthValue();
		assertEquals(12, lastMonthValue);
		Month lastMonth = quarter.lastMonth();
		assertEquals(Month.DECEMBER, lastMonth);

		// ==========

		MonthDay firstMonthDay = quarter.firstMonthDay();
		assertEquals(firstMonthDay, MonthDay.of(10, 1));

		MonthDay lastMonthDay = quarter.lastMonthDay();
		assertEquals(lastMonthDay, MonthDay.of(12, 31));
	}

	@Test
	void testPlusZeroAndPositiveRealNumbers() {
		for (int i = 0; i < 100; i += 4) {
			assertEquals(Quarter.Q1, Quarter.Q1.plus(i));
			assertEquals(Quarter.Q2, Quarter.Q2.plus(i));
			assertEquals(Quarter.Q3, Quarter.Q3.plus(i));
			assertEquals(Quarter.Q4, Quarter.Q4.plus(i));
		}
		for (int i = 1; i < 100 + 1; i += 4) {
			assertEquals(Quarter.Q2, Quarter.Q1.plus(i));
			assertEquals(Quarter.Q3, Quarter.Q2.plus(i));
			assertEquals(Quarter.Q4, Quarter.Q3.plus(i));
			assertEquals(Quarter.Q1, Quarter.Q4.plus(i));
		}
		for (int i = 2; i < 100 + 2; i += 4) {
			assertEquals(Quarter.Q3, Quarter.Q1.plus(i));
			assertEquals(Quarter.Q4, Quarter.Q2.plus(i));
			assertEquals(Quarter.Q1, Quarter.Q3.plus(i));
			assertEquals(Quarter.Q2, Quarter.Q4.plus(i));
		}
		for (int i = 3; i < 100 + 3; i += 4) {
			assertEquals(Quarter.Q4, Quarter.Q1.plus(i));
			assertEquals(Quarter.Q1, Quarter.Q2.plus(i));
			assertEquals(Quarter.Q2, Quarter.Q3.plus(i));
			assertEquals(Quarter.Q3, Quarter.Q4.plus(i));
		}
	}

	@Test
	void testPlusZeroAndNegativeNumber() {
		for (int i = 0; i > -100; i -= 4) {
			assertEquals(Quarter.Q1, Quarter.Q1.plus(i));
			assertEquals(Quarter.Q2, Quarter.Q2.plus(i));
			assertEquals(Quarter.Q3, Quarter.Q3.plus(i));
			assertEquals(Quarter.Q4, Quarter.Q4.plus(i));
		}
		for (int i = -1; i > -(100 + 1); i -= 4) {
			assertEquals(Quarter.Q4, Quarter.Q1.plus(i));
			assertEquals(Quarter.Q1, Quarter.Q2.plus(i));
			assertEquals(Quarter.Q2, Quarter.Q3.plus(i));
			assertEquals(Quarter.Q3, Quarter.Q4.plus(i));
		}
		for (int i = -2; i > -(100 + 2); i -= 4) {
			assertEquals(Quarter.Q3, Quarter.Q1.plus(i));
			assertEquals(Quarter.Q4, Quarter.Q2.plus(i));
			assertEquals(Quarter.Q1, Quarter.Q3.plus(i));
			assertEquals(Quarter.Q2, Quarter.Q4.plus(i));
		}
		for (int i = -3; i > -(100 + 3); i -= 4) {
			assertEquals(Quarter.Q2, Quarter.Q1.plus(i));
			assertEquals(Quarter.Q3, Quarter.Q2.plus(i));
			assertEquals(Quarter.Q4, Quarter.Q3.plus(i));
			assertEquals(Quarter.Q1, Quarter.Q4.plus(i));
		}
	}
}
