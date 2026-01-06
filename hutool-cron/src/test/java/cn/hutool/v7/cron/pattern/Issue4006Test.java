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
