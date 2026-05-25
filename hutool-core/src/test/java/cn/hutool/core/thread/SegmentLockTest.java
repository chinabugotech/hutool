package cn.hutool.core.thread;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.lock.SegmentLock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SegmentLock 单元测试类
 */
public class SegmentLockTest {

	private static final int SEGMENT_COUNT = 4;
	private SegmentLock<Lock> strongLock;
	private SegmentLock<Lock> weakLock;
	private SegmentLock<Semaphore> semaphore;
	private SegmentLock<ReadWriteLock> readWriteLock;

	@BeforeEach
	public void setUp() {
		strongLock = SegmentLock.lock(SEGMENT_COUNT);
		weakLock = SegmentLock.lazyWeakLock(SEGMENT_COUNT);
		semaphore = SegmentLock.semaphore(SEGMENT_COUNT, 2);
		readWriteLock = SegmentLock.readWriteLock(SEGMENT_COUNT);
	}

	@Test
	public void testSize() {
		assertEquals(SEGMENT_COUNT, strongLock.size());
		assertEquals(SEGMENT_COUNT, weakLock.size());
		assertEquals(SEGMENT_COUNT, semaphore.size());
		assertEquals(SEGMENT_COUNT, readWriteLock.size());
	}

	@SuppressWarnings("StringOperationCanBeSimplified")
	@Test
	public void testGetWithSameKey() {
		// 相同 key 应返回相同锁
		String key1 = "testKey";
		String key2 = new String("testKey"); // equals 但不同对象
		Lock lock1 = strongLock.get(key1);
		Lock lock2 = strongLock.get(key2);
		assertSame(lock1, lock2, "相同 key 应返回同一锁对象");

		Lock weakLock1 = weakLock.get(key1);
		Lock weakLock2 = weakLock.get(key2);
		assertSame(weakLock1, weakLock2, "弱引用锁相同 key 应返回同一锁对象");
	}

	@Test
	public void testGetAt() {
		for (int i = 0; i < SEGMENT_COUNT; i++) {
			Lock lock = strongLock.getAt(i);
			assertNotNull(lock, "getAt 返回的锁不应为 null");
		}
		assertThrows(IllegalArgumentException.class, () -> strongLock.getAt(SEGMENT_COUNT),
			"超出段数的索引应抛出异常");
	}

	@Test
	public void testBulkGet() {
		List<String> keys = CollUtil.newArrayList("key1", "key2", "key3");
		Iterable<Lock> locks = strongLock.bulkGet(keys);
		List<Lock> lockList = CollUtil.newArrayList(locks);

		assertEquals(3, lockList.size(), "bulkGet 返回的锁数量应与 key 数量一致");

		// 检查顺序性
		int prevIndex = -1;
		for (Lock lock : lockList) {
			int index = findIndex(strongLock, lock);
			assertTrue(index >= prevIndex, "bulkGet 返回的锁应按索引升序");
			prevIndex = index;
		}
	}

	@Test
	public void testLockConcurrency() throws InterruptedException {
		int threadCount = SEGMENT_COUNT * 2;
		CountDownLatch startLatch = new CountDownLatch(1);
		CountDownLatch endLatch = new CountDownLatch(threadCount);
		ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		List<String> keys = new ArrayList<>();
		for (int i = 0; i < threadCount; i++) {
			keys.add("key" + i);
		}

		for (int i = 0; i < threadCount; i++) {
			final String key = keys.get(i);
			executor.submit(() -> {
				try {
					startLatch.await();
					Lock lock = strongLock.get(key);
					lock.lock();
					try {
						Thread.sleep(100); // 模拟工作
					} finally {
						lock.unlock();
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				} finally {
					endLatch.countDown();
				}
			});
		}

		startLatch.countDown();
		assertTrue(endLatch.await(2000, java.util.concurrent.TimeUnit.MILLISECONDS),
			"并发锁测试应在 2 秒内完成");
		executor.shutdown();
	}

	@Test
	public void testSemaphore() {
		Semaphore sem = semaphore.get("testKey");
		assertEquals(2, sem.availablePermits(), "信号量初始许可应为 2");

		sem.acquireUninterruptibly(2);
		assertEquals(0, sem.availablePermits(), "获取所有许可后应为 0");

		sem.release(1);
		assertEquals(1, sem.availablePermits(), "释放一个许可后应为 1");
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	@Test
	public void testReadWriteLock() throws InterruptedException {
		ReadWriteLock rwLock = readWriteLock.get("testKey");
		Lock readLock = rwLock.readLock();
		Lock writeLock = rwLock.writeLock();

		// 测试读锁可重入
		readLock.lock();
		assertTrue(readLock.tryLock(), "读锁应允许多个线程同时持有");
		readLock.unlock();
		readLock.unlock();

		CountDownLatch latch = new CountDownLatch(1);
		ExecutorService executor = Executors.newSingleThreadExecutor();
		AtomicBoolean readLockAcquired = new AtomicBoolean(false);

		writeLock.lock();
		executor.submit(() -> {
			readLockAcquired.set(readLock.tryLock());
			latch.countDown();
		});

		latch.await(500, TimeUnit.MILLISECONDS);
		assertFalse(readLockAcquired.get(), "写锁持有时读锁应失败");
		writeLock.unlock();

		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.SECONDS);
	}

	@Test
	public void testWeakReferenceCleanup() throws InterruptedException {
		SegmentLock<Lock> weakLockLarge = SegmentLock.lazyWeakLock(1024); // 超过 LARGE_LAZY_CUTOFF
		Lock lock = weakLockLarge.get("testKey");

		System.gc();
		Thread.sleep(100);

		// 弱引用锁未被其他引用，应仍可获取
		Lock lockAgain = weakLockLarge.get("testKey");
		assertSame(lock, lockAgain, "弱引用锁未被回收时应返回同一对象");
	}

	@Test
	public void testInvalidSegmentCount() {
		assertThrows(IllegalArgumentException.class, () -> SegmentLock.lock(0),
			"段数为 0 应抛出异常");
		assertThrows(IllegalArgumentException.class, () -> SegmentLock.lock(-1),
			"负段数应抛出异常");
	}

	@Test
	public void testHashDistribution() {
		SegmentLock<Lock> lock = SegmentLock.lock(4);
		int[] counts = new int[4];
		for (int i = 0; i < 100; i++) {
			int index = findIndex(lock, lock.get("key" + i));
			counts[index]++;
		}
		for (int count : counts) {
			assertTrue(count > 0, "每个段都应至少被分配到一个 key");
		}
	}

	private int findIndex(SegmentLock<Lock> lock, Lock target) {
		for (int i = 0; i < lock.size(); i++) {
			if (lock.getAt(i) == target) {
				return i;
			}
		}
		return -1;
	}
}
