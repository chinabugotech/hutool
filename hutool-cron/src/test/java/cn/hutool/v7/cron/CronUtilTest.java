package cn.hutool.v7.cron;

import cn.hutool.v7.core.exception.HutoolException;
import cn.hutool.v7.core.io.resource.NoResourceException;
import cn.hutool.v7.core.lang.Console;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CronUtilTest {


	@Test
	void testSetMatchSecond() {
		// 测试默认是分钟模式
		assertFalse(CronUtil.getScheduler().config.isMatchSecond());

		// 设置为秒模式
		CronUtil.setMatchSecond(true);
		assertTrue(CronUtil.getScheduler().config.isMatchSecond());

		// 恢复为分钟模式
		CronUtil.setMatchSecond(false);
		assertFalse(CronUtil.getScheduler().config.isMatchSecond());
	}

	@Test
	void testSetCronSetting() {
		// 测试设置文件路径
		// 如果文件不存在，应该抛出异常
		assertThrows(NoResourceException.class, () -> CronUtil.setCronSetting("nonexistent.setting"));
	}

	@Test
	void testSchedule() {
		// 测试添加任务
		String taskId = CronUtil.schedule("0/1 * * * * ?", () -> Console.log("Task running"));
		assertNotNull(taskId);

		// 测试带ID添加任务
		String customId = "custom-task";
		CronUtil.schedule(customId, "0/1 * * * * ?", () -> Console.log("Custom task running"));
	}

	@Test
	void testRemove() {
		String taskId = CronUtil.schedule("0/1 * * * * ?", () -> Console.log("Task to remove"));
		assertTrue(CronUtil.remove(taskId));

		// 测试移除不存在的任务
		assertFalse(CronUtil.remove("nonexistent-task"));
	}

	@Test
	void testStartStop() {
		CronUtil.schedule("0/1 * * * * ?", () -> Console.log("Test task"));
		CronUtil.start();
		assertTrue(CronUtil.getScheduler().isStarted());
		CronUtil.stop();
		assertFalse(CronUtil.getScheduler().isStarted());
	}

	@Test
	@Disabled
	void testRestart() {
		CronUtil.schedule("0/1 * * * * ?", () -> Console.log("Restart test task"));
		CronUtil.start();

		// 测试重启
		CronUtil.restart();
		assertTrue(CronUtil.getScheduler().isStarted());

		CronUtil.stop();
	}

	@Test
	void testIsValidExpression() {
		// 测试有效表达式
		assertTrue(CronUtil.isValidExpression("0 0 12 * * ?"));
		assertTrue(CronUtil.isValidExpression("0/5 * * * * ?"));

		// 测试无效表达式
		assertFalse(CronUtil.isValidExpression(null));
		assertFalse(CronUtil.isValidExpression("invalid expression"));
		assertFalse(CronUtil.isValidExpression("0 0 12 * * ? * * *"));
	}

	@Test
	void testStartWhenAlreadyStarted() {
		CronUtil.start();
		assertThrows(HutoolException.class, CronUtil::start);
		CronUtil.stop();
	}
}
