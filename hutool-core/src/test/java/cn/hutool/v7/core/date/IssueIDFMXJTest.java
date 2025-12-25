package cn.hutool.v7.core.date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class IssueIDFMXJTest {

	@Test
	void stopWatchNegativeTimeTest() throws NoSuchFieldException, IllegalAccessException {
		final StopWatch stopWatch = new StopWatch();
		stopWatch.start("负耗时测试任务");

		// 反射修改startTimeNanos为当前时间+1秒（模拟nanoTime回退）
		final Field startTimeNanosField = StopWatch.class.getDeclaredField("startTimeNanos");
		startTimeNanosField.setAccessible(true);
		startTimeNanosField.set(stopWatch, System.nanoTime() + 1_000_000_000);

		stopWatch.stop();

		Assertions.assertEquals(0, stopWatch.getLastTaskTimeNanos());
		Assertions.assertEquals(0, stopWatch.getTotalTimeNanos());
	}

}
