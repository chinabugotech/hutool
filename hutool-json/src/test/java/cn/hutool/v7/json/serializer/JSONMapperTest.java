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

package cn.hutool.v7.json.serializer;

import cn.hutool.v7.core.date.DateUtil;
import cn.hutool.v7.core.date.StopWatch;
import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.json.JSONFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class JSONMapperTest {

	/**
	 * Mapper性能耗费较多
	 */
	@Test
	@Disabled
	void toJSONTest() {
		final JSONFactory factory = JSONFactory.getInstance();

		final JSONMapper mapper = factory.getMapper();

		final StopWatch stopWatch = DateUtil.createStopWatch();

		final int count = 1000;
		stopWatch.start("use mapper");
		for (int i = 0; i < count; i++) {
			mapper.toJSON("qbw123", false);
		}
		stopWatch.stop();

		stopWatch.start("use ofPrimitive");
		for (int i = 0; i < count; i++) {
			factory.ofPrimitive("qbw123");
		}
		stopWatch.stop();

		Console.log(stopWatch.prettyPrint());
	}
}
