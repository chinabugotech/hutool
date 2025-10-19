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

package cn.hutool.v7.cron;

import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.core.data.id.IdUtil;
import cn.hutool.v7.cron.pattern.CronPattern;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TaskTableTest {

	@Test
	public void taskTableTest(){
		final TaskTable taskTable = new MatchTaskTable();
		taskTable.add(IdUtil.fastUUID(), new CronPattern("*/10 * * * * *"), ()-> Console.log("Task 1"));
		taskTable.add(IdUtil.fastUUID(), new CronPattern("*/20 * * * * *"), ()-> Console.log("Task 2"));
		taskTable.add(IdUtil.fastUUID(), new CronPattern("*/30 * * * * *"), ()-> Console.log("Task 3"));

		Assertions.assertEquals(3, taskTable.size());
		final List<String> ids = taskTable.getIds();
		Assertions.assertEquals(3, ids.size());

		// getById
		Assertions.assertEquals(new CronPattern("*/10 * * * * *"), taskTable.getPattern(ids.get(0)));
		Assertions.assertEquals(new CronPattern("*/20 * * * * *"), taskTable.getPattern(ids.get(1)));
		Assertions.assertEquals(new CronPattern("*/30 * * * * *"), taskTable.getPattern(ids.get(2)));

		// set test
		taskTable.updatePattern(ids.get(2), new CronPattern("*/40 * * * * *"));
		Assertions.assertEquals(new CronPattern("*/40 * * * * *"), taskTable.getPattern(ids.get(2)));

		// getTask
		Assertions.assertEquals(
			taskTable.getTask(1),
			taskTable.getTask(ids.get(1))
		);
	}
}
