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

import java.util.TimeZone;

/**
 * 定时任务配置类
 *
 * @author Looly
 * @since 5.4.7
 */
public class CronConfig {

	/**
	 * 创建Cron配置
	 *
	 * @return Cron配置
	 */
	public static CronConfig of(){
		return new CronConfig();
	}

	/**
	 * 时区
	 */
	private TimeZone timezone = TimeZone.getDefault();
	/**
	 * 是否支持秒匹配
	 */
	private boolean matchSecond;
	/**
	 * 是否为守护线程
	 */
	private boolean daemon;
	/**
	 * 是否使用触发队列
	 */
	private boolean useTriggerQueue;

	/**
	 * 构造
	 */
	public CronConfig() {
	}

	/**
	 * 获得时区，默认为 {@link TimeZone#getDefault()}
	 *
	 * @return 时区
	 */
	public TimeZone getTimeZone() {
		return this.timezone;
	}

	/**
	 * 设置时区
	 *
	 * @param timezone 时区
	 * @return this
	 */
	public CronConfig setTimeZone(final TimeZone timezone) {
		this.timezone = timezone;
		return this;
	}

	/**
	 * 是否支持秒匹配
	 *
	 * @return {@code true}使用，{@code false}不使用
	 */
	public boolean isMatchSecond() {
		return this.matchSecond;
	}

	/**
	 * 设置是否支持秒匹配，默认不使用
	 *
	 * @param isMatchSecond {@code true}支持，{@code false}不支持
	 * @return this
	 */
	public CronConfig setMatchSecond(final boolean isMatchSecond) {
		this.matchSecond = isMatchSecond;
		return this;
	}

	/**
	 * 是否为守护线程
	 *
	 * @return {@code true}守护线程，{@code false}非守护线程
	 */
	public boolean isDaemon() {
		return this.daemon;
	}

	/**
	 * 设置是否为守护线程
	 *
	 * @param daemon {@code true}守护线程，{@code false}非守护线程
	 * @return this
	 */
	public CronConfig setDaemon(final boolean daemon) {
		this.daemon = daemon;
		return this;
	}

	/**
	 * 是否使用触发队列
	 *
	 * @return {@code true}使用，{@code false}不使用
	 */
	public boolean isUseTriggerQueue() {
		return this.useTriggerQueue;
	}

	/**
	 * 设置是否使用触发队列<br>
	 * {@code true}则使用对接方式触发，此时会预先将任务的下一次触发时间加入队列，队列中任务的触发时间小于当前时间时，则从队列中取出并执行。<br>
	 * {@code false}则使用普通方式触发，此时会检查任务表，当任务表中的表达式匹配指定时间时，则执行相应的Task。
	 *
	 * @param useTriggerQueue {@code true}使用，{@code false}不使用
	 * @return this
	 */
	public CronConfig setUseTriggerQueue(final boolean useTriggerQueue) {
		this.useTriggerQueue = useTriggerQueue;
		return this;
	}
}
