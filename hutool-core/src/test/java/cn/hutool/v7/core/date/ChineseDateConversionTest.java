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

import cn.hutool.v7.core.date.chinese.ChineseDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

import java.time.LocalDate;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ResourceLock("java.util.TimeZone.default")
public class ChineseDateConversionTest {

	@Test
	public void firstLunarMonthRoundTripTest() {
		final LocalDate firstDay = LocalDate.of(1900, 1, 31);
		for (int day = 1; day <= 29; day++) {
			final ChineseDate date = new ChineseDate(1900, 1, day, false);
			assertEquals(firstDay.plusDays(day - 1), solarDate(date));
			final ChineseDate roundTrip = new ChineseDate(solarDate(date));
			assertEquals(1900, roundTrip.getChineseYear());
			assertEquals(1, roundTrip.getMonth());
			assertEquals(day, roundTrip.getDay());
		}
	}

	@Test
	public void zeroLunarDayRemainsInvalidTest() {
		assertEquals(-1, new ChineseDate(1900, 1, 0, false).getGregorianYear());
		assertEquals(-1, new ChineseDate(2024, 1, 0, false).getGregorianYear());
	}

	@Test
	public void lunarToSolarIsIndependentOfDefaultTimeZoneTest() {
		final TimeZone original = TimeZone.getDefault();
		final String[] zones = {"UTC", "Asia/Shanghai", "America/New_York", "Asia/Tokyo", "Pacific/Honolulu"};
		final int[][] dates = {
			{2024, 1, 1, 0, 2024, 2, 10},
			{2020, 4, 15, 0, 2020, 5, 7},
			{2020, 4, 15, 1, 2020, 6, 6},
			{1991, 8, 8, 0, 1991, 9, 15},
			{2025, 6, 1, 0, 2025, 6, 25},
			{2025, 6, 1, 1, 2025, 7, 25}
		};
		try {
			for (final String zone : zones) {
				TimeZone.setDefault(TimeZone.getTimeZone(zone));
				for (final int[] input : dates) {
					final ChineseDate date = new ChineseDate(input[0], input[1], input[2], input[3] == 1);
					final LocalDate expected = LocalDate.of(input[4], input[5], input[6]);
					assertEquals(expected, solarDate(date), zone);
					assertEquals(expected, date.getGregorianDate().toInstant()
						.atZone(TimeZone.getDefault().toZoneId()).toLocalDate(), zone);
				}
			}
		} finally {
			TimeZone.setDefault(original);
		}
	}

	private static LocalDate solarDate(final ChineseDate date) {
		return LocalDate.of(date.getGregorianYear(), date.getGregorianMonthBase1(), date.getGregorianDay());
	}
}
