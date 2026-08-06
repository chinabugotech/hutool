package cn.hutool.core.date;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * 自然语言日期解析测试
 */
public class DateUtilNaturalTest {

	/** 基准日期：2026-08-04 10:00:00（周二） */
	private static final DateTime BASE = new DateTime(LocalDateTime.of(2026, 8, 4, 0, 0, 0));

	@Test
	public void testParseRelativeDay() {
		assertEquals("2026-08-04 00:00:00", DateUtil.parseNatural("今天", BASE).toString());
		assertEquals("2026-08-05 00:00:00", DateUtil.parseNatural("明天", BASE).toString());
		assertEquals("2026-08-06 00:00:00", DateUtil.parseNatural("后天", BASE).toString());
		assertEquals("2026-08-03 00:00:00", DateUtil.parseNatural("昨天", BASE).toString());
		assertEquals("2026-08-02 00:00:00", DateUtil.parseNatural("前天", BASE).toString());
	}

	@Test
	public void testParseRelativeDayTime() {
		DateTime result = DateUtil.parseNatural("明天下午3点", BASE);
		assertEquals("2026-08-05 15:00:00", result.toString());

		result = DateUtil.parseNatural("后天上午10点半", BASE);
		assertEquals("2026-08-06 10:30:00", result.toString());

		result = DateUtil.parseNatural("明天中午", BASE);
		assertEquals("2026-08-05 12:00:00", result.toString());

		result = DateUtil.parseNatural("后天晚上", BASE);
		assertEquals("2026-08-06 19:00:00", result.toString());
	}

	@Test
	public void testParseRelativeOffset() {
		assertEquals("2026-08-01 00:00:00", DateUtil.parseNatural("3天前", BASE).toString());
		assertEquals("2026-08-09 00:00:00", DateUtil.parseNatural("5天后", BASE).toString());
		assertEquals("2026-07-21 00:00:00", DateUtil.parseNatural("2周前", BASE).toString());
		assertEquals("2026-09-04 00:00:00", DateUtil.parseNatural("1个月后", BASE).toString());
		assertEquals("2025-08-04 00:00:00", DateUtil.parseNatural("1年前", BASE).toString());
	}

	@Test
	public void testParseWeekday() {
		// 2026-08-04 是周二
		assertEquals("2026-08-04 00:00:00", DateUtil.parseNatural("本周二", BASE).toString());
		assertEquals("2026-08-10 00:00:00", DateUtil.parseNatural("下周一", BASE).toString());
		assertEquals("2026-08-02 00:00:00", DateUtil.parseNatural("上周日", BASE).toString());
		assertEquals("2026-08-11 00:00:00", DateUtil.parseNatural("下下周二", BASE).toString());
	}

	@Test
	public void testParseWeekdayTime() {
		DateTime result = DateUtil.parseNatural("下周一上午9点", BASE);
		assertEquals("2026-08-10 09:00:00", result.toString());

		result = DateUtil.parseNatural("本周五下午5点半", BASE);
		assertEquals("2026-08-07 17:30:00", result.toString());
	}

	@Test
	public void testParseBoundary() {
		assertEquals("2026-08-01 00:00:00", DateUtil.parseNatural("本月第一天", BASE).toString());
		assertEquals("2026-08-31 00:00:00", DateUtil.parseNatural("本月最后一天", BASE).toString());
		assertEquals("2026-09-01 00:00:00", DateUtil.parseNatural("下个月第一天", BASE).toString());
		assertEquals("2026-07-31 00:00:00", DateUtil.parseNatural("上个月最后一天", BASE).toString());
	}

	@Test
	public void testParseDayPart() {
		assertEquals("2026-08-04 08:00:00", DateUtil.parseNatural("上午", BASE).toString());
		assertEquals("2026-08-04 12:00:00", DateUtil.parseNatural("中午", BASE).toString());
		assertEquals("2026-08-04 13:00:00", DateUtil.parseNatural("下午", BASE).toString());
		assertEquals("2026-08-04 19:00:00", DateUtil.parseNatural("晚上", BASE).toString());
	}

	@Test
	public void testParseQuarter() {
		assertEquals("2026-07-01 00:00:00", DateUtil.parseNatural("本季度", BASE).toString());
		assertEquals("2026-10-01 00:00:00", DateUtil.parseNatural("下季度", BASE).toString());
		assertEquals("2026-04-01 00:00:00", DateUtil.parseNatural("上季度", BASE).toString());
	}

	@Test
	public void testParseYear() {
		assertEquals("2026-01-01 00:00:00", DateUtil.parseNatural("今年", BASE).toString());
		assertEquals("2027-01-01 00:00:00", DateUtil.parseNatural("明年", BASE).toString());
		assertEquals("2025-01-01 00:00:00", DateUtil.parseNatural("去年", BASE).toString());
	}

	@Test
	public void testParseYearMonth() {
		assertEquals("2026-08-01 00:00:00", DateUtil.parseNatural("2026年8月", BASE).toString());
		assertEquals("2025-12-01 00:00:00", DateUtil.parseNatural("2025年12月", BASE).toString());
	}

	@Test
	public void testParseNow() {
		// 解析"现在"应返回当前时间（基准时间）
		DateTime now = DateUtil.parseNatural("现在", BASE);
		assertEquals(BASE.getTime(), now.getTime());

		now = DateUtil.parseNatural("此刻", BASE);
		assertEquals(BASE.getTime(), now.getTime());
	}

	@Test
	public void testParseStandardFormatFallback() {
		// 标准格式应直接解析
		assertEquals("2026-08-04 14:30:00",
				DateUtil.parseNatural("2026-08-04 14:30:00", BASE).toString());
		assertEquals("2026-08-04 00:00:00",
				DateUtil.parseNatural("2026/08/04", BASE).toString());
	}

	@Test
	public void testParseNull() {
		assertNull(DateUtil.parseNatural(null));
		assertNull(DateUtil.parseNatural(""));
		assertNull(DateUtil.parseNatural("   "));
	}

	@Test
	public void testParseUnsupported() {
		// 不支持的表达式返回null
		assertNull(DateUtil.parseNatural("无法转换文字", BASE));
	}
}
