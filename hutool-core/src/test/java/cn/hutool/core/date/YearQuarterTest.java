package cn.hutool.core.date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class YearQuarterTest {

	// ================================
	// #region - of(int year, int quarter)
	// ================================

	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 3, 4 })
	void of_ValidYearAndQuarterValue_CreatesYearQuarter(int quarter) {
		{
			int year = 2024;
			YearQuarter yearQuarter = YearQuarter.of(year, quarter);
			assertEquals(year, yearQuarter.getYear());
			assertEquals(Quarter.of(quarter), yearQuarter.getQuarter());
			assertEquals(quarter, yearQuarter.getQuarterValue());
		}
		{
			int year = Year.MIN_VALUE;
			YearQuarter yearQuarter = YearQuarter.of(year, quarter);
			assertEquals(year, yearQuarter.getYear());
			assertEquals(Quarter.of(quarter), yearQuarter.getQuarter());
			assertEquals(quarter, yearQuarter.getQuarterValue());
		}
		{
			int year = Year.MAX_VALUE;
			YearQuarter yearQuarter = YearQuarter.of(year, quarter);
			assertEquals(year, yearQuarter.getYear());
			assertEquals(Quarter.of(quarter), yearQuarter.getQuarter());
			assertEquals(quarter, yearQuarter.getQuarterValue());
		}
	}

	@ParameterizedTest
	@ValueSource(ints = { -1, 0, 5, 108 })
	void of_ValidYearAndInvalidQuarterValue_DateTimeException(int quarter) {
		int year = 2024;
		assertThrows(DateTimeException.class, () -> {
			YearQuarter.of(year, quarter);
		});
	}

	@ParameterizedTest
	@ValueSource(ints = { Year.MIN_VALUE - 1, Year.MAX_VALUE + 1 })
	void of_InvalidYearAndValidQuarterValue_DateTimeException(int year) {
		assertThrows(DateTimeException.class, () -> {
			YearQuarter.of(year, 1);
		});
		assertThrows(DateTimeException.class, () -> {
			YearQuarter.of(year, 2);
		});
		assertThrows(DateTimeException.class, () -> {
			YearQuarter.of(year, 3);
		});
		assertThrows(DateTimeException.class, () -> {
			YearQuarter.of(year, 4);
		});
	}

	@Test
	void of_InvalidYearAndInvalidQuarterValue_DateTimeException() {
		final int[] years = { Year.MIN_VALUE - 1, Year.MAX_VALUE + 1 };
		final int[] quarters = { -1, 0, 5, 108 };
		for (int year : years) {
			final int yearValue = year;
			for (int quarter : quarters) {
				final int quarterValue = quarter;
				assertThrows(DateTimeException.class,
						() -> YearQuarter.of(yearValue, quarterValue));
			}
		}
	}

	// ================================
	// #endregion - of(int year, int quarter)
	// ================================

	// ================================
	// #region - of(int year, Quarter quarter)
	// ================================

	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 3, 4 })
	void of_ValidYearAndQuarter_CreatesYearQuarter(int quarterValue) {
		{
			int year = 2024;
			Quarter quarter = Quarter.of(quarterValue);
			YearQuarter yearQuarter = YearQuarter.of(year, quarter);
			assertEquals(year, yearQuarter.getYear());
			assertEquals(quarter, yearQuarter.getQuarter());
			assertEquals(quarterValue, yearQuarter.getQuarterValue());
		}
		{
			int year = Year.MIN_VALUE;
			Quarter quarter = Quarter.of(quarterValue);
			YearQuarter yearQuarter = YearQuarter.of(year, quarter);
			assertEquals(year, yearQuarter.getYear());
			assertEquals(quarter, yearQuarter.getQuarter());
			assertEquals(quarterValue, yearQuarter.getQuarterValue());
		}
		{
			int year = Year.MAX_VALUE;
			Quarter quarter = Quarter.of(quarterValue);
			YearQuarter yearQuarter = YearQuarter.of(year, quarter);
			assertEquals(year, yearQuarter.getYear());
			assertEquals(quarter, yearQuarter.getQuarter());
			assertEquals(quarterValue, yearQuarter.getQuarterValue());
		}
	}

	@Test
	void of_ValidYearAndNullQuarter_NullPointerException() {
		int year = 2024;
		assertThrows(NullPointerException.class, () -> {
			YearQuarter.of(year, null);
		});
	}

	@ParameterizedTest
	@ValueSource(ints = { Year.MIN_VALUE - 1, Year.MAX_VALUE + 1 })
	void of_InvalidYearAndValidQuarter_DateTimeException(int year) {
		assertThrows(DateTimeException.class, () -> {
			YearQuarter.of(year, Quarter.Q1);
		});
		assertThrows(DateTimeException.class, () -> {
			YearQuarter.of(year, Quarter.Q2);
		});
		assertThrows(DateTimeException.class, () -> {
			YearQuarter.of(year, Quarter.Q3);
		});
		assertThrows(DateTimeException.class, () -> {
			YearQuarter.of(year, Quarter.Q4);
		});
	}

	@Test
	void of_InvalidYearAndNullQuarter_DateTimeException() {
		final int[] years = { Year.MIN_VALUE - 1, Year.MAX_VALUE + 1 };
		for (int year : years) {
			final int yearValue = year;
			assertThrows(DateTimeException.class,
					() -> YearQuarter.of(yearValue, null));

		}
	}

	// ================================
	// #endregion - of(int year, Quarter quarter)
	// ================================

	// ================================
	// #region - of(LocalDate date)
	// ================================

	@ParameterizedTest
	@ValueSource(ints = {
			2023, // 非闰年
			2024, // 闰年
			Year.MIN_VALUE,
			Year.MAX_VALUE,
	})
	void of_ValidLocalDate_CreatesYearQuarter_Q1(int year) {
		{
			LocalDate date = YearMonth.of(year, 1).atDay(1);
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(1, yq.getQuarterValue());
			assertSame(Quarter.Q1, yq.getQuarter());
		}
		{
			LocalDate date = YearMonth.of(year, 1).atEndOfMonth();
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(1, yq.getQuarterValue());
			assertSame(Quarter.Q1, yq.getQuarter());
		}
		{
			LocalDate date = YearMonth.of(year, 2).atDay(1);
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(1, yq.getQuarterValue());
			assertSame(Quarter.Q1, yq.getQuarter());
		}
		{
			LocalDate date = YearMonth.of(year, 2).atEndOfMonth();
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(1, yq.getQuarterValue());
			assertSame(Quarter.Q1, yq.getQuarter());
		}
		{
			LocalDate date = YearMonth.of(year, 3).atDay(1);
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(1, yq.getQuarterValue());
			assertSame(Quarter.Q1, yq.getQuarter());
		}
		{
			LocalDate date = YearMonth.of(year, 3).atEndOfMonth();
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(1, yq.getQuarterValue());
			assertSame(Quarter.Q1, yq.getQuarter());
		}
	}

	@ParameterizedTest
	@ValueSource(ints = {
			2023, // 非闰年
			2024, // 闰年
			Year.MIN_VALUE,
			Year.MAX_VALUE,
	})
	void of_ValidLocalDate_CreatesYearQuarter_Q2(int year) {
		{
			LocalDate date = YearMonth.of(year, 4).atDay(1);
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(2, yq.getQuarterValue());
			assertSame(Quarter.Q2, yq.getQuarter());
		}
		{
			LocalDate date = YearMonth.of(year, 4).atEndOfMonth();
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(2, yq.getQuarterValue());
			assertSame(Quarter.Q2, yq.getQuarter());
		}
		{
			LocalDate date = YearMonth.of(year, 5).atDay(1);
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(2, yq.getQuarterValue());
			assertSame(Quarter.Q2, yq.getQuarter());
		}
		{
			LocalDate date = YearMonth.of(year, 5).atEndOfMonth();
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(2, yq.getQuarterValue());
			assertSame(Quarter.Q2, yq.getQuarter());
		}
		{
			LocalDate date = YearMonth.of(year, 6).atDay(1);
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(2, yq.getQuarterValue());
			assertSame(Quarter.Q2, yq.getQuarter());
		}
		{
			LocalDate date = YearMonth.of(year, 6).atEndOfMonth();
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(2, yq.getQuarterValue());
			assertSame(Quarter.Q2, yq.getQuarter());
		}
	}

	@ParameterizedTest
	@ValueSource(ints = {
			2023, // 非闰年
			2024, // 闰年
			Year.MIN_VALUE,
			Year.MAX_VALUE,
	})
	void of_ValidLocalDate_CreatesYearQuarter_Q3(int year) {
		{
			LocalDate date = YearMonth.of(year, 7).atDay(1);
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(3, yq.getQuarterValue());
			assertSame(Quarter.Q3, yq.getQuarter());
		}
		{
			LocalDate date = YearMonth.of(year, 7).atEndOfMonth();
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(3, yq.getQuarterValue());
			assertSame(Quarter.Q3, yq.getQuarter());
		}
		{
			LocalDate date = YearMonth.of(year, 8).atDay(1);
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(3, yq.getQuarterValue());
			assertSame(Quarter.Q3, yq.getQuarter());
		}
		{
			LocalDate date = YearMonth.of(year, 8).atEndOfMonth();
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(3, yq.getQuarterValue());
			assertSame(Quarter.Q3, yq.getQuarter());
		}
		{
			LocalDate date = YearMonth.of(year, 9).atDay(1);
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(3, yq.getQuarterValue());
			assertSame(Quarter.Q3, yq.getQuarter());
		}
		{
			LocalDate date = YearMonth.of(year, 9).atEndOfMonth();
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(3, yq.getQuarterValue());
			assertSame(Quarter.Q3, yq.getQuarter());
		}
	}

	@ParameterizedTest
	@ValueSource(ints = {
			2023, // 非闰年
			2024, // 闰年
			Year.MIN_VALUE,
			Year.MAX_VALUE,
	})
	void of_ValidLocalDate_CreatesYearQuarter_Q4(int year) {
		{
			LocalDate date = YearMonth.of(year, 10).atDay(1);
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(4, yq.getQuarterValue());
			assertSame(Quarter.Q4, yq.getQuarter());
		}
		{
			LocalDate date = YearMonth.of(year, 10).atEndOfMonth();
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(4, yq.getQuarterValue());
			assertSame(Quarter.Q4, yq.getQuarter());
		}
		{
			LocalDate date = YearMonth.of(year, 11).atDay(1);
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(4, yq.getQuarterValue());
			assertSame(Quarter.Q4, yq.getQuarter());
		}
		{
			LocalDate date = YearMonth.of(year, 11).atEndOfMonth();
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(4, yq.getQuarterValue());
			assertSame(Quarter.Q4, yq.getQuarter());
		}
		{
			LocalDate date = YearMonth.of(year, 12).atDay(1);
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(4, yq.getQuarterValue());
			assertSame(Quarter.Q4, yq.getQuarter());
		}
		{
			LocalDate date = YearMonth.of(year, 12).atEndOfMonth();
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(4, yq.getQuarterValue());
			assertSame(Quarter.Q4, yq.getQuarter());
		}
	}

	@Test
	void of_NullLocalDate_NullPointerException() {
		LocalDate date = null;
		assertThrows(NullPointerException.class, () -> {
			YearQuarter.of(date);
		});
	}

	// ================================
	// #endregion - of(LocalDate date)
	// ================================

	// ================================
	// #region - of(Date date)
	// ================================

	@SuppressWarnings("deprecation")
	@ParameterizedTest
	@ValueSource(ints = {
			2023, // 非闰年
			2024, // 闰年
			1,
			999999,
	})
	void of_ValidDate_CreatesYearQuarter(int year) {
		{
			Date date = new Date(year - 1900, 1 - 1, 1);
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(1, yq.getQuarterValue());
			assertSame(Quarter.Q1, yq.getQuarter());
		}
		{
			Date date = new Date(year - 1900, 3 - 1, 31, 23, 59, 59);
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(1, yq.getQuarterValue());
			assertSame(Quarter.Q1, yq.getQuarter());
		}
		{
			Date date = new Date(year - 1900, 4 - 1, 1);
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(2, yq.getQuarterValue());
			assertSame(Quarter.Q2, yq.getQuarter());
		}
		{
			Date date = new Date(year - 1900, 6 - 1, 30, 23, 59, 59);
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(2, yq.getQuarterValue());
			assertSame(Quarter.Q2, yq.getQuarter());
		}
		{
			Date date = new Date(year - 1900, 7 - 1, 1);
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(3, yq.getQuarterValue());
			assertSame(Quarter.Q3, yq.getQuarter());
		}
		{
			Date date = new Date(year - 1900, 9 - 1, 30, 23, 59, 59);
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(3, yq.getQuarterValue());
			assertSame(Quarter.Q3, yq.getQuarter());
		}
		{
			Date date = new Date(year - 1900, 10 - 1, 1);
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(4, yq.getQuarterValue());
			assertSame(Quarter.Q4, yq.getQuarter());
		}
		{
			Date date = new Date(year - 1900, 12 - 1, 31, 23, 59, 59);
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(4, yq.getQuarterValue());
			assertSame(Quarter.Q4, yq.getQuarter());
		}
	}

	@Test
	void of_NullDate_NullPointerException() {
		Date date = null;
		assertThrows(NullPointerException.class, () -> {
			YearQuarter.of(date);
		});
	}

	// ================================
	// #endregion - of(Date date)
	// ================================

	// ================================
	// #region - of(Calendar date)
	// ================================

	@ParameterizedTest
	@ValueSource(ints = {
			2023, // 非闰年
			2024, // 闰年
			1,
			999999,
	})
	void of_ValidCalendar_CreatesYearQuarter(int year) {
		Calendar date = Calendar.getInstance();
		{
			date.set(year, 1 - 1, 1);
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(1, yq.getQuarterValue());
			assertSame(Quarter.Q1, yq.getQuarter());
		}
		{
			date.set(year, 3 - 1, 31, 23, 59, 59);
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(1, yq.getQuarterValue());
			assertSame(Quarter.Q1, yq.getQuarter());
		}
		{
			date.set(year, 4 - 1, 1);
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(2, yq.getQuarterValue());
			assertSame(Quarter.Q2, yq.getQuarter());
		}
		{
			date.set(year, 6 - 1, 30, 23, 59, 59);
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(2, yq.getQuarterValue());
			assertSame(Quarter.Q2, yq.getQuarter());
		}
		{
			date.set(year, 7 - 1, 1);
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(3, yq.getQuarterValue());
			assertSame(Quarter.Q3, yq.getQuarter());
		}
		{
			date.set(year, 9 - 1, 30, 23, 59, 59);
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(3, yq.getQuarterValue());
			assertSame(Quarter.Q3, yq.getQuarter());
		}
		{
			date.set(year, 10 - 1, 1);
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(4, yq.getQuarterValue());
			assertSame(Quarter.Q4, yq.getQuarter());
		}
		{
			date.set(year, 12 - 1, 31, 23, 59, 59);
			YearQuarter yq = YearQuarter.of(date);
			assertEquals(year, yq.getYear());
			assertEquals(4, yq.getQuarterValue());
			assertSame(Quarter.Q4, yq.getQuarter());
		}
	}

	@Test
	void of_NullCalendar_NullPointerException() {
		Calendar date = null;
		assertThrows(NullPointerException.class, () -> {
			YearQuarter.of(date);
		});
	}

	// ================================
	// #endregion - of(Calendar date)
	// ================================

	// ================================
	// #region - of(YearMonth yearMonth)
	// ================================

	@ParameterizedTest
	@ValueSource(ints = {
			2023, // 非闰年
			2024, // 闰年
			Year.MIN_VALUE,
			Year.MAX_VALUE,
	})
	void of_ValidYearMonth_CreatesYearMonth_Q1(int year) {
		{
			YearMonth yearMonth = YearMonth.of(year, 1);
			YearQuarter yearQuarter = YearQuarter.of(yearMonth);
			assertEquals(year, yearQuarter.getYear());
			assertEquals(1, yearQuarter.getQuarterValue());
			assertSame(Quarter.Q1, yearQuarter.getQuarter());
		}
		{
			YearMonth yearMonth = YearMonth.of(year, 2);
			YearQuarter yearQuarter = YearQuarter.of(yearMonth);
			assertEquals(year, yearQuarter.getYear());
			assertEquals(1, yearQuarter.getQuarterValue());
			assertSame(Quarter.Q1, yearQuarter.getQuarter());
		}
		{
			YearMonth yearMonth = YearMonth.of(year, 3);
			YearQuarter yearQuarter = YearQuarter.of(yearMonth);
			assertEquals(year, yearQuarter.getYear());
			assertEquals(1, yearQuarter.getQuarterValue());
			assertSame(Quarter.Q1, yearQuarter.getQuarter());
		}
	}

	@ParameterizedTest
	@ValueSource(ints = {
			2023, // 非闰年
			2024, // 闰年
			Year.MIN_VALUE,
			Year.MAX_VALUE,
	})
	void of_ValidYearMonth_CreatesYearMonth_Q2(int year) {
		{
			YearMonth yearMonth = YearMonth.of(year, 4);
			YearQuarter yearQuarter = YearQuarter.of(yearMonth);
			assertEquals(year, yearQuarter.getYear());
			assertEquals(2, yearQuarter.getQuarterValue());
			assertSame(Quarter.Q2, yearQuarter.getQuarter());
		}
		{
			YearMonth yearMonth = YearMonth.of(year, 5);
			YearQuarter yearQuarter = YearQuarter.of(yearMonth);
			assertEquals(year, yearQuarter.getYear());
			assertEquals(2, yearQuarter.getQuarterValue());
			assertSame(Quarter.Q2, yearQuarter.getQuarter());
		}
		{
			YearMonth yearMonth = YearMonth.of(year, 6);
			YearQuarter yearQuarter = YearQuarter.of(yearMonth);
			assertEquals(year, yearQuarter.getYear());
			assertEquals(2, yearQuarter.getQuarterValue());
			assertSame(Quarter.Q2, yearQuarter.getQuarter());
		}
	}

	@ParameterizedTest
	@ValueSource(ints = {
			2023, // 非闰年
			2024, // 闰年
			Year.MIN_VALUE,
			Year.MAX_VALUE,
	})
	void of_ValidYearMonth_CreatesYearMonth_Q3(int year) {
		{
			YearMonth yearMonth = YearMonth.of(year, 7);
			YearQuarter yearQuarter = YearQuarter.of(yearMonth);
			assertEquals(year, yearQuarter.getYear());
			assertEquals(3, yearQuarter.getQuarterValue());
			assertSame(Quarter.Q3, yearQuarter.getQuarter());
		}
		{
			YearMonth yearMonth = YearMonth.of(year, 8);
			YearQuarter yearQuarter = YearQuarter.of(yearMonth);
			assertEquals(year, yearQuarter.getYear());
			assertEquals(3, yearQuarter.getQuarterValue());
			assertSame(Quarter.Q3, yearQuarter.getQuarter());
		}
		{
			YearMonth yearMonth = YearMonth.of(year, 9);
			YearQuarter yearQuarter = YearQuarter.of(yearMonth);
			assertEquals(year, yearQuarter.getYear());
			assertEquals(3, yearQuarter.getQuarterValue());
			assertSame(Quarter.Q3, yearQuarter.getQuarter());
		}
	}

	@ParameterizedTest
	@ValueSource(ints = {
			2023, // 非闰年
			2024, // 闰年
			Year.MIN_VALUE,
			Year.MAX_VALUE,
	})
	void of_ValidYearMonth_CreatesYearMonth_Q4(int year) {
		{
			YearMonth yearMonth = YearMonth.of(year, 10);
			YearQuarter yearQuarter = YearQuarter.of(yearMonth);
			assertEquals(year, yearQuarter.getYear());
			assertEquals(4, yearQuarter.getQuarterValue());
			assertSame(Quarter.Q4, yearQuarter.getQuarter());
		}
		{
			YearMonth yearMonth = YearMonth.of(year, 11);
			YearQuarter yearQuarter = YearQuarter.of(yearMonth);
			assertEquals(year, yearQuarter.getYear());
			assertEquals(4, yearQuarter.getQuarterValue());
			assertSame(Quarter.Q4, yearQuarter.getQuarter());
		}
		{
			YearMonth yearMonth = YearMonth.of(year, 12);
			YearQuarter yearQuarter = YearQuarter.of(yearMonth);
			assertEquals(year, yearQuarter.getYear());
			assertEquals(4, yearQuarter.getQuarterValue());
			assertSame(Quarter.Q4, yearQuarter.getQuarter());
		}
	}

	@ParameterizedTest
	@ValueSource(ints = {
			2023, // 非闰年
			2024, // 闰年
			Year.MIN_VALUE,
			Year.MAX_VALUE,
	})
	void of_NullYearMonth_CreatesYearMonth_Q4(int year) {
		YearMonth yearMonth = null;
		assertThrows(NullPointerException.class,
				() -> YearQuarter.of(yearMonth));
	}

	// ================================
	// #endregion - of(YearMonth yearMonth)
	// ================================

	// ================================
	// #region - firstDate & lastDate
	// ================================

	@ParameterizedTest
	@ValueSource(ints = { 1949, 1990, 2000, 2008, 2023, 2024, Year.MIN_VALUE, Year.MAX_VALUE })
	void test_getFirstDate_And_getLastDate(int year) {
		{
			final int quarterValue = 1;
			YearQuarter yearQuarter = YearQuarter.of(year, quarterValue);

			LocalDate expectedFirstDate = LocalDate.of(year, 1, 1);
			LocalDate expectedLastDate = LocalDate.of(year, 3, 31);

			assertEquals(expectedFirstDate, yearQuarter.firstDate());
			assertEquals(expectedLastDate, yearQuarter.lastDate());
		}
		{
			final int quarterValue = 2;
			YearQuarter yearQuarter = YearQuarter.of(year, quarterValue);

			LocalDate expectedFirstDate = LocalDate.of(year, 4, 1);
			LocalDate expectedLastDate = LocalDate.of(year, 6, 30);

			assertEquals(expectedFirstDate, yearQuarter.firstDate());
			assertEquals(expectedLastDate, yearQuarter.lastDate());
		}
		{
			final int quarterValue = 3;
			YearQuarter yearQuarter = YearQuarter.of(year, quarterValue);

			LocalDate expectedFirstDate = LocalDate.of(year, 7, 1);
			LocalDate expectedLastDate = LocalDate.of(year, 9, 30);

			assertEquals(expectedFirstDate, yearQuarter.firstDate());
			assertEquals(expectedLastDate, yearQuarter.lastDate());
		}
		{
			final int quarterValue = 4;
			YearQuarter yearQuarter = YearQuarter.of(year, quarterValue);

			LocalDate expectedFirstDate = LocalDate.of(year, 10, 1);
			LocalDate expectedLastDate = LocalDate.of(year, 12, 31);

			assertEquals(expectedFirstDate, yearQuarter.firstDate());
			assertEquals(expectedLastDate, yearQuarter.lastDate());
		}
	}

	// ================================
	// #endregion - firstDate & lastDate
	// ================================

	// ================================
	// #region - firstYearMonth & lastYearMonth
	// ================================

	@ParameterizedTest
	@ValueSource(ints = { 1949, 1990, 2000, 2008, 2023, 2024, Year.MIN_VALUE, Year.MAX_VALUE })
	void test_firstYearMonth_And_lastYearMonth(int year) {
		YearQuarter yq;

		yq = YearQuarter.of(year, Quarter.Q1);
		assertEquals(YearMonth.of(year, 1), yq.firstYearMonth());
		yq = YearQuarter.of(year, Quarter.Q2);
		assertEquals(YearMonth.of(year, 4), yq.firstYearMonth());
		yq = YearQuarter.of(year, Quarter.Q3);
		assertEquals(YearMonth.of(year, 7), yq.firstYearMonth());
		yq = YearQuarter.of(year, Quarter.Q4);
		assertEquals(YearMonth.of(year, 10), yq.firstYearMonth());

		yq = YearQuarter.of(year, Quarter.Q1);
		assertEquals(YearMonth.of(year, 3), yq.lastYearMonth());
		yq = YearQuarter.of(year, Quarter.Q2);
		assertEquals(YearMonth.of(year, 6), yq.lastYearMonth());
		yq = YearQuarter.of(year, Quarter.Q3);
		assertEquals(YearMonth.of(year, 9), yq.lastYearMonth());
		yq = YearQuarter.of(year, Quarter.Q4);
		assertEquals(YearMonth.of(year, 12), yq.lastYearMonth());
	}

	// ================================
	// #endregion - firstYearMonth & lastYearMonth
	// ================================

	// ================================
	// #region - firstMonth & lastMonth
	// ================================

	@ParameterizedTest
	@ValueSource(ints = { 1949, 1990, 2000, 2008, 2023, 2024, Year.MIN_VALUE, Year.MAX_VALUE })
	void testFirstMonthAndLastMonth(int year) {
		YearQuarter q1 = YearQuarter.of(year, 1);
		assertEquals(1, q1.firstMonthValue());
		assertEquals(Month.JANUARY, q1.firstMonth());
		assertEquals(3, q1.lastMonthValue());
		assertEquals(Month.MARCH, q1.lastMonth());

		YearQuarter q2 = YearQuarter.of(year, 2);
		assertEquals(4, q2.firstMonthValue());
		assertEquals(Month.APRIL, q2.firstMonth());
		assertEquals(6, q2.lastMonthValue());
		assertEquals(Month.JUNE, q2.lastMonth());

		YearQuarter q3 = YearQuarter.of(year, 3);
		assertEquals(7, q3.firstMonthValue());
		assertEquals(Month.JULY, q3.firstMonth());
		assertEquals(9, q3.lastMonthValue());
		assertEquals(Month.SEPTEMBER, q3.lastMonth());

		YearQuarter q4 = YearQuarter.of(year, 4);
		assertEquals(10, q4.firstMonthValue());
		assertEquals(Month.OCTOBER, q4.firstMonth());
		assertEquals(12, q4.lastMonthValue());
		assertEquals(Month.DECEMBER, q4.lastMonth());
	}

	// ================================
	// #endregion - firstMonth & lastMonth
	// ================================

	// ================================
	// #region - compareTo
	// ================================

	@Test
	void testCompareTo() {
		int year1;
		int quarter1;
		YearQuarter yearQuarter1;

		year1 = 2024;
		quarter1 = 1;
		yearQuarter1 = YearQuarter.of(year1, Quarter.of(quarter1));

		for (int year2 = 2000; year2 <= 2050; year2++) {
			for (int quarter2 = 1; quarter2 <= 4; quarter2++) {
				YearQuarter yearQuarter2 = YearQuarter.of(year2, Quarter.of(quarter2));

				if (year1 == year2) {
					// 同年
					assertEquals(quarter1 - quarter2, yearQuarter1.compareTo(yearQuarter2));

					if (quarter1 == quarter2) {
						// 同年同季度
						assertEquals(yearQuarter1, yearQuarter2);
						assertEquals(0, yearQuarter1.compareTo(yearQuarter2));
					} else if (quarter1 < quarter2) {
						assertNotEquals(yearQuarter1, yearQuarter2);
						assertTrue(yearQuarter1.isBefore(yearQuarter2));
						assertFalse(yearQuarter1.isAfter(yearQuarter2));
						assertFalse(yearQuarter2.isBefore(yearQuarter1));
						assertTrue(yearQuarter2.isAfter(yearQuarter1));
					} else if (quarter1 > quarter2) {
						assertNotEquals(yearQuarter1, yearQuarter2);
						assertFalse(yearQuarter1.isBefore(yearQuarter2));
						assertTrue(yearQuarter1.isAfter(yearQuarter2));
						assertTrue(yearQuarter2.isBefore(yearQuarter1));
						assertFalse(yearQuarter2.isAfter(yearQuarter1));
					}
				} else {
					// 不同年
					assertEquals(year1 - year2, yearQuarter1.compareTo(yearQuarter2));
					assertNotEquals(0, yearQuarter1.compareTo(yearQuarter2));
					if (year1 < year2) {
						assertNotEquals(yearQuarter1, yearQuarter2);
						assertTrue(yearQuarter1.isBefore(yearQuarter2));
						assertFalse(yearQuarter1.isAfter(yearQuarter2));
						assertFalse(yearQuarter2.isBefore(yearQuarter1));
						assertTrue(yearQuarter2.isAfter(yearQuarter1));
					} else if (year1 > year2) {
						assertNotEquals(yearQuarter1, yearQuarter2);
						assertFalse(yearQuarter1.isBefore(yearQuarter2));
						assertTrue(yearQuarter1.isAfter(yearQuarter2));
						assertTrue(yearQuarter2.isBefore(yearQuarter1));
						assertFalse(yearQuarter2.isAfter(yearQuarter1));
					}
				}
			}
		}
	}

	// ================================
	// #endregion - compareTo
	// ================================

	@ParameterizedTest
	@ValueSource(ints = { Year.MIN_VALUE + 25, Year.MAX_VALUE - 25, -1, 0, 1, 1949, 1990, 2000, 2008, 2023, 2024 })
	void testPlusQuartersAndMinusQuarters(int year) {
		for (int quarter = 1; quarter <= 4; quarter++) {
			YearQuarter yq1 = YearQuarter.of(year, quarter);
			for (int quartersToAdd = -100; quartersToAdd <= 100; quartersToAdd++) {
				YearQuarter plus = yq1.plusQuarters(quartersToAdd);
				YearQuarter minus = yq1.minusQuarters(-quartersToAdd);
				assertEquals(plus, minus);

				// offset: 表示自 公元 0000年以来，经历了多少季度。所以 0 表示 -0001,Q4; 1 表示 0000 Q1
				long offset = (year * 4L + quarter) + quartersToAdd;
				if (offset > 0) {
					assertEquals((offset - 1) / 4, plus.getYear());
					assertEquals(((offset - 1) % 4) + 1, plus.getQuarterValue());
				} else {
					assertEquals((offset / 4 - 1), plus.getYear());
					assertEquals((4 + offset % 4), plus.getQuarterValue());
				}
			}
		}
	}

	@ParameterizedTest
	@ValueSource(ints = { Year.MIN_VALUE + 1, Year.MAX_VALUE - 1, -1, 0, 1, 1900, 1990, 2000, 2023, 2024 })
	void test_nextQuarter_And_lastQuarter(int year) {
		int quarter;

		YearQuarter yq;
		YearQuarter next;
		YearQuarter last;

		quarter = 1;
		yq = YearQuarter.of(year, quarter);
		next = yq.nextQuarter();
		assertEquals(year, next.getYear());
		assertEquals(2, next.getQuarterValue());
		last = yq.lastQuarter();
		assertEquals(year - 1, last.getYear());
		assertEquals(4, last.getQuarterValue());

		quarter = 2;
		yq = YearQuarter.of(year, quarter);
		next = yq.nextQuarter();
		assertEquals(year, next.getYear());
		assertEquals(3, next.getQuarterValue());
		last = yq.lastQuarter();
		assertEquals(year, last.getYear());
		assertEquals(1, last.getQuarterValue());

		quarter = 3;
		yq = YearQuarter.of(year, quarter);
		next = yq.nextQuarter();
		assertEquals(year, next.getYear());
		assertEquals(4, next.getQuarterValue());
		last = yq.lastQuarter();
		assertEquals(year, last.getYear());
		assertEquals(2, last.getQuarterValue());

		quarter = 4;
		yq = YearQuarter.of(year, quarter);
		next = yq.nextQuarter();
		assertEquals(year + 1, next.getYear());
		assertEquals(1, next.getQuarterValue());
		last = yq.lastQuarter();
		assertEquals(year, last.getYear());
		assertEquals(3, last.getQuarterValue());
	}

	@ParameterizedTest
	@ValueSource(ints = { Year.MIN_VALUE + 100, Year.MAX_VALUE - 100, -1, 0, 1, 1949, 1990, 2000, 2008, 2023, 2024 })
	void test_PlusYearsAndMinusYears(int year) {
		for (int yearToAdd = -100; yearToAdd <= 100; yearToAdd++) {
			YearQuarter q1 = YearQuarter.of(year, Quarter.Q1);
			YearQuarter plus = q1.plusYears(yearToAdd);
			assertEquals(year + yearToAdd, plus.getYear());
			assertEquals(Quarter.Q1, plus.getQuarter());
			YearQuarter minus = q1.minusYears(yearToAdd);
			assertEquals(Quarter.Q1, minus.getQuarter());
			assertEquals(year - yearToAdd, minus.getYear());

			assertEquals(q1.plusYears(yearToAdd), q1.minusYears(-yearToAdd));
		}
	}

	@ParameterizedTest
	@ValueSource(ints = { Year.MIN_VALUE + 1, Year.MAX_VALUE - 1, -1, 0, 1, 1900, 1990, 2000, 2023, 2024 })
	void test_nextYear_And_lastYear(int year) {
		int quarter;

		YearQuarter yq;
		YearQuarter next;
		YearQuarter last;

		quarter = 1;
		yq = YearQuarter.of(year, quarter);
		next = yq.nextYear();
		assertSame(Quarter.Q1, yq.getQuarter());
		assertEquals(year + 1, next.getYear());
		assertSame(Quarter.Q1, next.getQuarter());
		last = yq.lastYear();
		assertEquals(year - 1, last.getYear());
		assertSame(Quarter.Q1, last.getQuarter());

		quarter = 2;
		yq = YearQuarter.of(year, quarter);
		next = yq.nextYear();
		assertSame(Quarter.Q2, yq.getQuarter());
		assertEquals(year + 1, next.getYear());
		assertSame(Quarter.Q2, next.getQuarter());
		last = yq.lastYear();
		assertEquals(year - 1, last.getYear());
		assertSame(Quarter.Q2, last.getQuarter());

		quarter = 3;
		yq = YearQuarter.of(year, quarter);
		next = yq.nextYear();
		assertSame(Quarter.Q3, yq.getQuarter());
		assertEquals(year + 1, next.getYear());
		assertSame(Quarter.Q3, next.getQuarter());
		last = yq.lastYear();
		assertEquals(year - 1, last.getYear());
		assertSame(Quarter.Q3, last.getQuarter());

		quarter = 4;
		yq = YearQuarter.of(year, quarter);
		next = yq.nextYear();
		assertSame(Quarter.Q4, yq.getQuarter());
		assertEquals(year + 1, next.getYear());
		assertSame(Quarter.Q4, next.getQuarter());
		last = yq.lastYear();
		assertEquals(year - 1, last.getYear());
		assertSame(Quarter.Q4, last.getQuarter());
	}

	@ParameterizedTest
	@ValueSource(ints = { -1, 0, 1, 1900, 2000, 2023, 2024, Year.MAX_VALUE, Year.MIN_VALUE })
	void test_compareTo_sameYear(int year) {
		YearQuarter yq1 = YearQuarter.of(year, 1);
		YearQuarter yq2 = YearQuarter.of(year, 2);
		YearQuarter yq3 = YearQuarter.of(year, 3);
		YearQuarter yq4 = YearQuarter.of(year, 4);

		assertTrue(yq1.equals(YearQuarter.of(year, Quarter.Q1))); // NOSONAR
		assertTrue(yq1.compareTo(yq1) == 0); // NOSONAR
		assertTrue(yq1.compareTo(yq2) < 0);
		assertTrue(yq1.compareTo(yq3) < 0);
		assertTrue(yq1.compareTo(yq4) < 0);

		assertTrue(yq2.equals(YearQuarter.of(year, Quarter.Q2))); // NOSONAR
		assertTrue(yq2.compareTo(yq1) > 0);
		assertTrue(yq2.compareTo(yq2) == 0); // NOSONAR
		assertTrue(yq2.compareTo(yq3) < 0);
		assertTrue(yq2.compareTo(yq4) < 0);

		assertTrue(yq3.equals(YearQuarter.of(year, Quarter.Q3))); // NOSONAR
		assertTrue(yq3.compareTo(yq1) > 0);
		assertTrue(yq3.compareTo(yq2) > 0);
		assertTrue(yq3.compareTo(yq3) == 0); // NOSONAR
		assertTrue(yq3.compareTo(yq4) < 0);

		assertTrue(yq4.equals(YearQuarter.of(year, Quarter.Q4))); // NOSONAR
		assertTrue(yq4.compareTo(yq1) > 0);
		assertTrue(yq4.compareTo(yq2) > 0);
		assertTrue(yq4.compareTo(yq3) > 0);
		assertTrue(yq4.compareTo(yq4) == 0); // NOSONAR
	}

	@ParameterizedTest
	@ValueSource(ints = { -1, 0, 1, 1900, 2000, 2023, 2024, Year.MAX_VALUE, Year.MIN_VALUE })
	void test_isBefore_sameYear(int year) {
		YearQuarter yq1 = YearQuarter.of(year, 1);
		YearQuarter yq2 = YearQuarter.of(year, 2);
		YearQuarter yq3 = YearQuarter.of(year, 3);
		YearQuarter yq4 = YearQuarter.of(year, 4);

		assertFalse(yq1.isBefore(YearQuarter.of(year, Quarter.Q1)));
		assertTrue(yq1.isBefore(yq2));
		assertTrue(yq1.isBefore(yq3));
		assertTrue(yq1.isBefore(yq4));

		assertFalse(yq2.isBefore(yq1));
		assertFalse(yq2.isBefore(YearQuarter.of(year, Quarter.Q2)));
		assertTrue(yq2.isBefore(yq3));
		assertTrue(yq2.isBefore(yq4));

		assertFalse(yq3.isBefore(yq1));
		assertFalse(yq3.isBefore(yq2));
		assertFalse(yq3.isBefore(YearQuarter.of(year, Quarter.Q3)));
		assertTrue(yq3.isBefore(yq4));

		assertFalse(yq4.isBefore(yq1));
		assertFalse(yq4.isBefore(yq2));
		assertFalse(yq4.isBefore(yq3));
		assertFalse(yq4.isBefore(YearQuarter.of(year, Quarter.Q4)));
	}

	@ParameterizedTest
	@ValueSource(ints = { -1, 0, 1, 1900, 2000, 2023, 2024, Year.MAX_VALUE, Year.MIN_VALUE })
	void test_isAfter_sameYear(int year) {
		YearQuarter yq1 = YearQuarter.of(year, 1);
		YearQuarter yq2 = YearQuarter.of(year, 2);
		YearQuarter yq3 = YearQuarter.of(year, 3);
		YearQuarter yq4 = YearQuarter.of(year, 4);

		assertFalse(yq1.isAfter(YearQuarter.of(year, Quarter.Q1)));
		assertFalse(yq1.isAfter(yq2));
		assertFalse(yq1.isAfter(yq3));
		assertFalse(yq1.isAfter(yq4));

		assertTrue(yq2.isAfter(yq1));
		assertFalse(yq2.isAfter(YearQuarter.of(year, Quarter.Q2)));
		assertFalse(yq2.isAfter(yq3));
		assertFalse(yq2.isAfter(yq4));

		assertTrue(yq3.isAfter(yq1));
		assertTrue(yq3.isAfter(yq2));
		assertFalse(yq3.isAfter(YearQuarter.of(year, Quarter.Q3)));
		assertFalse(yq3.isAfter(yq4));

		assertTrue(yq4.isAfter(yq1));
		assertTrue(yq4.isAfter(yq2));
		assertTrue(yq4.isAfter(yq3));
		assertFalse(yq4.isAfter(YearQuarter.of(year, Quarter.Q4)));
	}

	@Test
	void test_compareTo_null() {
		YearQuarter yq = YearQuarter.of(2024, 4);
		assertThrows(NullPointerException.class,
				() -> yq.compareTo(null));
		assertThrows(NullPointerException.class,
				() -> yq.isBefore(null));
		assertThrows(NullPointerException.class,
				() -> yq.isAfter(null));
		assertNotEquals(null, yq);
	}

	@ParameterizedTest
	@ValueSource(ints = { -1, 0, 1, 1900, 2000, 2023, 2024, Year.MAX_VALUE - 1, Year.MIN_VALUE + 1 })
	void test_compareTo_differentYear(int year) {
		for (int quarter1 = 1; quarter1 <= 4; quarter1++) {
			YearQuarter yq = YearQuarter.of(year, quarter1);
			for (int quarter2 = 1; quarter2 <= 4; quarter2++) {
				// gt
				assertTrue(yq.compareTo(YearQuarter.of(year + 1, quarter2)) < 0);
				assertTrue(yq.isBefore(YearQuarter.of(year + 1, quarter2)));
				assertTrue(YearQuarter.of(year + 1, quarter2).compareTo(yq) > 0);
				assertTrue(YearQuarter.of(year + 1, quarter2).isAfter(yq));
				// lt
				assertTrue(yq.compareTo(YearQuarter.of(year - 1, quarter2)) > 0);
				assertTrue(yq.isAfter(YearQuarter.of(year - 1, quarter2)));
				assertTrue(YearQuarter.of(year - 1, quarter2).compareTo(yq) < 0);
				assertTrue(YearQuarter.of(year - 1, quarter2).isBefore(yq));
			}
		}
	}
}
