package cn.hutool.cron.pattern;

import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.quartz.CronExpression;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
		Assertions.assertFalse(pattern.match(DateUtil.parse("2026-01-31 00:00:00").getTime(), true));
		Assertions.assertTrue(pattern.match(DateUtil.parse("2026-01-30 00:00:00").getTime(), true));

		// 30/2只表示30日
		final CronPattern pattern2 = CronPattern.of("0 0 0 30/2 * ?");
		Assertions.assertFalse(pattern2.match(DateUtil.parse("2026-02-28 00:00:00").getTime(), true));
		Assertions.assertFalse(pattern2.match(DateUtil.parse("2026-03-31 00:00:00").getTime(), true));
	}

	@Test
	void explicitLastDayStillMatchesTest() {
		Assertions.assertTrue(CronPattern.of("0 0 0 L * ?").match(DateUtil.parse("2026-02-28 00:00:00").getTime(), true));
		Assertions.assertTrue(CronPattern.of("0 0 0 28-L * ?").match(DateUtil.parse("2026-04-30 00:00:00").getTime(), true));
		Assertions.assertTrue(CronPattern.of("0 0 0 L/2 * ?").match(DateUtil.parse("2026-04-30 00:00:00").getTime(), true));
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
				Assertions.assertEquals(quartz.isSatisfiedBy(date), pattern.match(date.getTime(), true),
					expression + " match " + DateUtil.formatDateTime(date));

				final Calendar noon = (Calendar) day.clone();
				noon.set(Calendar.HOUR_OF_DAY, 12);
				Assertions.assertEquals(quartz.getNextValidTimeAfter(noon.getTime()), pattern.nextMatchAfter(noon).getTime(),
					expression + " next after " + DateUtil.formatDateTime(noon.getTime()));

				day.add(Calendar.DAY_OF_MONTH, 1);
			}
		}
	}
}
