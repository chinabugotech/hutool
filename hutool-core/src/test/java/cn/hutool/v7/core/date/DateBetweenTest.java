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

import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateBetweenTest {

	@Test
	public void betweenYearTest() {
		final Date start = DateUtil.parse("2017-02-01 12:23:46");
		final Date end = DateUtil.parse("2018-02-01 12:23:46");
		final long betweenYear = new DateBetween(start, end).betweenYear(false);
		assertEquals(1, betweenYear);

		final Date start1 = DateUtil.parse("2017-02-01 12:23:46");
		final Date end1 = DateUtil.parse("2018-03-01 12:23:46");
		final long betweenYear1 = new DateBetween(start1, end1).betweenYear(false);
		assertEquals(1, betweenYear1);

		// 不足1年
		final Date start2 = DateUtil.parse("2017-02-01 12:23:46");
		final Date end2 = DateUtil.parse("2018-02-01 11:23:46");
		final long betweenYear2 = new DateBetween(start2, end2).betweenYear(false);
		assertEquals(0, betweenYear2);
	}

	@Test
	public void betweenYearTest2() {
		final Date start = DateUtil.parse("2000-02-29");
		final Date end = DateUtil.parse("2018-02-28");
		final long betweenYear = new DateBetween(start, end).betweenYear(false);
		assertEquals(18, betweenYear);
	}

	@Test
	public void betweenYearTest3(){
		final String dateStr1 = "2023-02-28 00:00:01";
		final Date sdate = DateUtil.parse(dateStr1);
		final String dateStr2 = "2024-02-29 00:00:00";
		final Date edate = DateUtil.parse(dateStr2);

		final long result = DateUtil.betweenYear(sdate, edate, false);
		assertEquals(0, result);
	}

	@Test
	public void betweenYearTest4(){
		final String dateStr1 = "2024-02-29 00:00:00";
		final Date sdate = DateUtil.parse(dateStr1);
		final String dateStr2 = "2025-02-28 00:00:00";
		final Date edate = DateUtil.parse(dateStr2);

		final long result = DateUtil.betweenYear(sdate, edate, false);
		assertEquals(1, result);
	}

	@Test
	public void issueI97U3JTest(){
		final String dateStr1 = "2024-02-29 23:59:59";
		final Date sdate = DateUtil.parse(dateStr1);

		final String dateStr2 = "2023-03-01 00:00:00";
		final Date edate = DateUtil.parse(dateStr2);

		final long result = DateUtil.betweenYear(sdate, edate, false);
		assertEquals(0, result);
	}

	@Test
	public void betweenMonthTest() {
		final Date start = DateUtil.parse("2017-02-01 12:23:46");
		final Date end = DateUtil.parse("2018-02-01 12:23:46");
		final long betweenMonth = new DateBetween(start, end).betweenMonth(false);
		assertEquals(12, betweenMonth);

		final Date start1 = DateUtil.parse("2017-02-01 12:23:46");
		final Date end1 = DateUtil.parse("2018-03-01 12:23:46");
		final long betweenMonth1 = new DateBetween(start1, end1).betweenMonth(false);
		assertEquals(13, betweenMonth1);

		// 不足
		final Date start2 = DateUtil.parse("2017-02-01 12:23:46");
		final Date end2 = DateUtil.parse("2018-02-01 11:23:46");
		final long betweenMonth2 = new DateBetween(start2, end2).betweenMonth(false);
		assertEquals(11, betweenMonth2);
	}

	@Test
	public void betweenMinuteTest() {
		final Date date1 = DateUtil.parse("2017-03-01 20:33:23");
		final Date date2 = DateUtil.parse("2017-03-01 23:33:23");
		final String formatBetween = DateUtil.formatBetween(date1, date2, BetweenFormatter.Level.SECOND);
		assertEquals("3小时", formatBetween);
	}

	@Test
	public void betweenWeeksTest(){
		final long betweenWeek = DateUtil.betweenWeek(
				DateUtil.parse("2020-11-21"),
				DateUtil.parse("2020-11-23"), false);

		final long betweenWeek2 = TimeUtil.between(
				TimeUtil.parse("2020-11-21", "yyy-MM-dd"),
				TimeUtil.parse("2020-11-23", "yyy-MM-dd"),
				ChronoUnit.WEEKS);
		assertEquals(betweenWeek, betweenWeek2);
	}

	@Test
	public void issueIDFVKGTest() {
		Date b = new Date(1609459200000L); // 2021-01-01 00:00:00
		Date e = new Date(1609545600000L); // 2021-01-02 00:00:00
		DateBetween db = new DateBetween(b, e);

		// 修改原始 date
		b.setTime(0L); // 1970-01-01

		// 期望 DateBetween 不受影响，间隔仍为 1 天
		assertEquals(1, db.between(DateUnit.DAY));
	}
}
