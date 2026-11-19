package cn.hutool.core.date;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link DateUtil#betweenDay(Date, Date, boolean)}和{@link DateUtil#betweenWeek(Date, Date, boolean)}在有夏令时的时区按日历日计算
 */
public class BetweenDayDstTest {

	private static DateTime of(final String zone, final String dateTime) {
		final ZoneId zoneId = ZoneId.of(zone);
		return new DateTime(LocalDateTime.parse(dateTime).atZone(zoneId).toInstant(), zoneId);
	}

	@Test
	public void betweenDayAcrossDstStartTest() {
		// 2024-03-10在America/New_York只有23小时
		final DateTime begin = of("America/New_York", "2024-03-10T08:00:00");
		final DateTime end = of("America/New_York", "2024-03-11T08:00:00");
		assertEquals(1, DateUtil.betweenDay(begin, end, true));
		assertEquals(1, DateUtil.betweenDay(end, begin, true));

		assertEquals(30, DateUtil.betweenDay(
			of("America/New_York", "2024-03-01T00:00:00"),
			of("America/New_York", "2024-03-31T00:00:00"), true));
		assertEquals(30, DateUtil.betweenDay(
			of("Europe/London", "2024-03-15T23:30:00"),
			of("Europe/London", "2024-04-14T00:10:00"), true));
	}

	@Test
	public void betweenDayAcrossDstEndTest() {
		// 2024-11-03在America/New_York有25小时，零点之间的毫秒差不是一天的整数倍
		assertEquals(1, DateUtil.betweenDay(
			of("America/New_York", "2024-11-03T00:00:00"),
			of("America/New_York", "2024-11-04T00:00:00"), true));
	}

	@Test
	public void betweenDayAtMidnightDstGapTest() {
		// America/Sao_Paulo在2018-11-04零点切换夏令时，当天从01:00开始
		assertEquals(1, DateUtil.betweenDay(
			of("America/Sao_Paulo", "2018-11-03T12:00:00"),
			of("America/Sao_Paulo", "2018-11-04T12:00:00"), true));
		assertEquals(2, DateUtil.betweenDay(
			of("America/Sao_Paulo", "2018-11-03T00:00:00"),
			of("America/Sao_Paulo", "2018-11-05T00:00:00"), true));
	}

	@Test
	public void betweenWeekAcrossDstStartTest() {
		assertEquals(1, DateUtil.betweenWeek(
			of("America/New_York", "2024-03-04T00:00:00"),
			of("America/New_York", "2024-03-11T00:00:00"), true));
	}

	@Test
	public void betweenDayDefaultTimeZoneTest() {
		final TimeZone defaultTimeZone = TimeZone.getDefault();
		try {
			TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
			LocalDate day = LocalDate.of(2023, 12, 1);
			final Date begin = DateUtil.parse("2023-12-01 12:00:00");
			for (int i = 0; i < 500; i++) {
				final Date end = DateUtil.parse(day.toString() + " 06:30:00");
				assertEquals(ChronoUnit.DAYS.between(LocalDate.of(2023, 12, 1), day),
					DateUtil.betweenDay(begin, end, true), day.toString());
				assertEquals(ChronoUnit.DAYS.between(LocalDate.of(2023, 12, 1), day) / 7,
					DateUtil.betweenWeek(begin, end, true), day.toString());
				day = day.plusDays(1);
			}
		} finally {
			TimeZone.setDefault(defaultTimeZone);
		}
	}
}
