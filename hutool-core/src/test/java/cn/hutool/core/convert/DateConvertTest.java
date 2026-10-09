package cn.hutool.core.convert;

import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class DateConvertTest {

	@Test
	public void toDateTest() {
		String a = "2017-05-06";
		Date value = Convert.toDate(a);
		assertEquals(a, DateUtil.formatDate(value));

		long timeLong = DateUtil.date().getTime();
		Date value2 = Convert.toDate(timeLong);
		assertEquals(timeLong, value2.getTime());
	}

	@Test
	public void toDateFromIntTest() {
		int dateLong = -1497600000;
		Date value = Convert.toDate(dateLong);
		assertNotNull(value);
		assertEquals("Mon Dec 15 00:00:00 CST 1969", value.toString().replace("GMT+08:00", "CST"));

		final java.sql.Date sqlDate = Convert.convert(java.sql.Date.class, dateLong);
		assertNotNull(sqlDate);
		assertEquals("1969-12-15", sqlDate.toString());
	}

	@Test
	public void toDateFromLocalDateTimeTest() {
		LocalDateTime localDateTime = LocalDateTime.parse("2017-05-06T08:30:00", DateTimeFormatter.ISO_DATE_TIME);
		Date value = Convert.toDate(localDateTime);
		assertNotNull(value);
		assertEquals("2017-05-06", DateUtil.formatDate(value));
	}

	@Test
	public void toSqlDateTest() {
		String a = "2017-05-06";
		java.sql.Date value = Convert.convert(java.sql.Date.class, a);
		assertEquals("2017-05-06", value.toString());

		long timeLong = DateUtil.date().getTime();
		java.sql.Date value2 = Convert.convert(java.sql.Date.class, timeLong);
		assertEquals(timeLong, value2.getTime());
	}

	@Test
	public void toLocalDateTimeTest() {
		Date src = new Date();

		LocalDateTime ldt = Convert.toLocalDateTime(src);
		assertEquals(ldt, DateUtil.toLocalDateTime(src));

		Timestamp ts = Timestamp.from(src.toInstant());
		ldt = Convert.toLocalDateTime(ts);
		assertEquals(ldt, DateUtil.toLocalDateTime(src));

		String str = "2020-12-12 12:12:12.0";
		ldt = Convert.toLocalDateTime(str);
		assertEquals(str, ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")));
	}

	@Test
	public void toInstantTest() {
		// Convert.toInstant返回类型是Date，内部转换的目标类型却写成了Instant.class，
		// 导致defaultValue为null时任何非null输入都抛出ClassCastException
		assertEquals(0L, Convert.toInstant(Instant.ofEpochMilli(0L), (Date) null).getTime());

		final Date date = new Date();
		assertEquals(date.getTime(), Convert.toInstant(date, (Date) null).getTime());

		assertEquals("2020-01-01", DateUtil.formatDate(Convert.toInstant("2020-01-01", (Date) null)));

		assertNull(Convert.toInstant(null, (Date) null));
	}
}
