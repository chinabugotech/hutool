/*
 * Copyright (c) 2025 Hutool Team and hutool.cn
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

import java.util.Date;
import java.util.List;

public class DateRangeTest {
	@Test
	void issue3783Test() {
		final Date start = DateUtil.parse("2024-01-01");
		final Date end = DateUtil.parse("2024-02-01");
		final List<DateTime> dateTimes = DateUtil.rangeToList(start, end, DateField.DAY_OF_MONTH, 0);
		Assertions.assertEquals(1, dateTimes.size());
		Assertions.assertEquals("2024-01-01 00:00:00", dateTimes.get(0).toString());
	}

	@Test
	void issue3783Test2() {
		final Date start = DateUtil.parse("2024-01-01");
		final Date end = DateUtil.parse("2024-02-01");
		final List<DateTime> dateTimes = DateUtil.rangeToList(start, end, DateField.DAY_OF_MONTH, -2);
		Assertions.assertEquals(1, dateTimes.size());
		Assertions.assertEquals("2024-01-01 00:00:00", dateTimes.get(0).toString());
	}
}
