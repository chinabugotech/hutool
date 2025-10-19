/*
 * Copyright (c) 2025 Hutool Team and hutool.cn
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

import cn.hutool.v7.core.collection.ListUtil;
import cn.hutool.v7.cron.task.CronTask;
import cn.hutool.v7.cron.task.Task;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 任务管理器，提供任务的全生命周期管理，提供：
 * <ul>
 *     <li>启动器管理，由CronTimer按照固定周期（每分钟或每秒钟）调用，用于检查CronTable中的任务是否匹配。</li>
 *     <li>执行器管理，由CronTable匹配后调用，用于启动具体的任务。</li>
 * </ul>
 *
 * @author Looly
 * @since 7.0.0
 */
public class TaskManager implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	protected final Scheduler scheduler;
	/**
	 * 启动器列表
	 */
	protected final List<TaskLauncher> launchers = new ArrayList<>();
	/**
	 * 执行器列表
	 */
	private final List<TaskExecutor> executors = new ArrayList<>();

	/**
	 * 构造
	 *
	 * @param scheduler {@link Scheduler}
	 */
	public TaskManager(final Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	// region ----- TaskLauncher

	/**
	 * 获取所有启动器列表，不可修改
	 *
	 * @return 启动器列表
	 */
	public List<TaskLauncher> getLaunchers() {
		return ListUtil.view(this.launchers);
	}

	/**
	 * 启动 TaskLauncher
	 *
	 * @param millis 触发事件的时间戳
	 * @return {@link TaskLauncher}
	 */
	protected TaskLauncher spawnLauncher(final long millis) {
		final TaskLauncher launcher = new TaskLauncher(this.scheduler, millis);
		synchronized (this.launchers) {
			this.launchers.add(launcher);
		}
		this.scheduler.threadExecutor.execute(launcher);
		return launcher;
	}

	/**
	 * 启动器启动完毕，启动完毕后从执行器列表中移除
	 *
	 * @param launcher 启动器 {@link TaskLauncher}
	 */
	protected void notifyLauncherCompleted(final TaskLauncher launcher) {
		synchronized (launchers) {
			launchers.remove(launcher);
		}
	}
	// endregion

	// region ----- TaskExecutor

	/**
	 * 获取所有正在执行的任务调度执行器，不可修改
	 *
	 * @return 任务执行器列表
	 * @since 4.6.7
	 */
	public List<TaskExecutor> getExecutors() {
		return ListUtil.view(this.executors);
	}

	/**
	 * 启动 执行器TaskExecutor，即启动作业
	 *
	 * @param task {@link Task}
	 * @return {@link TaskExecutor}
	 */
	protected TaskExecutor spawnExecutor(final CronTask task) {
		final TaskExecutor executor = new TaskExecutor(this.scheduler, task);
		synchronized (this.executors) {
			this.executors.add(executor);
		}
		this.scheduler.threadExecutor.execute(executor);
		return executor;
	}

	/**
	 * 执行器执行完毕调用此方法，将执行器从执行器列表移除，此方法由{@link TaskExecutor}对象调用，用于通知管理器自身已完成执行
	 *
	 * @param executor 执行器 {@link TaskExecutor}
	 */
	protected void notifyExecutorCompleted(final TaskExecutor executor) {
		synchronized (executors) {
			executors.remove(executor);
		}
	}
	// endregion
}
