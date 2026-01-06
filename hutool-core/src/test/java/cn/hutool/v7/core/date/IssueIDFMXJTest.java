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
