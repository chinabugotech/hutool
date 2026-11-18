/*
 * Copyright (c) 2026 Hutool Team.
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

package cn.hutool.v7.cron.pattern;

import cn.hutool.v7.core.date.DateUtil;
import org.junit.jupiter.api.Test;
import org.quartz.CronExpression;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 日字段的步进展开不应包含表示“L”（最后一天）的32
 */
public class CronPatternDayOfMonthStepTest {
	private static final String[] PATTERNS = {
		"0 0 0 2/2 * ?",
		"0 0 0 2/3 * ?",
		"0 0 0 30/2 * ?",
		"0 0 0 */31 * ?",
		"0 0 0 26-5/2 * ?",
	};

	@Test
	void stepDoesNotMatchLastDayTest() {
		// 2026-01-31是奇数日，不应被“从2日开始每2天”匹配
		final CronPattern pattern = CronPattern.of("0 0 0 2/2 * ?");
		assertFalse(pattern.match(DateUtil.parse("2026-01-31 00:00:00").getTime(), true));
		assertTrue(pattern.match(DateUtil.parse("2026-01-30 00:00:00").getTime(), true));

		// 30/2只表示30日
		final CronPattern pattern2 = CronPattern.of("0 0 0 30/2 * ?");
		assertFalse(pattern2.match(DateUtil.parse("2026-02-28 00:00:00").getTime(), true));
		assertFalse(pattern2.match(DateUtil.parse("2026-03-31 00:00:00").getTime(), true));
	}

	@Test
	void explicitLastDayStillMatchesTest() {
		assertTrue(CronPattern.of("0 0 0 L * ?").match(DateUtil.parse("2026-02-28 00:00:00").getTime(), true));
		assertTrue(CronPattern.of("0 0 0 28-L * ?").match(DateUtil.parse("2026-04-30 00:00:00").getTime(), true));
		assertTrue(CronPattern.of("0 0 0 L/2 * ?").match(DateUtil.parse("2026-04-30 00:00:00").getTime(), true));
	}

	@Test
	void sameAsQuartzTest() throws ParseException {
		final TimeZone timeZone = TimeZone.getDefault();
		for (final String expression : PATTERNS) {
			final CronPattern pattern = CronPattern.of(expression);
			final CronExpression quartz = new CronExpression(expression);
			quartz.setTimeZone(timeZone);

			final Calendar day = Calendar.getInstance(timeZone);
			day.clear();
			day.set(2024, Calendar.JANUARY, 1, 0, 0, 0);
			// 覆盖闰年和平年
			for (int i = 0; i < 731; i++) {
				final Date date = day.getTime();
				assertEquals(quartz.isSatisfiedBy(date), pattern.match(date.getTime(), true),
					expression + " match " + DateUtil.formatDateTime(date));

				final Calendar noon = (Calendar) day.clone();
				noon.set(Calendar.HOUR_OF_DAY, 12);
				assertEquals(quartz.getNextValidTimeAfter(noon.getTime()), pattern.nextMatchAfter(noon).getTime(),
					expression + " next after " + DateUtil.formatDateTime(noon.getTime()));

				day.add(Calendar.DAY_OF_MONTH, 1);
			}
		}
	}
}
