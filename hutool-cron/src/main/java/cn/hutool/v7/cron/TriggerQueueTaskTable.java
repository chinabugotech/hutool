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

import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.cron.pattern.CronPattern;
import cn.hutool.v7.cron.task.CronTask;
import cn.hutool.v7.cron.task.Task;

import java.io.Serial;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.Lock;

/**
 * 带有触发队列的定时任务表<br>
 * 在用户添加任务时，会把下一次触发时间加入到队列中，会从队列中取出任务并执行。<br>
 * 执行任务时，会检查队列，当队列中任务的触发时间小于当前时间时，则从队列中取出并执行。<br>
 * 执行后，将下一次触发时间加入到队列中。
 *
 * @author Looly
 */
public class TriggerQueueTaskTable extends TaskTable {
	@Serial
	private static final long serialVersionUID = 1L;

	private final PriorityBlockingQueue<TriggerTime> triggerQueue;

	/**
	 * 构造, 默认容量为{@link TaskTable#DEFAULT_CAPACITY}
	 */
	public TriggerQueueTaskTable() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始容量
	 */
	public TriggerQueueTaskTable(final int initialCapacity) {
		super(initialCapacity);
		this.triggerQueue = new PriorityBlockingQueue<>(initialCapacity);
	}

	@Override
	public TriggerQueueTaskTable add(final String id, final CronPattern pattern, final Task task) {
		super.add(id, pattern, task);
		// 将下一个触发时间及任务添加到队列中
		this.triggerQueue.offer(new TriggerTime(id, pattern.nextMatchFromNow()));
		return this;
	}

	@Override
	public boolean remove(final String id) {
		// 移除队列中的任务
		this.triggerQueue.removeIf(task -> StrUtil.equals(task.id(), id));
		return super.remove(id);

	}

	@Override
	public boolean updatePattern(final String id, final CronPattern pattern) {
		// 移除队列中的任务
		this.triggerQueue.removeIf(task -> StrUtil.equals(task.id(), id));
		// 将下一个触发时间及任务添加到队列中
		this.triggerQueue.offer(new TriggerTime(id, pattern.nextMatchFromNow()));
		return super.updatePattern(id, pattern);
	}

	@Override
	public void execute(final Scheduler scheduler, final long millis) {
		final Lock readLock = lock.readLock();
		readLock.lock();
		try {
			executeTaskBeforeInternal(scheduler, millis);
		} finally {
			readLock.unlock();
		}
	}

	/**
	 * 执行给定时间戳以及之前所有的任务<br>
	 * 此方法依赖于触发队列
	 *
	 * @param scheduler {@link Scheduler}
	 * @param millis    时间毫秒
	 */
	private void executeTaskBeforeInternal(final Scheduler scheduler, final long millis) {
		while (true) {
			final TriggerTime triggerTime = this.triggerQueue.poll();
			if (null == triggerTime) {
				// 队列空
				break;
			}

			final long triggerTimestamp = triggerTime.timestamp();
			if (triggerTimestamp > millis) {
				// 任务时间未到
				this.triggerQueue.offer(triggerTime);
				break;
			}

			// 执行任务
			final String id = triggerTime.id();
			scheduler.taskManager.spawnExecutor(getCronTask(id));

			// 将下一个触发时间及任务添加到队列中
			long nextMillis = millis;
			if((millis - triggerTimestamp) / 1000 == 0){
				// 秒级别相同，说明这秒已经执行，从下一秒开始
				nextMillis += 1000;
			}
			this.triggerQueue.offer(new TriggerTime(id, getPattern(id).nextMatch(nextMillis)));
		}
	}

	/**
	 * 获取一个{@link CronTask}，无锁
	 *
	 * @param id ID
	 * @return {@link CronTask}
	 * @since 7.0.0
	 */
	private CronTask getCronTask(final String id) {
		final int index = this.table.indexOfLeft(id);
		return index > -1 ? new CronTask(id, this.table.getMiddle(index), this.table.getRight(index)) : null;
	}

	/**
	 * 触发时间
	 *
	 * @param id        ID
	 * @param timestamp 触发时间
	 */
	private record TriggerTime(String id, long timestamp) implements Comparable<TriggerTime> {
		@Override
		public int compareTo(final TriggerTime other) {
			return Long.compare(this.timestamp, other.timestamp);
		}
	}
}
