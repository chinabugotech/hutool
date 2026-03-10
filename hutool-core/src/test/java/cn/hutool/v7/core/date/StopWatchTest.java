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

package cn.hutool.v7.core.date;

import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.core.thread.ThreadUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 秒表工具单元测试
 *
 * @author Looly
 */
public class StopWatchTest {

	@Test
	public void ofTest() {
		final StopWatch stopWatch = StopWatch.of();
		assertNotNull(stopWatch);
		assertEquals("", stopWatch.getId());

		final StopWatch stopWatchWithId = StopWatch.of("test-id");
		assertNotNull(stopWatchWithId);
		assertEquals("test-id", stopWatchWithId.getId());
	}

	@Test
	public void constructorTest() {
		final StopWatch stopWatch1 = new StopWatch();
		assertNotNull(stopWatch1);
		assertEquals("", stopWatch1.getId());

		final StopWatch stopWatch2 = new StopWatch("test-id");
		assertNotNull(stopWatch2);
		assertEquals("test-id", stopWatch2.getId());

		final StopWatch stopWatch3 = new StopWatch("test-id", true);
		assertNotNull(stopWatch3);
		assertEquals("test-id", stopWatch3.getId());

		final StopWatch stopWatch4 = new StopWatch("test-id", false);
		assertNotNull(stopWatch4);
		assertEquals("test-id", stopWatch4.getId());
	}

	@Test
	public void startAndStopTest() {
		final StopWatch stopWatch = new StopWatch();

		stopWatch.start("task1");
		assertTrue(stopWatch.isRunning());
		assertEquals("task1", stopWatch.currentTaskName());

		ThreadUtil.sleep(10);

		stopWatch.stop();
		assertFalse(stopWatch.isRunning());
		assertNull(stopWatch.currentTaskName());
		assertEquals(1, stopWatch.getTaskCount());
	}

	@Test
	public void startWithoutNameTest() {
		final StopWatch stopWatch = new StopWatch();

		stopWatch.start();
		assertTrue(stopWatch.isRunning());
		assertEquals("", stopWatch.currentTaskName());

		ThreadUtil.sleep(5);

		stopWatch.stop();
		assertFalse(stopWatch.isRunning());
		assertEquals(1, stopWatch.getTaskCount());
	}

	@Test
	public void multipleTasksTest() {
		final StopWatch stopWatch = new StopWatch();

		stopWatch.start("task1");
		ThreadUtil.sleep(10);
		stopWatch.stop();

		stopWatch.start("task2");
		ThreadUtil.sleep(20);
		stopWatch.stop();

		stopWatch.start("task3");
		ThreadUtil.sleep(15);
		stopWatch.stop();

		assertEquals(3, stopWatch.getTaskCount());
		assertEquals("task3", stopWatch.getLastTaskName());
	}

	@Test
	public void getLastTaskInfoTest() {
		final StopWatch stopWatch = new StopWatch();

		stopWatch.start("task1");
		ThreadUtil.sleep(10);
		stopWatch.stop();

		final StopWatch.TaskInfo taskInfo = stopWatch.getLastTaskInfo();
		assertNotNull(taskInfo);
		assertEquals("task1", taskInfo.getTaskName());
		assertTrue(taskInfo.getTimeNanos() > 0);
		assertTrue(taskInfo.getTimeMillis() >= 10);
	}

	@Test
	public void getTotalTimeTest() {
		final StopWatch stopWatch = new StopWatch();

		stopWatch.start("task1");
		ThreadUtil.sleep(10);
		stopWatch.stop();

		stopWatch.start("task2");
		ThreadUtil.sleep(20);
		stopWatch.stop();

		assertTrue(stopWatch.getTotalTimeNanos() > 0);
		assertTrue(stopWatch.getTotalTimeMillis() >= 30);
		assertTrue(stopWatch.getTotalTimeSeconds() > 0);

		assertTrue(stopWatch.getTotal(TimeUnit.NANOSECONDS) > 0);
		assertTrue(stopWatch.getTotal(TimeUnit.MILLISECONDS) >= 30);
		assertTrue(stopWatch.getTotal(TimeUnit.MICROSECONDS) > 0);
	}

	@Test
	public void getTotalTimePrettyTest() {
		final StopWatch stopWatch = new StopWatch();

		stopWatch.start("task1");
		ThreadUtil.sleep(100);
		stopWatch.stop();

		final String prettyTime = stopWatch.getTotalTimePretty();
		assertNotNull(prettyTime);
		assertFalse(prettyTime.isEmpty());
	}

	@Test
	public void keepTaskListFalseTest() {
		final StopWatch stopWatch = new StopWatch("test", false);

		stopWatch.start("task1");
		ThreadUtil.sleep(10);
		stopWatch.stop();

		assertEquals(1, stopWatch.getTaskCount());
		assertThrows(UnsupportedOperationException.class, stopWatch::getTaskInfo);
	}

	@Test
	public void setKeepTaskListTest() {
		final StopWatch stopWatch = new StopWatch("test", false);

		stopWatch.start("task1");
		ThreadUtil.sleep(10);
		stopWatch.stop();

		stopWatch.setKeepTaskList(true);

		stopWatch.start("task2");
		ThreadUtil.sleep(10);
		stopWatch.stop();

		assertEquals(2, stopWatch.getTaskCount());
		final StopWatch.TaskInfo[] taskInfos = stopWatch.getTaskInfo();
		assertNotNull(taskInfos);
		assertEquals(1, taskInfos.length);
		assertEquals("task2", taskInfos[0].getTaskName());
	}

	@Test
	public void shortSummaryTest() {
		final StopWatch stopWatch = new StopWatch("test-summary");

		stopWatch.start("task1");
		ThreadUtil.sleep(10);
		stopWatch.stop();

		final String summary = stopWatch.shortSummary();
		assertNotNull(summary);
		assertTrue(summary.contains("test-summary"));
		assertTrue(summary.contains("running time"));

		final String summaryWithUnit = stopWatch.shortSummary(TimeUnit.MILLISECONDS);
		assertNotNull(summaryWithUnit);
		Assertions.assertTrue(summaryWithUnit.contains("ms"));
	}

	@Test
	public void prettyPrintTest() {
		final StopWatch stopWatch = new StopWatch();
		stopWatch.start("任务 1");
		ThreadUtil.sleep(10);
		stopWatch.stop();
		stopWatch.start("任务 2");
		ThreadUtil.sleep(20);
		stopWatch.stop();

		final String prettyPrint = stopWatch.prettyPrint();
		assertNotNull(prettyPrint);
		assertTrue(prettyPrint.contains("任务 1"));
		assertTrue(prettyPrint.contains("任务 2"));

		Console.log(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
	}

	@Test
	public void toStringTest() {
		final StopWatch stopWatch = new StopWatch("test-toString");

		stopWatch.start("task1");
		ThreadUtil.sleep(10);
		stopWatch.stop();

		stopWatch.start("task2");
		ThreadUtil.sleep(20);
		stopWatch.stop();

		final String str = stopWatch.toString();
		assertNotNull(str);
		assertTrue(str.contains("test-toString"));
		assertTrue(str.contains("task1"));
		assertTrue(str.contains("task2"));
	}

	@Test
	public void taskInfoGetTimeTest() {
		final StopWatch stopWatch = new StopWatch();

		stopWatch.start("task1");
		ThreadUtil.sleep(15);
		stopWatch.stop();

		final StopWatch.TaskInfo taskInfo = stopWatch.getLastTaskInfo();
		assertNotNull(taskInfo);

		assertTrue(taskInfo.getTime(TimeUnit.NANOSECONDS) > 0);
		assertTrue(taskInfo.getTime(TimeUnit.MILLISECONDS) >= 15);
		assertTrue(taskInfo.getTimeNanos() > 0);
		assertTrue(taskInfo.getTimeMillis() >= 15);
		assertTrue(taskInfo.getTimeSeconds() > 0);
	}

	@Test
	public void IllegalStateExceptionStartTest() {
		final StopWatch stopWatch = new StopWatch();

		stopWatch.start("task1");

		assertThrows(IllegalStateException.class, () -> {
			stopWatch.start("task2");
		});

		stopWatch.stop();
	}

	@Test
	public void IllegalStateExceptionStopTest() {
		final StopWatch stopWatch = new StopWatch();

		assertThrows(IllegalStateException.class, stopWatch::stop);
	}

	@Test
	public void IllegalStateExceptionGetLastTaskTest() {
		final StopWatch stopWatch = new StopWatch();

		assertThrows(IllegalStateException.class, stopWatch::getLastTaskTimeNanos);

		assertThrows(IllegalStateException.class, stopWatch::getLastTaskTimeMillis);

		assertThrows(IllegalStateException.class, stopWatch::getLastTaskName);

		assertThrows(IllegalStateException.class, stopWatch::getLastTaskInfo);
	}
}

