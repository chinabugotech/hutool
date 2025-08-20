package cn.hutool.v7.cron.pattern;

import cn.hutool.v7.core.date.DateTime;
import cn.hutool.v7.core.lang.Console;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class Issue4006Test {
	@Test
	@Disabled
	void testCron() {
		final String cron = "0 0 0 */1 * ? *";
		final DateTime judgeTime = DateTime.of(new Date());
		final CronPattern cronPattern = new CronPattern(cron);

		Console.log("cronPattern = " + cronPattern);
		final Date nextDate = CronPatternUtil.nextDateAfter(cronPattern, judgeTime);
		Console.log("nextDate = " + nextDate);
	}
}
