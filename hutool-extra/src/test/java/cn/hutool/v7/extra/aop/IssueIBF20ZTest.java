package cn.hutool.v7.extra.aop;

import cn.hutool.v7.core.thread.ThreadUtil;
import cn.hutool.v7.extra.aop.engine.ProxyEngine;
import cn.hutool.v7.extra.aop.engine.ProxyEngineFactory;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IssueIBF20ZTest {
	@Test
	public void testLoadFirstAvailableConcurrent() throws InterruptedException {
		// 创建一个固定大小的线程池
		final int threadCount = 1000;
		final ExecutorService executorService = ThreadUtil.newExecutor(threadCount);

		// 创建一个 CountDownLatch，用于等待所有任务完成
		final CountDownLatch latch = new CountDownLatch(threadCount);

		// 计数器用于统计成功加载服务提供者的次数
		final AtomicInteger successCount = new AtomicInteger(0);

		// 提交多个任务到线程池
		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				final ProxyEngine factory = ProxyEngineFactory.createEngine();
				if (factory != null) {
					successCount.incrementAndGet();
				}
				latch.countDown(); // 每个任务完成时，计数减一
			});
		}

		// 等待所有任务完成
		latch.await();

		// 关闭线程池并等待所有任务完成
		executorService.shutdown();

		// 验证所有线程都成功加载了服务提供者
		assertEquals(threadCount, successCount.get());
	}
}
