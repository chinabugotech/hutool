package cn.hutool.core.collection;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * {@link BatchUtil} 单元测试
 *
 * @author wangzaixu
 */
public class BatchUtilTest {

	/**
	 * 正常分批：每页都能成功，且单条消费者不会触发
	 */
	@Test
	public void handleNormalTest() {
		final List<Integer> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			list.add(i);
		}

		final AtomicInteger batchCallCount = new AtomicInteger();
		final AtomicInteger onceCallCount = new AtomicInteger();
		final List<Integer> collected = new ArrayList<>();

		BatchUtil.handle(list, 3,
			batch -> {
				batchCallCount.incrementAndGet();
				collected.addAll(batch);
			},
			null,
			item -> onceCallCount.incrementAndGet(),
			null);

		// 10 个元素每批 3 个：3 + 3 + 3 + 1 = 4 批
		assertEquals(4, batchCallCount.get());
		assertEquals(0, onceCallCount.get());
		assertEquals(list, collected);
	}

	/**
	 * 空集合（含 null）：不抛异常、不执行任何消费者
	 */
	@Test
	public void handleEmptyOrNullListTest() {
		final AtomicInteger callCount = new AtomicInteger();

		assertDoesNotThrow(() -> BatchUtil.handle(Collections.<Integer>emptyList(), 10,
			batch -> callCount.incrementAndGet(),
			item -> {
			}));
		assertDoesNotThrow(() -> BatchUtil.handle((List<Integer>) null, 10,
			batch -> callCount.incrementAndGet(),
			item -> {
			}));

		assertEquals(0, callCount.get());
	}

	/**
	 * 单批场景：list.size() 正好等于 batchSize
	 */
	@Test
	public void handleSingleBatchTest() {
		final List<Integer> list = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			list.add(i);
		}

		final AtomicInteger batchCallCount = new AtomicInteger();
		final AtomicInteger onceCallCount = new AtomicInteger();

		BatchUtil.handle(list, 5,
			batch -> batchCallCount.addAndGet(batch.size()),
			item -> onceCallCount.incrementAndGet());

		assertEquals(5, batchCallCount.get());
		assertEquals(0, onceCallCount.get());
	}

	/**
	 * 批处理失败，降级为单条处理（无批重试、无单条重试）
	 */
	@Test
	public void handleBatchFailFallbackToOnceTest() {
		final List<Integer> list = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			list.add(i);
		}

		final AtomicInteger batchCallCount = new AtomicInteger();
		final AtomicInteger onceCallCount = new AtomicInteger();

		BatchUtil.handle(list, 5,
			batch -> {
				batchCallCount.incrementAndGet();
				throw new RuntimeException("batch fail");
			},
			null,
			item -> onceCallCount.incrementAndGet(),
			null);

		assertEquals(1, batchCallCount.get());
		assertEquals(5, onceCallCount.get());
	}

	/**
	 * 批处理失败，有批重试：批重试成功则不再降级单条
	 */
	@Test
	public void handleBatchRetrySuccessTest() {
		final List<Integer> list = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			list.add(i);
		}

		final AtomicInteger onceCallCount = new AtomicInteger();

		BatchUtil.handle(list, 5,
			batch -> {
				throw new RuntimeException("first fail");
			},
			batch -> {
				// 批重试成功
			},
			item -> onceCallCount.incrementAndGet(),
			null);

		assertEquals(0, onceCallCount.get());
	}

	/**
	 * 批处理失败，批重试也失败，降级为单条处理
	 */
	@Test
	public void handleBatchRetryFailThenFallbackTest() {
		final List<Integer> list = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			list.add(i);
		}

		final AtomicInteger batchRetryCount = new AtomicInteger();
		final AtomicInteger onceCallCount = new AtomicInteger();

		BatchUtil.handle(list, 5,
			batch -> {
				throw new RuntimeException("first fail");
			},
			batch -> {
				batchRetryCount.incrementAndGet();
				throw new RuntimeException("retry fail");
			},
			item -> onceCallCount.incrementAndGet(),
			null);

		assertEquals(1, batchRetryCount.get());
		assertEquals(5, onceCallCount.get());
	}

	/**
	 * 单条失败 + 单条重试成功：不应抛异常
	 */
	@Test
	public void handleOnceRetrySuccessTest() {
		final List<Integer> list = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			list.add(i);
		}

		final AtomicInteger onceRetryCount = new AtomicInteger();

		assertDoesNotThrow(() -> BatchUtil.handle(list, 3,
			batch -> {
				throw new RuntimeException("batch fail");
			},
			null,
			item -> {
				throw new RuntimeException("once fail: " + item);
			},
			item -> onceRetryCount.incrementAndGet()));

		assertEquals(3, onceRetryCount.get());
	}

	/**
	 * 单条失败且单条重试也失败：抛出运行时异常
	 */
	@Test
	public void handleOnceRetryFailThrowsTest() {
		final List<Integer> list = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			list.add(i);
		}

		final Consumer<List<Integer>> batchConsumer = batch -> {
			throw new RuntimeException("batch fail");
		};
		final Consumer<Integer> onceConsumer = item -> {
			throw new RuntimeException("once fail: " + item);
		};
		final Consumer<Integer> retryOnceConsumer = item -> {
			throw new IllegalStateException("retry fail: " + item);
		};

		// 第一条失败就会抛出，后续元素不应再处理
		final RuntimeException ex = assertThrows(RuntimeException.class,
			() -> BatchUtil.handle(list, 3,
				batchConsumer, null, onceConsumer, retryOnceConsumer));
		assertTrue(ex.getMessage().contains("retry fail"));
	}

	/**
	 * batchSize <= 0 应抛 IllegalArgumentException
	 */
	@Test
	public void handleInvalidBatchSizeTest() {
		final List<Integer> list = new ArrayList<>();
		list.add(1);

		assertThrows(IllegalArgumentException.class,
			() -> BatchUtil.handle(list, 0,
				batch -> {
				}, item -> {
				}));
		assertThrows(IllegalArgumentException.class,
			() -> BatchUtil.handle(list, -1,
				batch -> {
				}, item -> {
				}));
	}

	/**
	 * batchConsumer 为 null 应抛 IllegalArgumentException
	 */
	@Test
	public void handleNullBatchConsumerTest() {
		final List<Integer> list = new ArrayList<>();
		list.add(1);

		assertThrows(IllegalArgumentException.class,
			() -> BatchUtil.handle(list, 10, null, null,
				item -> {
				}, null));
	}

	/**
	 * onceConsumer 为 null 应抛 IllegalArgumentException
	 */
	@Test
	public void handleNullOnceConsumerTest() {
		final List<Integer> list = new ArrayList<>();
		list.add(1);

		assertThrows(IllegalArgumentException.class,
			() -> BatchUtil.handle(list, 10,
				batch -> {
				}, null, null, null));
	}

	/**
	 * 简化重载：使用默认批大小
	 */
	@Test
	public void handleDefaultBatchSizeTest() {
		final List<Integer> list = new ArrayList<>();
		// 大于默认批大小 500 的数据，验证会触发多次分批
		for (int i = 0; i < 1200; i++) {
			list.add(i);
		}

		final AtomicInteger batchCallCount = new AtomicInteger();

		BatchUtil.handle(list,
			batch -> batchCallCount.incrementAndGet(),
			item -> {
			});

		// 1200 / 500 = 2.4，向上取整分 3 批：[500, 500, 200]
		assertEquals(3, batchCallCount.get());
	}

	/**
	 * 列表中包含 null 元素：null 元素应被跳过（不抛异常）
	 */
	@Test
	public void handleWithNullElementTest() {
		final List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(null);
		list.add(2);

		final AtomicInteger onceCallCount = new AtomicInteger();

		assertDoesNotThrow(() -> BatchUtil.handle(list, 10,
			batch -> {
				throw new RuntimeException("force fallback");
			},
			item -> onceCallCount.incrementAndGet()));

		// 两条非 null 元素触发，null 被跳过
		assertEquals(2, onceCallCount.get());
	}

	/**
	 * 单条失败但未配置 retryOnceConsumer：异常被静默吞掉（无兜底时不抛）
	 *
	 * <p>retryOnceConsumer 作为安全兜底存在：未配置时调用方主动放弃了兜底，
	 * 单条失败不应向上抛，由调用方自行保证 onceConsumer 的可靠性。
	 */
	@Test
	public void handleOnceFailWithoutRetrySwallowsTest() {
		final List<Integer> list = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			list.add(i);
		}

		final AtomicInteger onceCallCount = new AtomicInteger();

		// 4 参数重载：retryOnceConsumer 默认为 null（无兜底）
		assertDoesNotThrow(() -> BatchUtil.handle(list, 3,
			batch -> {
				throw new RuntimeException("batch fail");
			},
			item -> {
				onceCallCount.incrementAndGet();
				throw new RuntimeException("once fail: " + item);
			}));

		// 3 个 item 的 onceConsumer 都被调用过，但失败被静默吞掉
		assertEquals(3, onceCallCount.get());
	}
}
