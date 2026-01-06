/*
 * Copyright (c) 2013-2026 Hutool Team.
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
