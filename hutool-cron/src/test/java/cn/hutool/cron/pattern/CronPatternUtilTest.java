package cn.hutool.cron.pattern;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CronPatternUtilTest {

	@Test
	public void matchedDatesTest() {
		//测试每30秒执行
		List<Date> matchedDates = CronPatternUtil.matchedDates("0/30 * 8-18 * * ?", DateUtil.parse("2018-10-15 14:33:22"), 5, true);
		assertEquals(5, matchedDates.size());
		assertEquals("2018-10-15 14:33:30", matchedDates.get(0).toString());
		assertEquals("2018-10-15 14:34:00", matchedDates.get(1).toString());
		assertEquals("2018-10-15 14:34:30", matchedDates.get(2).toString());
		assertEquals("2018-10-15 14:35:00", matchedDates.get(3).toString());
		assertEquals("2018-10-15 14:35:30", matchedDates.get(4).toString());
	}

	@Test
	public void matchedDatesTest2() {
		//测试每小时执行
		List<Date> matchedDates = CronPatternUtil.matchedDates("0 0 */1 * * *", DateUtil.parse("2018-10-15 14:33:22"), 5, true);
		assertEquals(5, matchedDates.size());
		assertEquals("2018-10-15 15:00:00", matchedDates.get(0).toString());
		assertEquals("2018-10-15 16:00:00", matchedDates.get(1).toString());
		assertEquals("2018-10-15 17:00:00", matchedDates.get(2).toString());
		assertEquals("2018-10-15 18:00:00", matchedDates.get(3).toString());
		assertEquals("2018-10-15 19:00:00", matchedDates.get(4).toString());
	}

	@Test
	public void matchedDatesTest3() {
		//测试最后一天
		List<Date> matchedDates = CronPatternUtil.matchedDates("0 0 */1 L * *", DateUtil.parse("2018-10-30 23:33:22"), 5, true);
		assertEquals(5, matchedDates.size());
		assertEquals("2018-10-31 00:00:00", matchedDates.get(0).toString());
		assertEquals("2018-10-31 01:00:00", matchedDates.get(1).toString());
		assertEquals("2018-10-31 02:00:00", matchedDates.get(2).toString());
		assertEquals("2018-10-31 03:00:00", matchedDates.get(3).toString());
		assertEquals("2018-10-31 04:00:00", matchedDates.get(4).toString());
	}

	@Test
	public void issue4056Test() {
		// "*/5"和"1/5"意义相同，从1号开始，每5天一个匹配，则匹配的天为：
		// 2025-02-01, 2025-02-06, 2025-02-11, 2025-02-16, 2025-02-21, 2025-02-26
		// 2025-03-01, 2025-03-06, 2025-03-11, 2025-03-16, 2025-03-21, 2025-03-26, 2025-03-31
		final String cron = "0 0 0 */5 * ? *";
		final CronPattern cronPattern = new CronPattern(cron);

		// 2025-02-28不应该在匹配之列
		boolean match = cronPattern.match(DateUtil.parse("2025-02-28 00:00:00").toCalendar(), true);
		Assertions.assertFalse( match);

		match = cronPattern.match(DateUtil.parse("2025-03-01 00:00:00").toCalendar(), true);
		Assertions.assertTrue( match);

		match = cronPattern.match(DateUtil.parse("2025-03-31 00:00:00").toCalendar(), true);
		Assertions.assertTrue( match);
	}

	@Test
	public void issue4056Test2() {
		final String cron = "0 0 0 */5 * ? *";
		final CronPattern cronPattern = new CronPattern(cron);

		final DateTime judgeTime = DateUtil.parse("2025-02-27 23:59:59");
		final Date nextDate = CronPatternUtil.nextDateAfter(cronPattern, judgeTime);
		// "*/5"和"1/5"意义相同，从1号开始，每5天一个匹配，则匹配的天为：
		// 2025-02-01, 2025-02-06, 2025-02-11, 2025-02-16, 2025-02-21, 2025-02-26
		// 2025-03-01, 2025-03-06, 2025-03-11, 2025-03-16, 2025-03-21, 2025-03-26, 2025-03-31
		// 下一个匹配日期应为2025-03-01
		Assertions.assertEquals("2025-03-01 00:00:00", nextDate.toString());
	}
}
