/*
 * Copyright (c) 2013-2025 Hutool Team and hutool.cn
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
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class StopWatchTest {

	/**
	 * https://gitee.com/chinabugotech/hutool/issues/I6HSBG
	 */
	@Test
	public void prettyPrintTest() {
		final StopWatch stopWatch = new StopWatch();
		stopWatch.start("任务1");
		ThreadUtil.sleep(1);
		stopWatch.stop();
		stopWatch.start("任务2");
		ThreadUtil.sleep(200);
		stopWatch.stop();

		Console.log(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
	}
}
