package cn.hutool.v7.cron.demo;

import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.cron.CronConfig;
import cn.hutool.v7.cron.Scheduler;

public class TriggerQueueTest {
	public static void main(final String[] args) {
		final Scheduler scheduler = new Scheduler(CronConfig.of().setMatchSecond(true).setUseTriggerQueue(true));
		scheduler.schedule("*/10 * * * * *",
			() -> Console.log("Hutool task */10 running at: [{}]", System.currentTimeMillis()));
		scheduler.schedule("*/3 * * * * *",
			() -> Console.log("Hutool task */3 running at: [{}]", System.currentTimeMillis()));

		scheduler.start();
	}
}
