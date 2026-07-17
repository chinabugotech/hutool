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

package cn.hutool.v7.core.thread.retry;

import cn.hutool.v7.core.lang.Assert;

import java.time.Duration;

/**
 * 固定延迟退避策略，每次重试等待相同的时间。
 * <p>
 * 等价于 {@link RetryableTask#delay(Duration)} 的行为，用于与 {@link Backoff} 体系统一。
 * </p>
 *
 * @author dong0713
 * @see ExponentialBackoff
 * @since 7.0.0
 */
public class FixedBackoff implements Backoff {

	private final Duration delay;

	/**
	 * 构造固定退避策略
	 *
	 * @param delay 固定等待时间，不为 {@code null}，且必须为正数
	 */
	public FixedBackoff(final Duration delay) {
		Assert.notNull(delay, "delay must not be null");
		Assert.isTrue(!delay.isNegative() && !delay.isZero(), "delay must be positive");
		this.delay = delay;
	}

	@Override
	public Duration nextDelay(final int attempt) {
		return this.delay;
	}
}
