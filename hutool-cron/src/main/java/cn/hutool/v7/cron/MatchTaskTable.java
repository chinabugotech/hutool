package cn.hutool.v7.cron;

import cn.hutool.v7.cron.task.CronTask;

import java.util.concurrent.locks.Lock;

/**
 * 基于匹配的任务表<br>
 * 每次检查任务表中的表达式是否匹配指定时间，如果匹配则执行相应的Task
 *
 * @author Looly
 */
public class MatchTaskTable extends TaskTable {

	/**
	 * 构造, 默认容量为{@link TaskTable#DEFAULT_CAPACITY}
	 */
	public MatchTaskTable() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始容量
	 */
	public MatchTaskTable(final int initialCapacity) {
		super(initialCapacity);
	}

	@Override
	public void execute(final Scheduler scheduler, final long millis) {
		final Lock readLock = lock.readLock();
		readLock.lock();
		try {
			executeTaskIfMatchInternal(scheduler, millis);
		} finally {
			readLock.unlock();
		}
	}

	/**
	 * 如果时间匹配则执行相应的Task，无锁
	 *
	 * @param scheduler {@link Scheduler}
	 * @param millis    时间毫秒
	 * @since 3.1.1
	 */
	private void executeTaskIfMatchInternal(final Scheduler scheduler, final long millis) {
		final int size = size();
		for (int i = 0; i < size; i++) {
			if (this.table.getMiddle(i).match(scheduler.config.getTimeZone(), millis, scheduler.config.isMatchSecond())) {
				scheduler.taskManager.spawnExecutor(
					new CronTask(this.table.getLeft(i), this.table.getMiddle(i), this.table.getRight(i)));
			}
		}
	}
}
