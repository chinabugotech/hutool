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

package cn.hutool.v7.extra.management.oshi;

import oshi.hardware.CentralProcessor;
import oshi.util.Util;

/**
 * CPU负载时间信息
 *
 * @author Looly
 * @since 5.7.12
 */
public class CpuTicks {

	/**
	 * 空闲时间，表示CPU没有执行任何任务的时间
	 */
	long idle;

	/**
	 * 优先级较低的进程时间，表示CPU在执行优先级较低的进程时所花费的时间
	 */
	long nice;

	/**
	 * 中断请求时间，表示CPU处理中断请求所花费的时间
	 */
	long irq;

	/**
	 * 软中断请求时间，表示CPU处理软中断请求所花费的时间
	 */
	long softIrq;

	/**
	 * 被虚拟机偷走的时间，表示在虚拟化环境中，CPU被其他虚拟机使用的时间
	 */
	long steal;

	/**
	 * 系统时间，表示CPU在执行操作系统内核指令时所花费的时间
	 */
	long cSys;

	/**
	 * 用户时间，表示CPU在执行用户进程时所花费的时间
	 */
	long user;

	/**
	 * I/O等待时间，表示CPU等待I/O操作完成所花费的时间
	 */
	long ioWait;


	/**
	 * 构造，等待时间为用于计算在一定时长内的CPU负载情况，如传入1000表示最近1秒的负载情况
	 *
	 * @param processor   {@link CentralProcessor}
	 * @param waitingTime 设置等待时间，单位毫秒
	 */
	public CpuTicks(final CentralProcessor processor, final long waitingTime) {
		// CPU信息
		final long[] prevTicks = processor.getSystemCpuLoadTicks();
		// 这里必须设置延迟
		Util.sleep(waitingTime);
		final long[] ticks = processor.getSystemCpuLoadTicks();

		this.idle = tick(prevTicks, ticks, CentralProcessor.TickType.IDLE);
		this.nice = tick(prevTicks, ticks, CentralProcessor.TickType.NICE);
		this.irq = tick(prevTicks, ticks, CentralProcessor.TickType.IRQ);
		this.softIrq = tick(prevTicks, ticks, CentralProcessor.TickType.SOFTIRQ);
		this.steal = tick(prevTicks, ticks, CentralProcessor.TickType.STEAL);
		this.cSys = tick(prevTicks, ticks, CentralProcessor.TickType.SYSTEM);
		this.user = tick(prevTicks, ticks, CentralProcessor.TickType.USER);
		this.ioWait = tick(prevTicks, ticks, CentralProcessor.TickType.IOWAIT);
	}

	/**
	 * 获取CPU空闲时间
	 *
	 * @return CPU空闲时间
	 */
	public long getIdle() {
		return idle;
	}

	/**
	 * 设置CPU空闲时间
	 *
	 * @param idle CPU空闲时间
	 * @return 当前对象实例，用于链式调用
	 */
	public CpuTicks setIdle(final long idle) {
		this.idle = idle;
		return this;
	}

	/**
	 * 获取CPU在nice模式下花费的时间
	 *
	 * @return CPU在nice模式下花费的时间
	 */
	public long getNice() {
		return nice;
	}

	/**
	 * 设置CPU在nice模式下花费的时间
	 *
	 * @param nice CPU在nice模式下花费的时间
	 * @return 当前对象实例，用于链式调用
	 */
	public CpuTicks setNice(final long nice) {
		this.nice = nice;
		return this;
	}

	/**
	 * 获取CPU在处理中断请求上花费的时间
	 *
	 * @return CPU在处理中断请求上花费的时间
	 */
	public long getIrq() {
		return irq;
	}

	/**
	 * 设置CPU在处理中断请求上花费的时间
	 *
	 * @param irq CPU在处理中断请求上花费的时间
	 * @return 当前对象实例，用于链式调用
	 */
	public CpuTicks setIrq(final long irq) {
		this.irq = irq;
		return this;
	}

	/**
	 * 获取CPU在处理软中断上花费的时间
	 *
	 * @return CPU在处理软中断上花费的时间
	 */
	public long getSoftIrq() {
		return softIrq;
	}

	/**
	 * 设置CPU在处理软中断上花费的时间
	 *
	 * @param softIrq CPU在处理软中断上花费的时间
	 * @return 当前对象实例，用于链式调用
	 */
	public CpuTicks setSoftIrq(final long softIrq) {
		this.softIrq = softIrq;
		return this;
	}

	/**
	 * 获取CPU被其他虚拟处理器占用的时间
	 *
	 * @return CPU被其他虚拟处理器占用的时间
	 */
	public long getSteal() {
		return steal;
	}

	/**
	 * 设置CPU被其他虚拟处理器占用的时间
	 *
	 * @param steal CPU被其他虚拟处理器占用的时间
	 * @return 当前对象实例，用于链式调用
	 */
	public CpuTicks setSteal(final long steal) {
		this.steal = steal;
		return this;
	}

	/**
	 * 获取CPU在系统模式下花费的时间
	 *
	 * @return CPU在系统模式下花费的时间
	 */
	public long getcSys() {
		return cSys;
	}

	/**
	 * 设置CPU在系统模式下花费的时间
	 *
	 * @param cSys CPU在系统模式下花费的时间
	 * @return 当前对象实例，用于链式调用
	 */
	public CpuTicks setcSys(final long cSys) {
		this.cSys = cSys;
		return this;
	}

	/**
	 * 获取CPU在用户模式下花费的时间
	 *
	 * @return CPU在用户模式下花费的时间
	 */
	public long getUser() {
		return user;
	}

	/**
	 * 设置CPU在用户模式下花费的时间
	 *
	 * @param user CPU在用户模式下花费的时间
	 * @return 当前对象实例，用于链式调用
	 */
	public CpuTicks setUser(final long user) {
		this.user = user;
		return this;
	}

	/**
	 * 获取CPU在等待I/O完成上花费的时间
	 *
	 * @return CPU在等待I/O完成上花费的时间
	 */
	public long getIoWait() {
		return ioWait;
	}

	/**
	 * 设置CPU在等待I/O完成上花费的时间
	 *
	 * @param ioWait CPU在等待I/O完成上花费的时间
	 * @return 当前对象实例，用于链式调用
	 */
	public CpuTicks setIoWait(final long ioWait) {
		this.ioWait = ioWait;
		return this;
	}


	/**
	 * 获取CPU总的使用率
	 *
	 * @return CPU总使用率
	 */
	public long totalCpu() {
		return Math.max(user + nice + cSys + idle + ioWait + irq + softIrq + steal, 0);
	}

	@Override
	public String toString() {
		return "CpuTicks{" +
			"idle=" + idle +
			", nice=" + nice +
			", irq=" + irq +
			", softIrq=" + softIrq +
			", steal=" + steal +
			", cSys=" + cSys +
			", user=" + user +
			", ioWait=" + ioWait +
			'}';
	}

	/**
	 * 获取一段时间内的CPU负载标记差
	 *
	 * @param prevTicks 开始的ticks
	 * @param ticks     结束的ticks
	 * @param tickType  tick类型
	 * @return 标记差
	 * @since 5.7.12
	 */
	private static long tick(final long[] prevTicks, final long[] ticks, final CentralProcessor.TickType tickType) {
		return ticks[tickType.getIndex()] - prevTicks[tickType.getIndex()];
	}
}
