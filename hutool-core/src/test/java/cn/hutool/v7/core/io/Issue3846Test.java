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

package cn.hutool.v7.core.io;

import cn.hutool.v7.core.date.DateUtil;
import cn.hutool.v7.core.date.StopWatch;
import cn.hutool.v7.core.io.resource.ResourceUtil;
import cn.hutool.v7.core.lang.Console;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class Issue3846Test {
	@Test
	@Disabled
	void readBytesTest() {
		final StopWatch stopWatch = DateUtil.createStopWatch();
		stopWatch.start();
		final String filePath = "d:/test/issue3846.data";
		final byte[] bytes = IoUtil.readBytes(ResourceUtil.getStream(filePath), false);
		stopWatch.stop();
		Console.log(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
	}
}
