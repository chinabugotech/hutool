package cn.hutool.cron;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.io.Serializable;

/**
 * 定时任务计时器<br>
 * 计时器线程每隔一分钟（一秒钟）检查一次任务列表，一旦匹配到执行对应的Task
 * @author Looly
 *
 */
public class CronTimer extends Thread implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.get();

	/** 定时单元：秒 */
	private final long TIMER_UNIT_SECOND = DateUnit.SECOND.getMillis();
	/** 定时单元：分 */
	private final long TIMER_UNIT_MINUTE = DateUnit.MINUTE.getMillis();

	/** 定时任务是否已经被强制关闭 */
	private boolean isStop;
	private final Scheduler scheduler;

	/**
	 * 构造
	 * @param scheduler {@link Scheduler}
	 */
	public CronTimer(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	@Override
	public void run() {
		final long timerUnit = this.scheduler.config.matchSecond ? TIMER_UNIT_SECOND : TIMER_UNIT_MINUTE;
		final long doubleTimeUnit = 2 * timerUnit;

		long thisTime = System.currentTimeMillis();
		long nextTime;
		long sleep;
		while(false == isStop){
			spawnLauncher(thisTime);

			//下一时间计算是按照上一个执行点开始时间计算的
			//此处除以定时单位是为了清零单位以下部分，例如单位是分则秒和毫秒清零
			nextTime = ((thisTime / timerUnit) + 1) * timerUnit;
			sleep = nextTime - System.currentTimeMillis();
			if(sleep < 0){
				// 可能循环执行慢导致时间点跟不上系统时间，追赶系统时间并执行中间差异的时间点（issue#IB49EF@Gitee）
				thisTime = System.currentTimeMillis();
				while(nextTime <= thisTime){
					// 追赶系统时间并运行执行点
					spawnLauncher(nextTime);
					nextTime = ((thisTime / timerUnit) + 1) * timerUnit;
				}
				continue;
			} else if(sleep > doubleTimeUnit){
				// 时间回退，可能用户回拨了时间或自动校准了时间，重新计算（issue#1224@Github）
				thisTime = System.currentTimeMillis();
				continue;
			} else if (false == ThreadUtil.safeSleep(sleep)) {
				//等待直到下一个时间点，如果被用户中断直接退出Timer
				break;
			}

			// issue#3460 采用叠加方式，确保正好是1分钟或1秒，避免sleep晚醒问题
			// 此处无需校验，因为每次循环都是sleep与上触发点的时间差。
			// 当上一次晚醒后，本次会减少sleep时间，保证误差在一个unit内，并不断修正。
			thisTime = nextTime;
		}
		log.debug("Hutool-cron timer stopped.");
	}

	/**
	 * 关闭定时器
	 */
	synchronized public void stopTimer() {
		this.isStop = true;
		ThreadUtil.interrupt(this, true);
	}

	/**
	 * 启动匹配
	 * @param millis 当前时间
	 */
	private void spawnLauncher(final long millis){
		//Console.log(millis / 1000, System.currentTimeMillis() / 1000);
		this.scheduler.taskLauncherManager.spawnLauncher(millis);
	}
}
