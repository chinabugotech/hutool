/*
 * Copyright (c) 2013-2025 Hutool Team and hutool.cn
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

package cn.hutool.v7.cron.demo;

import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.core.thread.ThreadUtil;
import cn.hutool.v7.cron.CronUtil;
import cn.hutool.v7.cron.TaskExecutor;
import cn.hutool.v7.cron.listener.TaskListener;
import cn.hutool.v7.cron.task.Task;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * 定时任务样例
 */
public class CronTest {

	@Test
	@Disabled
	public void customCronTest() {
		CronUtil.schedule("*/2 * * * * *", (Task) () -> Console.log("Task executed."));

		// 支持秒级别定时任务
		CronUtil.setMatchSecond(true);
		CronUtil.start();

		ThreadUtil.waitForDie();
		Console.log("Exit.");
	}

	@Test
	@Disabled
	public void cronTest() {
		// 支持秒级别定时任务
		CronUtil.setMatchSecond(true);
		CronUtil.getScheduler().setDaemon(false);
		CronUtil.start();

		ThreadUtil.waitForDie();
		CronUtil.stop();
	}

	@Test
	@Disabled
	public void cronWithListenerTest() {
		CronUtil.getScheduler().addListener(new TaskListener() {
			@Override
			public void onStart(final TaskExecutor executor) {
				Console.log("Found task:[{}] start!", executor.cronTask().getId());
			}

			@Override
			public void onSucceeded(final TaskExecutor executor) {
				Console.log("Found task:[{}] success!", executor.cronTask().getId());
			}

			@Override
			public void onFailed(final TaskExecutor executor, final Throwable exception) {
				Console.error("Found task:[{}] failed!", executor.cronTask().getId());
			}
		});

		// 支持秒级别定时任务
		CronUtil.setMatchSecond(true);
		CronUtil.start();

		ThreadUtil.waitForDie();
		Console.log("Exit.");
	}

	@Test
	@Disabled
	public void addAndRemoveTest() {
		final String id = CronUtil.schedule("*/2 * * * * *",
			() -> Console.log("task running : 2s"));

		Console.log(id);
		CronUtil.remove(id);

		// 支持秒级别定时任务
		CronUtil.setMatchSecond(true);
		CronUtil.start();
	}

	@Test
	@Disabled
	public void isValidExpressionTest() {
		Console.log(CronUtil.isValidExpression("5 * * * *"));
		Console.log(CronUtil.isValidExpression("3-18 5 * * * *"));
		Console.log(CronUtil.isValidExpression(""));
		Console.log(CronUtil.isValidExpression(null));
		Console.log(CronUtil.isValidExpression("A B C D E F"));

	}
}
