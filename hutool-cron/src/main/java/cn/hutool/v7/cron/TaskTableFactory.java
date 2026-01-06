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

package cn.hutool.v7.cron;

/**
 * 任务表工厂类
 *
 * @author Looly
 */
public class TaskTableFactory {

	/**
	 * 创建任务表
	 *
	 * @param config 定时任务配置
	 * @return 任务表
	 */
	public static TaskTable create(final CronConfig config) {
		return config.isUseTriggerQueue() ? new TriggerQueueTaskTable() : new MatchTaskTable();
	}
}
