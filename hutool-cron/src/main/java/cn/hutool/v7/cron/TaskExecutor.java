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

import cn.hutool.v7.cron.task.CronTask;

/**
 * 作业执行器<br>
 * 执行具体的作业，执行完毕销毁<br>
 * 作业执行器唯一关联一个作业，负责管理作业的运行的生命周期。
 *
 * @author Looly
 * @param scheduler 调度器
 * @param cronTask 被执行的任务
 */
public record TaskExecutor(Scheduler scheduler, CronTask cronTask) implements Runnable {

	@Override
	public void run() {
		try {
			scheduler.listenerManager.notifyTaskStart(this);
			cronTask.execute();
			scheduler.listenerManager.notifyTaskSucceeded(this);
		} catch (final Exception e) {
			scheduler.listenerManager.notifyTaskFailed(this, e);
		} finally {
			scheduler.taskManager.notifyExecutorCompleted(this);
		}
	}
}
