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

public class Issue3798Test {
	@Test
	void parseTest() {
		final String iso_datetime1 = "2000-01-01T12:00:00+08:00";
		final DateTime parse1 = DateUtil.parse(iso_datetime1);
		Assertions.assertEquals("2000-01-01 12:00:00", parse1.toString());

		// 伦敦时间（Greenwich Mean Time, GMT）和北京时间（China Standard Time, CST）之间的时差是8小时。北京时间比伦敦时间快8小时
		final String iso_datetime2 = "2000-01-01T12:00:00+00:00";
		final DateTime parse2 = DateUtil.parse(iso_datetime2);

		// 默认的，输出的是当地时间，即伦敦时间在北京是几点
		Assertions.assertEquals("2000-01-01 20:00:00", parse2.toString());
		// 如果想输出伦敦时间，则，需要指定时区
		Assertions.assertEquals("2000-01-01 12:00:00", parse2.toString(ZoneUtil.getTimeZone("GMT+00:00")));
	}
}
