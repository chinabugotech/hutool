package cn.hutool.cron.pattern;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.Console;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class Issue4006Test {
	@Test
	void testCron() {
//        String cron = "0 0 0 */1 * ?";
		String cron = "0 0 0 */1 * ? *";
		DateTime judgeTime = DateTime.of(new Date());
		CronPattern cronPattern = new CronPattern(cron);

		Console.log("cronPattern = " + cronPattern);
		Date nextDate = CronPatternUtil.nextDateAfter(cronPattern, judgeTime, true);
		Console.log("nextDate = " + nextDate);
	}
}
