package cn.hutool.core.thread;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LatchUtils 相关单元测试
 */
class LatchUtilsTest {

	private ExecutorService executorService = Executors.newFixedThreadPool(2);

	@Test
	void testSubmitTask1() {
		LatchUtils.submitTask(executorService, () -> {
			System.out.println("task1");
		});
		LatchUtils.submitTask(executorService, () -> {
			System.out.println("task2");

		});
		LatchUtils.submitTask(executorService, () -> {
			System.out.println("task3");

		});
		assertTrue(LatchUtils.waitFor(1L, TimeUnit.SECONDS
		));
	}
}
