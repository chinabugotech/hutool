package cn.hutool.core.convert;

import cn.hutool.core.date.DateUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class TemporalAccessorConverterTest {

	@Test
	public void toInstantTest(){
		String dateStr = "2019-02-18";

		// 通过转换获取的Instant为UTC时间
		Instant instant = Convert.convert(Instant.class, dateStr);
		Instant instant1 = DateUtil.parse(dateStr).toInstant();
		assertEquals(instant1, instant);
	}

	@Test
	public void toLocalDateTimeTest(){
		LocalDateTime localDateTime = Convert.convert(LocalDateTime.class, "2019-02-18");
		assertEquals("2019-02-18T00:00", localDateTime.toString());
	}

	@Test
	public void toLocalDateTest(){
		LocalDate localDate = Convert.convert(LocalDate.class, "2019-02-18");
		assertEquals("2019-02-18", localDate.toString());
	}

	@Test
	public void toLocalTimeTest(){
		LocalTime localTime = Convert.convert(LocalTime.class, "2019-02-18");
		assertEquals("00:00", localTime.toString());
	}

	@Test
	public void toZonedDateTimeTest(){
		ZonedDateTime zonedDateTime = Convert.convert(ZonedDateTime.class, "2019-02-18");
		assertEquals("2019-02-18T00:00+08:00", zonedDateTime.toString().substring(0, 22));
	}

	@Test
	public void toOffsetDateTimeTest(){
		OffsetDateTime zonedDateTime = Convert.convert(OffsetDateTime.class, "2019-02-18");
		assertEquals("2019-02-18T00:00+08:00", zonedDateTime.toString());
	}

	@Test
	public void toOffsetTimeTest(){
		OffsetTime offsetTime = Convert.convert(OffsetTime.class, "2019-02-18");
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
		final LocalDateTime localDateTime = Convert.convert(LocalDateTime.class, map);
		assertEquals("2020-02-03T04:05:06.789", localDateTime.toString());
		assertEquals(6, localDateTime.getSecond());
		assertEquals(789000000, localDateTime.getNano());

		// 与LocalTime的处理保持一致
		final LocalTime localTime = Convert.convert(LocalTime.class, map);
		assertEquals("04:05:06.789", localTime.toString());
	}
}
