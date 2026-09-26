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

package cn.hutool.v7.core.convert;

import cn.hutool.v7.core.date.DateUtil;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TemporalAccessorConverterTest {

	@Test
	public void toInstantTest(){
		final String dateStr = "2019-02-18";

		// 通过转换获取的Instant为UTC时间
		final Instant instant = ConvertUtil.convert(Instant.class, dateStr);
		final Instant instant1 = Objects.requireNonNull(DateUtil.parse(dateStr)).toInstant();
		assertEquals(instant1, instant);
	}

	@Test
	public void toLocalDateTimeTest(){
		final LocalDateTime localDateTime = ConvertUtil.convert(LocalDateTime.class, "2019-02-18");
		assertEquals("2019-02-18T00:00", localDateTime.toString());
	}

	@Test
	public void toLocalDateTest(){
		final LocalDate localDate = ConvertUtil.convert(LocalDate.class, "2019-02-18");
		assertEquals("2019-02-18", localDate.toString());
	}

	@Test
	public void toLocalTimeTest(){
		final LocalTime localTime = ConvertUtil.convert(LocalTime.class, "2019-02-18");
		assertEquals("00:00", localTime.toString());
	}

	@Test
	public void toZonedDateTimeTest(){
		final ZonedDateTime zonedDateTime = ConvertUtil.convert(ZonedDateTime.class, "2019-02-18");
		assertEquals("2019-02-18T00:00+08:00", zonedDateTime.toString().substring(0, 22));
	}

	@Test
	public void toOffsetDateTimeTest(){
		final OffsetDateTime zonedDateTime = ConvertUtil.convert(OffsetDateTime.class, "2019-02-18");
		assertEquals("2019-02-18T00:00+08:00", zonedDateTime.toString());
	}

	@Test
	public void toOffsetTimeTest(){
		final OffsetTime offsetTime = ConvertUtil.convert(OffsetTime.class, "2019-02-18");
		assertEquals("00:00+08:00", offsetTime.toString());
	}

	@Test
	public void toLocalDateTimeFromMapTest(){
		final Map<String, Object> map = new HashMap<>();
		map.put("year", 2020);
		map.put("month", 2);
		map.put("day", 3);
		map.put("hour", 4);
		map.put("minute", 5);
		map.put("second", 6);
		map.put("nano", 789000000);

		// 纳秒应取自map的"nano"键，此前误取了"second"，导致nano变成6
		final LocalDateTime localDateTime = ConvertUtil.convert(LocalDateTime.class, map);
		assertEquals("2020-02-03T04:05:06.789", localDateTime.toString());
		assertEquals(6, localDateTime.getSecond());
		assertEquals(789000000, localDateTime.getNano());

		// 与LocalTime的处理保持一致
		final LocalTime localTime = ConvertUtil.convert(LocalTime.class, map);
		assertEquals("04:05:06.789", localTime.toString());
	}
}
