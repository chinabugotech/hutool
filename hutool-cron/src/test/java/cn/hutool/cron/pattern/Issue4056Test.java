package cn.hutool.cron.pattern;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.quartz.CronExpression;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class Issue4056Test {

	/**
	 * 见：https://github.com/quartz-scheduler/quartz/issues/1298
	 * Quartz-2.5.0这块有bug，只能使用2.4.0测认
	 *
	 * @throws ParseException 解析错误
	 */
	@Test
	void testCronAll() throws ParseException {
		final ArrayList<String> cronsList = new ArrayList<>();
		final ArrayList<DateTime> judgeTimes = new ArrayList<>();

		// 1. Cron 表达式（40个）
		cronsList.add("0 0 0 * * ? *"); // 每天00:00
		cronsList.add("0 0 12 * * ? *"); // 每天中午12:00
		cronsList.add("0 0 18 * * ? *"); // 每天傍晚18:00
		cronsList.add("0 0 6,12,18 * * ? *"); // 每天6点、12点、18点
		cronsList.add("0 0 */6 * * ? *"); // 每6小时
		cronsList.add("0 30 */8 * * ? *"); // 每8小时的30分
		cronsList.add("0 */15 * * * ? *"); // 每15分钟
		cronsList.add("0 */5 9-17 * * ? *"); // 工作时间内每5分钟
		cronsList.add("0 0 0-23/2 * * ? *"); // 每2小时
		cronsList.add("0 0 0 */8 * ? *"); // 每8天的00:00
		cronsList.add("0 0 12 15 * ? *"); // 每月15日12:00
		cronsList.add("0 0 0 L * ? *"); // 每月最后一天00:00
		cronsList.add("0 0 0 29 2 ? *"); // 2月29日00:00（闰年）
		cronsList.add("0 0 0 1 1 ? *"); // 每年1月1日00:00
		cronsList.add("0 0/30 * * * ? *"); // 每小时0分和30分
		cronsList.add("0 0 */4 * * ? *"); // 每4小时
		cronsList.add("0 0 0 1/3 * ? *"); // 每3天00:00
		cronsList.add("0 0 2 28-31 * ? *"); // 每月最后几天2:00
		cronsList.add("0 0 0 1,15 * ? *"); // 每月1日和15日00:00
		cronsList.add("0 0 0 1/5 * ? *"); // 每5天00:00
		cronsList.add("0 0 0 1/10 * ? *"); // 每10天00:00
		cronsList.add("0 0 0 1 */3 ? *"); // 每3个月的第1天00:00
		cronsList.add("0 0 0 25 12 ? *"); // 圣诞节00:00
		cronsList.add("0 0 12 31 12 ? *"); // 新年前夜12:00
		cronsList.add("0 0 0 14 2 ? *"); // 情人节00:00
		cronsList.add("0 0 10 1 5 ? *"); // 劳动节10:00
		cronsList.add("0 0 9 8 3 ? *"); // 妇女节09:00
		cronsList.add("0 0 0 1 4 ? *"); // 愚人节00:00
		cronsList.add("0 0 12 4 7 ? *"); // 美国独立日12:00
		cronsList.add("0 0 0 31 10 ? *"); // 万圣节00:00
		cronsList.add("0 7,19,31,43,55 * * * ? *"); // 特定分钟
		cronsList.add("0 */7 * * * ? *"); // 每7分钟
		cronsList.add("0 15-45/5 * * * ? *"); // 每小时的15-45分之间每5分钟
		cronsList.add("0 0-30/2 * * * ? *"); // 每小时前30分钟每2分钟
		cronsList.add("0 45 23 * * ? *"); // 每天23:45
		cronsList.add("0 59 23 * * ? *"); // 每天23:59
		cronsList.add("0 0 */3 * * ? *"); // 每3小时
		cronsList.add("0 0 9-18/2 * * ? *"); // 9点到18点每2小时
		cronsList.add("0 0 22-2 * * ? *"); // 22点到次日2点每小时
		cronsList.add("0 30 16 L * ? *"); // 每月最后一天16:30


		// 2. 测试时间 (50个)
		judgeTimes.add(DateUtil.parse("2025-02-01 18:20:10"));
		judgeTimes.add(DateUtil.parse("2024-02-29 10:00:00"));
		judgeTimes.add(DateUtil.parse("2025-12-31 23:59:59"));
		judgeTimes.add(DateUtil.parse("2025-01-01 00:00:00"));
		judgeTimes.add(DateUtil.parse("2025-06-15 12:00:00"));
		judgeTimes.add(DateUtil.parse("2025-03-30 00:00:00"));
		judgeTimes.add(DateUtil.parse("2025-02-28 23:59:59"));
		judgeTimes.add(DateUtil.parse("2025-03-01 00:00:00"));
		judgeTimes.add(DateUtil.parse("2025-01-31 23:59:59"));
		judgeTimes.add(DateUtil.parse("2025-04-30 23:59:59"));
		judgeTimes.add(DateUtil.parse("2025-06-30 23:59:59"));
		judgeTimes.add(DateUtil.parse("2025-09-30 23:59:59"));
		judgeTimes.add(DateUtil.parse("2026-01-01 00:00:00"));
		judgeTimes.add(DateUtil.parse("2024-02-28 00:00:00"));
		judgeTimes.add(DateUtil.parse("2024-02-29 00:00:00"));
		judgeTimes.add(DateUtil.parse("2024-02-29 23:59:59"));
		judgeTimes.add(DateUtil.parse("2023-02-28 23:59:59"));
		judgeTimes.add(DateUtil.parse("2028-02-29 12:00:00"));
		judgeTimes.add(DateUtil.parse("2025-06-15 00:00:00"));
		judgeTimes.add(DateUtil.parse("2025-06-15 23:59:59"));
		judgeTimes.add(DateUtil.parse("2025-03-31 23:59:59"));
		judgeTimes.add(DateUtil.parse("2025-04-01 00:00:00"));
		judgeTimes.add(DateUtil.parse("2025-07-01 00:00:00"));
		judgeTimes.add(DateUtil.parse("2025-10-01 00:00:00"));
		judgeTimes.add(DateUtil.parse("2025-01-06 09:00:00"));
		judgeTimes.add(DateUtil.parse("2025-01-10 17:00:00"));
		judgeTimes.add(DateUtil.parse("2025-01-11 12:00:00"));
		judgeTimes.add(DateUtil.parse("2025-01-12 12:00:00"));
		judgeTimes.add(DateUtil.parse("2025-03-09 01:59:59"));
		judgeTimes.add(DateUtil.parse("2025-03-09 03:00:00"));
		judgeTimes.add(DateUtil.parse("2025-11-02 01:59:59"));
		judgeTimes.add(DateUtil.parse("2025-11-02 01:00:00"));
		judgeTimes.add(DateUtil.parse("2024-12-31 23:59:59"));
		judgeTimes.add(DateUtil.parse("2024-01-01 00:00:00"));
		judgeTimes.add(DateUtil.parse("2026-12-31 23:59:59"));
		judgeTimes.add(DateUtil.parse("2026-01-01 00:00:00"));
		judgeTimes.add(DateUtil.parse("2025-05-15 08:45:30"));
		judgeTimes.add(DateUtil.parse("2025-08-22 14:20:15"));
		judgeTimes.add(DateUtil.parse("2025-11-03 19:10:45"));
		judgeTimes.add(DateUtil.parse("2025-02-14 09:30:00"));
		judgeTimes.add(DateUtil.parse("2025-07-07 07:07:07"));
		judgeTimes.add(DateUtil.parse("2025-09-09 09:09:09"));
		judgeTimes.add(DateUtil.parse("2025-10-10 10:10:10"));
		judgeTimes.add(DateUtil.parse("2025-12-12 12:12:12"));
		judgeTimes.add(DateUtil.parse("2025-03-03 03:03:03"));
		judgeTimes.add(DateUtil.parse("2025-06-06 06:06:06"));
		judgeTimes.add(DateUtil.parse("2025-04-16 00:00:00"));
		judgeTimes.add(DateUtil.parse("2025-04-30 23:59:59"));
		judgeTimes.add(DateUtil.parse("2025-05-01 00:00:00"));
		judgeTimes.add(DateUtil.parse("2025-05-01 00:00:01"));

		// 3. 计算并比对结果
		for (final String cron : cronsList) {
			final CronPattern hutoolCorn = new CronPattern(cron);
			final CronExpression quartzCorn = new CronExpression(cron);
			for (final DateTime judgeTime : judgeTimes) {
				final Date quartzDate = quartzCorn.getNextValidTimeAfter(judgeTime);
				final Date hutoolDate = CronPatternUtil.nextDateAfter(hutoolCorn, judgeTime);
				Assertions.assertEquals(quartzDate, hutoolDate);
			}
		}
	}

	@Test
	void issue4056Test() {
		final String cron = "0 0 0 1/3 * ? *";
		final CronPattern hutoolCorn = new CronPattern(cron);
		final Date hutoolDate = CronPatternUtil.nextDateAfter(hutoolCorn, DateUtil.parse("2025-02-28 00:00:00"));
		System.out.println(DateUtil.formatDateTime(hutoolDate));
	}
}
