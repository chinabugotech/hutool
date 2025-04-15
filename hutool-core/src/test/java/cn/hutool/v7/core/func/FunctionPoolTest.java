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

package cn.hutool.v7.core.func;

import cn.hutool.v7.core.collection.ListUtil;
import cn.hutool.v7.core.date.DateUtil;
import cn.hutool.v7.core.date.StopWatch;
import cn.hutool.v7.core.util.RandomUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class FunctionPoolTest {

	@Test
	public void createStringTest() {
		// 预热
		FunctionPool.createString("123".toCharArray());

		// 测试数据
		final ArrayList<char[]> list = ListUtil.of();
		for (int i = 0; i < 100000; i++) {
			list.add(RandomUtil.randomStringLower(100).toCharArray());
		}

		final StopWatch stopWatch = DateUtil.createStopWatch();
		stopWatch.start("copy creator");
		for (final char[] value : list) {
			new String(value);
		}
		stopWatch.stop();

		stopWatch.start("zero copy creator");
		for (final char[] value : list) {
			FunctionPool.createString(value);
		}
		stopWatch.stop();

		//Console.log(stopWatch.prettyPrint());
	}
}
