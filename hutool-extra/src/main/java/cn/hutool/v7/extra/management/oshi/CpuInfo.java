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

import cn.hutool.v7.core.math.NumberUtil;
import oshi.hardware.CentralProcessor;

import java.text.DecimalFormat;

/**
 * <p>CPU相关信息</p>
 *
 * @author Dai Yuanchuan
 **/
public class CpuInfo {

	private static final DecimalFormat LOAD_FORMAT = new DecimalFormat("#.00");

	/**
	 * CPU核心数
	 */
	private Integer cpuNum;

	/**
	 * CPU总的使用率
	 */
	private double toTal;

	/**
	 * CPU系统使用率
	 */
	private double sys;

	/**
	 * CPU用户使用率
	 */
	private double user;

	/**
	 * CPU当前等待率
	 */
	private double wait;

	/**
	 * CPU当前空闲率
	 */
	private double free;

	/**
	 * CPU型号信息
	 */
	private String cpuModel;

	/**
	 * CPU型号信息
	 */
	private CpuTicks ticks;

	/**
	 * 空构造
	 */
	public CpuInfo() {
	}

	/**
	 * 构造，等待时间为用于计算在一定时长内的CPU负载情况，如传入1000表示最近1秒的负载情况
	 *
	 * @param processor   {@link CentralProcessor}
	 * @param waitingTime 设置等待时间，单位毫秒
	 */
	public CpuInfo(final CentralProcessor processor, final long waitingTime) {
		init(processor, waitingTime);
	}

	/**
	 * 构造
	 *
	 * @param cpuNum   CPU核心数
	 * @param toTal    CPU总的使用率
	 * @param sys      CPU系统使用率
	 * @param user     CPU用户使用率
	 * @param wait     CPU当前等待率
	 * @param free     CPU当前空闲率
	 * @param cpuModel CPU型号信息
	 */
	public CpuInfo(final Integer cpuNum, final double toTal, final double sys, final double user, final double wait, final double free, final String cpuModel) {
		this.cpuNum = cpuNum;
		this.toTal = toTal;
		this.sys = sys;
		this.user = user;
		this.wait = wait;
		this.free = free;
		this.cpuModel = cpuModel;
	}

	/**
	 * 获取CPU核心数
	 *
	 * @return CPU核心数
	 */
	public Integer getCpuNum() {
		return cpuNum;
	}

	/**
	 * 设置CPU核心数
	 *
	 * @param cpuNum CPU核心数
	 * @return this
	 */
	public CpuInfo setCpuNum(final Integer cpuNum) {
		this.cpuNum = cpuNum;
		return this;
	}

	/**
	 * 获取CPU总使用率
	 *
	 * @return CPU总使用率
	 */
	public double getToTal() {
		return toTal;
	}

	/**
	 * 设置CPU总使用率
	 *
	 * @param toTal CPU总使用率
	 * @return this
	 */
	public CpuInfo setToTal(final double toTal) {
		this.toTal = toTal;
		return this;
	}

	/**
	 * 获取CPU系统使用率
	 *
	 * @return CPU系统使用率
	 */
	public double getSys() {
		return sys;
	}

	/**
	 * 设置CPU系统使用率
	 *
	 * @param sys CPU系统使用率
	 * @return this
	 */
	public CpuInfo setSys(final double sys) {
		this.sys = sys;
		return this;
	}

	/**
	 * 获取CPU用户使用率
	 *
	 * @return CPU用户使用率
	 */
	public double getUser() {
		return user;
	}

	/**
	 * 设置CPU用户使用率
	 *
	 * @param user CPU用户使用率
	 * @return this
	 */
	public CpuInfo setUser(final double user) {
		this.user = user;
		return this;
	}

	/**
	 * 获取CPU当前等待率
	 *
	 * @return CPU当前等待率
	 */
	public double getWait() {
		return wait;
	}

	/**
	 * 设置CPU当前等待率
	 *
	 * @param wait CPU当前等待率
	 * @return this
	 */
	public CpuInfo setWait(final double wait) {
		this.wait = wait;
		return this;
	}

	/**
	 * 获取CPU当前空闲率
	 *
	 * @return CPU当前空闲率
	 */
	public double getFree() {
		return free;
	}

	/**
	 * 设置CPU当前空闲率
	 *
	 * @param free CPU当前空闲率
	 * @return this
	 */
	public CpuInfo setFree(final double free) {
		this.free = free;
		return this;
	}

	/**
	 * 获取CPU型号信息
	 *
	 * @return CPU型号信息
	 */
	public String getCpuModel() {
		return cpuModel;
	}

	/**
	 * 设置CPU型号信息
	 *
	 * @param cpuModel CPU型号信息
	 * @return this
	 */
	public CpuInfo setCpuModel(final String cpuModel) {
		this.cpuModel = cpuModel;
		return this;
	}

	/**
	 * 获取CPU ticks
	 *
	 * @return CPU ticks
	 */
	public CpuTicks getTicks() {
		return ticks;
	}

	/**
	 * 设置CPU ticks
	 *
	 * @param ticks CPU ticks
	 * @return this
	 */
	public CpuInfo setTicks(final CpuTicks ticks) {
		this.ticks = ticks;
		return this;
	}

	/**
	 * 获取用户+系统的总的CPU使用率
	 *
	 * @return 总CPU使用率
	 */
	public double getUsed() {
		return NumberUtil.sub(100, this.free).doubleValue();
	}

	@Override
	public String toString() {
		return "CpuInfo{" +
			"CPU核心数=" + cpuNum +
			", CPU总的使用率=" + toTal +
			", CPU系统使用率=" + sys +
			", CPU用户使用率=" + user +
			", CPU当前等待率=" + wait +
			", CPU当前空闲率=" + free +
			", CPU利用率=" + getUsed() +
			", CPU型号信息='" + cpuModel + '\'' +
			'}';
	}

	/**
	 * 获取指定等待时间内系统CPU 系统使用率、用户使用率、利用率等等 相关信息
	 *
	 * @param processor   {@link CentralProcessor}
	 * @param waitingTime 设置等待时间，单位毫秒
	 * @since 5.7.12
	 */
	private void init(final CentralProcessor processor, final long waitingTime) {
		final CpuTicks ticks = new CpuTicks(processor, waitingTime);
		this.ticks = ticks;

		this.cpuNum = processor.getLogicalProcessorCount();
		this.cpuModel = processor.toString();

		final long totalCpu = ticks.totalCpu();
		this.toTal = totalCpu;
		this.sys = formatDouble(ticks.cSys, totalCpu);
		this.user = formatDouble(ticks.user, totalCpu);
		this.wait = formatDouble(ticks.ioWait, totalCpu);
		this.free = formatDouble(ticks.idle, totalCpu);
	}

	/**
	 * 获取每个CPU核心的tick，计算方式为 100 * tick / totalCpu
	 *
	 * @param tick     tick
	 * @param totalCpu CPU总数
	 * @return 平均每个CPU核心的tick
	 * @since 5.7.12
	 */
	private static double formatDouble(final long tick, final long totalCpu) {
		if (0 == totalCpu) {
			return 0D;
		}
		return Double.parseDouble(LOAD_FORMAT.format(tick <= 0 ? 0 : (100d * tick / totalCpu)));
	}
}
