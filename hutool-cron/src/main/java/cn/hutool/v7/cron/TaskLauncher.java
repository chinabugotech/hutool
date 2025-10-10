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

/**
 * 作业启动器<br>
 * 负责检查 {@link TaskTable} 是否有匹配到此时运行的Task<br>
 * 检查完毕后启动器结束
 *
 * @author Looly
 * @param scheduler 调度器
 * @param millis 毫秒数
 */
public record TaskLauncher(Scheduler scheduler, long millis) implements Runnable {

	@Override
	public void run() {
		//匹配秒部分由用户定义决定，始终不匹配年
		scheduler.taskTable.executeTaskIfMatch(this.scheduler, this.millis);

		//结束通知
		scheduler.taskManager.notifyLauncherCompleted(this);
	}
}
