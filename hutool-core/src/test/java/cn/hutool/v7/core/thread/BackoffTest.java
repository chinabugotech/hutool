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

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link Backoff}、{@link FixedBackoff}、{@link ExponentialBackoff} 单元测试
 */
public class BackoffTest {

	// region ----- FixedBackoff

	@Test
	void fixedBackoffReturnsConstantDelay() {
		final Backoff backoff = new FixedBackoff(Duration.ofMillis(200));
		for (int i = 1; i <= 5; i++) {
			assertEquals(Duration.ofMillis(200), backoff.nextDelay(i));
		}
	}

	@Test
	void fixedBackoffNullDelayShouldThrow() {
		assertThrows(IllegalArgumentException.class, () -> new FixedBackoff(null));
	}

	@Test
	void fixedBackoffZeroDelayShouldThrow() {
		assertThrows(IllegalArgumentException.class, () -> new FixedBackoff(Duration.ZERO));
	}

	@Test
	void fixedBackoffNegativeDelayShouldThrow() {
		assertThrows(IllegalArgumentException.class, () -> new FixedBackoff(Duration.ofMillis(-1)));
	}

	// endregion

	// region ----- ExponentialBackoff (无抖动)

	@Test
	void exponentialBackoffNoJitter() {
		final Backoff backoff = new ExponentialBackoff(
			Duration.ofMillis(100), 2.0, Duration.ofSeconds(10), false);
		// attempt 1: 100 * 2^0 = 100ms
		assertEquals(Duration.ofMillis(100), backoff.nextDelay(1));
		// attempt 2: 100 * 2^1 = 200ms
		assertEquals(Duration.ofMillis(200), backoff.nextDelay(2));
		// attempt 3: 100 * 2^2 = 400ms
		assertEquals(Duration.ofMillis(400), backoff.nextDelay(3));
		// attempt 4: 100 * 2^3 = 800ms
		assertEquals(Duration.ofMillis(800), backoff.nextDelay(4));
	}

	@Test
	void exponentialBackoffCappedByMaxDelay() {
		final Backoff backoff = new ExponentialBackoff(
			Duration.ofMillis(100), 2.0, Duration.ofMillis(500), false);
		// attempt 4: 100 * 2^3 = 800 → 截断为 500
		assertEquals(Duration.ofMillis(500), backoff.nextDelay(4));
		// attempt 10: 远超上限 → 500
		assertEquals(Duration.ofMillis(500), backoff.nextDelay(10));
	}

	// endregion

	// region ----- ExponentialBackoff (有抖动)

	@Test
	void exponentialBackoffWithJitterInRange() {
		final Backoff backoff = new ExponentialBackoff(
			Duration.ofMillis(1000), 2.0, Duration.ofSeconds(30), true);
		// attempt 1: 上限 1000ms，抖动后应在 [0, 1000]
		for (int i = 0; i < 50; i++) {
			final long millis = backoff.nextDelay(1).toMillis();
			assertTrue(millis >= 0 && millis <= 1000,
				"jitter value out of range: " + millis);
		}
	}

	@Test
	void exponentialBackoffWithJitterCappedByMaxDelay() {
		final Backoff backoff = new ExponentialBackoff(
			Duration.ofMillis(100), 2.0, Duration.ofMillis(500), true);
		// attempt 4: 计算值 800 → 截断 500 → 抖动 [0, 500]
		for (int i = 0; i < 50; i++) {
			final long millis = backoff.nextDelay(4).toMillis();
			assertTrue(millis >= 0 && millis <= 500,
				"jitter value out of range: " + millis);
		}
	}

	// endregion

	// region ----- ExponentialBackoff 参数校验

	@Test
	void exponentialBackoffNullInitialDelayShouldThrow() {
		assertThrows(IllegalArgumentException.class,
			() -> new ExponentialBackoff(null, 2.0, Duration.ofSeconds(10), false));
	}

	@Test
	void exponentialBackoffZeroInitialDelayShouldThrow() {
		assertThrows(IllegalArgumentException.class,
			() -> new ExponentialBackoff(Duration.ZERO, 2.0, Duration.ofSeconds(10), false));
	}

	@Test
	void exponentialBackoffMultiplierLessThanOneShouldThrow() {
		assertThrows(IllegalArgumentException.class,
			() -> new ExponentialBackoff(Duration.ofMillis(100), 0.5, Duration.ofSeconds(10), false));
	}

	@Test
	void exponentialBackoffMaxDelayLessThanInitialShouldThrow() {
		assertThrows(IllegalArgumentException.class,
			() -> new ExponentialBackoff(Duration.ofMillis(1000), 2.0, Duration.ofMillis(500), false));
	}

	// endregion

	// region ----- RetryableTask setter 校验修复验证

	@Test
	void maxAttemptsZeroShouldThrow() {
		// 修复前：校验 this.maxAttempts（默认3）永远通过，0 被接受；修复后校验入参
		assertThrows(IllegalArgumentException.class,
			() -> RetryableTask.retryForExceptions(() -> {}, RuntimeException.class)
				.maxAttempts(0));
	}

	@Test
	void maxAttemptsNegativeShouldThrow() {
		assertThrows(IllegalArgumentException.class,
			() -> RetryableTask.retryForExceptions(() -> {}, RuntimeException.class)
				.maxAttempts(-1));
	}

	@Test
	void delayNullShouldThrow() {
		// 修复前：校验 this.delay（默认非null）永远通过，null 被接受；修复后校验入参
		assertThrows(IllegalArgumentException.class,
			() -> RetryableTask.retryForExceptions(() -> {}, RuntimeException.class)
				.delay(null));
	}

	// endregion

	// region ----- RetryableTask + Backoff 集成

	@Test
	void retryableTaskWithFixedBackoff() {
		final int[] counter = {0};
		final RetryableTask<?> task = RetryableTask.retryForExceptions(() -> {
				counter[0]++;
				throw new RuntimeException("always fail");
			}, RuntimeException.class)
			.maxAttempts(3)
			.backoff(new FixedBackoff(Duration.ofMillis(10)))
			.execute();

		// maxAttempts=3：首次执行 + 3 次重试 = 共 4 次
		assertEquals(4, counter[0]);
		assertTrue(task.throwable().isPresent());
	}

	@Test
	void retryableTaskWithExponentialBackoff() {
		final int[] counter = {0};
		final RetryableTask<?> task = RetryableTask.retryForExceptions(() -> {
				counter[0]++;
				throw new RuntimeException("always fail");
			}, RuntimeException.class)
			.maxAttempts(3)
			.backoff(new ExponentialBackoff(Duration.ofMillis(10), 2.0, Duration.ofSeconds(1), false))
			.execute();

		// maxAttempts=3：首次执行 + 3 次重试 = 共 4 次
		assertEquals(4, counter[0]);
		assertTrue(task.throwable().isPresent());
	}

	@Test
	void retryableTaskBackoffNullShouldThrow() {
		assertThrows(IllegalArgumentException.class,
			() -> RetryableTask.retryForExceptions(() -> {}, RuntimeException.class)
				.backoff(null));
	}

	// endregion
}
