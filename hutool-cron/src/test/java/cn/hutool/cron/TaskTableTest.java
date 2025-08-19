package cn.hutool.cron;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.IdUtil;
import cn.hutool.cron.pattern.CronPattern;
import cn.hutool.cron.pattern.CronPatternUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class TaskTableTest {

	@Test
	@Disabled
	public void toStringTest(){
		final TaskTable taskTable = new TaskTable();
		taskTable.add(IdUtil.fastUUID(), new CronPattern("*/10 * * * * *"), ()-> Console.log("Task 1"));
		taskTable.add(IdUtil.fastUUID(), new CronPattern("*/20 * * * * *"), ()-> Console.log("Task 2"));
		taskTable.add(IdUtil.fastUUID(), new CronPattern("*/30 * * * * *"), ()-> Console.log("Task 3"));

		Console.log(taskTable);
	}
	@Test
	void testCron() {
//        String cron = "0 0 0 */1 * ?";
		String cron = "0 0 0 */1 * ? *";
		DateTime judgeTime = DateTime.of(new Date());
		CronPattern cronPattern = new CronPattern(cron);

		System.out.println("cronPattern = " + cronPattern);
		Date nextDate = CronPatternUtil.nextDateAfter(cronPattern, judgeTime, true);
		System.out.println("nextDate = " + nextDate);
	}
}
