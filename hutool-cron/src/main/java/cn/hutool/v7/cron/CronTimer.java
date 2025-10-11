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

import cn.hutool.v7.core.date.DateUnit;
import cn.hutool.v7.core.thread.ThreadUtil;
import cn.hutool.v7.log.Log;

import java.io.Serial;
import java.io.Serializable;

/**
 * 定时任务计时器<br>
 * 计时器线程每隔一分钟（一秒钟）检查一次任务列表，一旦匹配到执行对应的Task
 * @author Looly
 *
 */
public class CronTimer extends Thread implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	private static final Log log = Log.get();

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
	public CronTimer(final Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	@Override
	public void run() {
		final long timerUnit = this.scheduler.config.matchSecond ? TIMER_UNIT_SECOND : TIMER_UNIT_MINUTE;
		final long doubleTimeUnit = 2 * timerUnit;

		long thisTime = System.currentTimeMillis();
		while(!isStop){
			spawnLauncher(thisTime);

			//下一时间计算是按照上一个执行点开始时间计算的
			//此处除以定时单位是为了清零单位以下部分，例如单位是分则秒和毫秒清零
			long nextTime = ((thisTime / timerUnit) + 1) * timerUnit;
			final long sleep = nextTime - System.currentTimeMillis();
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
			} else if (!ThreadUtil.safeSleep(sleep)) {
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
		this.scheduler.taskManager.spawnLauncher(millis);
	}

	/**
	 * 检查是否为有效的sleep毫秒数，包括：
	 * <pre>
	 *     1. 是否&gt;0，防止用户向未来调整时间
	 *     1. 是否&lt;两倍的间隔单位，防止用户向历史调整时间
	 * </pre>
	 *
	 * @param millis 毫秒数
	 * @param timerUnit 定时单位，为秒或者分的毫秒值
	 * @return 是否为有效的sleep毫秒数
	 * @since 5.3.2
	 */
	private static boolean isValidSleepMillis(final long millis, final long timerUnit){
		return millis > 0 &&
				// 防止用户向前调整时间导致的长时间sleep
				millis < (2 * timerUnit);
	}
}
