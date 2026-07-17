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

import java.time.Duration;

/**
 * 退避策略接口，用于计算每次重试前的等待时间。
 * <p>
 * 配合 {@link RetryableTask#backoff(Backoff)} 使用，可实现固定退避、指数退避等多种策略。
 * </p>
 *
 * @author dong0713
 * @see FixedBackoff
 * @see ExponentialBackoff
 * @since 7.0.0
 */
@FunctionalInterface
public interface Backoff {

	/**
	 * 根据当前重试次数计算下一次重试前的等待时间
	 *
	 * @param attempt 当前重试次数（从 1 开始，1 表示第一次重试前的等待）
	 * @return 等待时间，不为 {@code null}
	 */
	Duration nextDelay(int attempt);
}
