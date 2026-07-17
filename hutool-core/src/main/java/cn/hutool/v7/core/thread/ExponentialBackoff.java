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

package cn.hutool.v7.core.thread;

import cn.hutool.v7.core.lang.Assert;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 指数退避策略（Exponential Backoff），延迟按倍数递增并可叠加随机抖动。
 * <p>
 * 计算公式：{@code delay = min(initialDelay * multiplier^(attempt-1), maxDelay)}，
 * 启用抖动时在 {@code [0, delay)} 范围内随机取值（Full Jitter）。
 * </p>
 * <p>
 * 典型用法：
 * <pre>{@code
 * RetryableTask.retryForExceptions(() -> callApi(), IOException.class)
 *     .maxAttempts(5)
 *     .backoff(new ExponentialBackoff(Duration.ofMillis(500), 2.0, Duration.ofSeconds(30), true))
 *     .execute();
 * }</pre>
 * </p>
 *
 * @author dong0713
 * @see FixedBackoff
 * @since 7.0.0
 */
public class ExponentialBackoff implements Backoff {

	private final Duration initialDelay;
	private final double multiplier;
	private final Duration maxDelay;
	private final boolean jitter;

	/**
	 * 构造指数退避策略
	 *
	 * @param initialDelay 初始延迟，不为 {@code null}，且必须为正数
	 * @param multiplier   倍数，必须 &ge; 1.0
	 * @param maxDelay     最大延迟上限，不为 {@code null}，且必须 &ge; initialDelay
	 * @param jitter       是否启用随机抖动（Full Jitter）
	 */
	public ExponentialBackoff(final Duration initialDelay, final double multiplier,
							  final Duration maxDelay, final boolean jitter) {
		Assert.notNull(initialDelay, "initialDelay must not be null");
		Assert.isTrue(!initialDelay.isNegative() && !initialDelay.isZero(),
			"initialDelay must be positive");
		Assert.isTrue(multiplier >= 1.0, "multiplier must be >= 1.0");
		Assert.notNull(maxDelay, "maxDelay must not be null");
		Assert.isTrue(maxDelay.compareTo(initialDelay) >= 0,
			"maxDelay must be >= initialDelay");

		this.initialDelay = initialDelay;
		this.multiplier = multiplier;
		this.maxDelay = maxDelay;
		this.jitter = jitter;
	}

	@Override
	public Duration nextDelay(final int attempt) {
		// 指数计算：initialDelay * multiplier^(attempt-1)
		final long initialMillis = this.initialDelay.toMillis();
		final long maxMillis = this.maxDelay.toMillis();
		final long rawMillis = (long) (initialMillis * Math.pow(this.multiplier, attempt - 1));

		// 溢出保护与上限截断
		final long cappedMillis = (rawMillis <= 0 || rawMillis > maxMillis) ? maxMillis : rawMillis;

		// Full Jitter：在 [0, cappedMillis] 范围内随机
		if (this.jitter && cappedMillis > 0) {
			return Duration.ofMillis(ThreadLocalRandom.current().nextLong(cappedMillis + 1));
		}
		return Duration.ofMillis(cappedMillis);
	}
}
