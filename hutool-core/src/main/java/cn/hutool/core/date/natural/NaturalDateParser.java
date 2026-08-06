package cn.hutool.core.date.natural;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自然语言日期解析器
 * 支持：今天/明天/后天、X天前/后、下周一、本月最后一天等
 *
 * @author hutool
 * @since 6.0.0
 */
public class NaturalDateParser {

	/**
	 * 星期映射
	 */
	private static final Map<String, DayOfWeek> WEEK_MAP = new HashMap<>();
	/**
	 * 相对日期关键词
	 */
	private static final Map<String, Integer> RELATIVE_DAY_MAP = new HashMap<>();
	/**
	 * 时间段关键词
	 */
	private static final Map<String, Integer> DAY_PART_MAP = new HashMap<>();
	/**
	 * 边界关键词
	 */
	private static final Map<String, String> BOUNDARY_MAP = new HashMap<>();

	static {
		// 星期映射（支持中文和英文）
		WEEK_MAP.put("周一", DayOfWeek.MONDAY);
		WEEK_MAP.put("星期一", DayOfWeek.MONDAY);
		WEEK_MAP.put("一", DayOfWeek.MONDAY);
		WEEK_MAP.put("周二", DayOfWeek.TUESDAY);
		WEEK_MAP.put("星期二", DayOfWeek.TUESDAY);
		WEEK_MAP.put("二", DayOfWeek.TUESDAY);
		WEEK_MAP.put("周三", DayOfWeek.WEDNESDAY);
		WEEK_MAP.put("星期三", DayOfWeek.WEDNESDAY);
		WEEK_MAP.put("三", DayOfWeek.WEDNESDAY);
		WEEK_MAP.put("周四", DayOfWeek.THURSDAY);
		WEEK_MAP.put("星期四", DayOfWeek.THURSDAY);
		WEEK_MAP.put("四", DayOfWeek.THURSDAY);
		WEEK_MAP.put("周五", DayOfWeek.FRIDAY);
		WEEK_MAP.put("星期五", DayOfWeek.FRIDAY);
		WEEK_MAP.put("五", DayOfWeek.FRIDAY);
		WEEK_MAP.put("周六", DayOfWeek.SATURDAY);
		WEEK_MAP.put("星期六", DayOfWeek.SATURDAY);
		WEEK_MAP.put("六", DayOfWeek.SATURDAY);
		WEEK_MAP.put("周日", DayOfWeek.SUNDAY);
		WEEK_MAP.put("星期日", DayOfWeek.SUNDAY);
		WEEK_MAP.put("天", DayOfWeek.SUNDAY);
		WEEK_MAP.put("日", DayOfWeek.SUNDAY);
		WEEK_MAP.put("七", DayOfWeek.SUNDAY);

		// 相对日期偏移（相对于今天）
		RELATIVE_DAY_MAP.put("今天", 0);
		RELATIVE_DAY_MAP.put("今日", 0);
		RELATIVE_DAY_MAP.put("明天", 1);
		RELATIVE_DAY_MAP.put("明日", 1);
		RELATIVE_DAY_MAP.put("后天", 2);
		RELATIVE_DAY_MAP.put("大后天", 3);
		RELATIVE_DAY_MAP.put("昨天", -1);
		RELATIVE_DAY_MAP.put("昨日", -1);
		RELATIVE_DAY_MAP.put("前天", -2);
		RELATIVE_DAY_MAP.put("大前天", -3);

		// 时间段映射（小时）
		DAY_PART_MAP.put("凌晨", 0);
		DAY_PART_MAP.put("早上", 6);
		DAY_PART_MAP.put("早晨", 6);
		DAY_PART_MAP.put("上午", 8);
		DAY_PART_MAP.put("am", 8);
		DAY_PART_MAP.put("中午", 12);
		DAY_PART_MAP.put("下午", 13);
		DAY_PART_MAP.put("pm", 13);
		DAY_PART_MAP.put("傍晚", 17);
		DAY_PART_MAP.put("晚上", 19);
		DAY_PART_MAP.put("夜里", 21);
		DAY_PART_MAP.put("半夜", 23);
		DAY_PART_MAP.put("午夜", 0);

		// 边界关键词
		BOUNDARY_MAP.put("第一天", "FIRST_DAY");
		BOUNDARY_MAP.put("最后一天", "LAST_DAY");
		BOUNDARY_MAP.put("第一天", "FIRST_DAY");
		BOUNDARY_MAP.put("最后一天", "LAST_DAY");
		BOUNDARY_MAP.put("月初", "FIRST_DAY");
		BOUNDARY_MAP.put("月末", "LAST_DAY");
		BOUNDARY_MAP.put("月底", "LAST_DAY");
		BOUNDARY_MAP.put("年初", "FIRST_DAY");
		BOUNDARY_MAP.put("年末", "LAST_DAY");
		BOUNDARY_MAP.put("年底", "LAST_DAY");
		BOUNDARY_MAP.put("周一", "FIRST_DAY");
		BOUNDARY_MAP.put("周日", "LAST_DAY");
	}

	/**
	 * 解析自然语言日期
	 *
	 * @param text 自然语言文本
	 * @param base 基准日期
	 * @return 解析后的DateTime，无法解析返回null
	 */
	public static DateTime parse(String text, DateTime base) {
		if (StrUtil.isBlank(text) || base == null) {
			return null;
		}

		String trimmed = text.trim();

		// 1. 尝试标准格式（复用DateUtil.parse）
		try {
			DateTime result = DateUtil.parse(trimmed);
			if (result != null) {
				return result;
			}
		} catch (Exception ignored) {
			// 继续尝试自然语言解析
		}

		// 2. 按优先级依次尝试匹配
		NaturalDatePattern[] patterns = NaturalDatePattern.values();
		for (NaturalDatePattern pattern : patterns) {
			if (pattern == NaturalDatePattern.STANDARD || pattern == NaturalDatePattern.DEFAULT) {
				continue;
			}
			DateTime result = tryParse(trimmed, base, pattern);
			if (result != null) {
				return result;
			}
		}

		return null;
	}

	/**
	 * 按指定模式尝试解析
	 */
	private static DateTime tryParse(String text, DateTime base, NaturalDatePattern pattern) {
		switch (pattern) {
			case NOW:
				return parseNow(text, base);
			case RELATIVE_DAY:
				return parseRelativeDay(text, base);
			case RELATIVE_DAY_TIME:
				return parseRelativeDayTime(text, base);
			case RELATIVE_OFFSET:
				return parseRelativeOffset(text, base);
			case WEEKDAY:
				return parseWeekday(text, base);
			case WEEKDAY_TIME:
				return parseWeekdayTime(text, base);
			case BOUNDARY:
				return parseBoundary(text, base);
			case DAY_PART:
				return parseDayPart(text, base);
			case QUARTER:
				return parseQuarter(text, base);
			case YEAR:
				return parseYear(text, base);
			case YEAR_MONTH:
				return parseYearMonth(text, base);
			default:
				return null;
		}
	}

	// ==================== 各模式解析实现 ====================

	/**
	 * 解析"现在"、"此刻"、"马上"
	 */
	private static DateTime parseNow(String text, DateTime base) {
		if (matchesAny(text, "现在", "此刻", "马上", "当前", "目前")) {
			return base;
		}
		return null;
	}

	/**
	 * 解析相对日期：今天、明天、后天、昨天、前天
	 */
	private static DateTime parseRelativeDay(String text, DateTime base) {
		for (Map.Entry<String, Integer> entry : RELATIVE_DAY_MAP.entrySet()) {
			if (text.equals(entry.getKey())) {
				return DateUtil.offsetDay(base, entry.getValue());
			}
		}
		return null;
	}

	/**
	 * 解析相对日期+时间：明天下午3点、后天上午10点半
	 */
	private static DateTime parseRelativeDayTime(String text, DateTime base) {
		// 匹配模式：{相对日期}{时间段}{时间}
		// 如 "明天下午3点" -> 明天 + 下午 + 3点
		String timePart = null;
		String dayKey = null;

		// 提取相对日期关键词
		for (String key : RELATIVE_DAY_MAP.keySet()) {
			if (text.startsWith(key)) {
				dayKey = key;
				timePart = text.substring(key.length());
				break;
			}
		}

		if (dayKey == null || StrUtil.isBlank(timePart)) {
			return null;
		}

		// 解析时间
		DateTime time = parseTimeExpression(timePart, base);
		if (time == null) {
			return null;
		}

		// 应用日期偏移
		Integer offset = RELATIVE_DAY_MAP.get(dayKey);
		if (offset == null) {
			return null;
		}

		DateTime result = DateUtil.offsetDay(base, offset);
		// 保留日期部分，应用时间部分
		return new DateTime(LocalDateTime.of(
				result.year(),
				result.month() + 1,
				result.dayOfMonth(),
				time.hour(true),
				time.minute(),
				time.second()
		));
	}

	/**
	 * 解析相对偏移：3天前、5天后、2周前、1个月后
	 */
	private static DateTime parseRelativeOffset(String text, DateTime base) {
		// 匹配模式：数字 + 单位 + 方向
		// 支持：天/日、周/星期、月、年、小时、分钟
		Pattern pattern = Pattern.compile(
				"^([+-]?\\d+)\\s*个?(天|日|周|星期|月|年|小时|分钟|秒)\\s*(前|后)?$"
		);
		Matcher m = pattern.matcher(text);
		if (!m.find()) {
			return null;
		}

		int amount = Integer.parseInt(m.group(1));
		String unit = m.group(2);
		String direction = m.group(3);

		// 处理方向
		if ("前".equals(direction)) {
			amount = -amount;
		}

		// 单位转换
		switch (unit) {
			case "天":
			case "日":
				return DateUtil.offsetDay(base, amount);
			case "周":
			case "星期":
				return DateUtil.offsetWeek(base, amount);
			case "月":
				return DateUtil.offsetMonth(base, amount);
			case "年":
				return DateUtil.offsetYear(base, amount);
			case "小时":
				return DateUtil.offsetHour(base, amount);
			case "分钟":
				return DateUtil.offsetMinute(base, amount);
			case "秒":
				return DateUtil.offsetSecond(base, amount);
			default:
				return null;
		}
	}

	/**
	 * 解析星期几：下周一、本周五、上周日
	 */
	private static DateTime parseWeekday(String text, DateTime base) {
		String prefix = null;
		String weekdayKey = null;

		// 提取前缀（下、本、上）
		if (text.startsWith("下")) {
			prefix = "下";
			weekdayKey = text.substring(1);
		} else if (text.startsWith("本")) {
			prefix = "本";
			weekdayKey = text.substring(1);
		} else if (text.startsWith("上")) {
			prefix = "上";
			weekdayKey = text.substring(1);
		} else {
			// 没有前缀，默认本周
			prefix = "本";
			weekdayKey = text;
		}

		// 修正："下下周一" -> "下下" + "周一"
		if (weekdayKey.startsWith("下") || weekdayKey.startsWith("上") || weekdayKey.startsWith("本")) {
			// 递归处理多级前缀
			String subPrefix = weekdayKey.substring(0, 1);
			String rest = weekdayKey.substring(1);
			DateTime subResult = parseWeekday(rest, base);
			if (subResult == null) {
				return null;
			}
			if ("下".equals(subPrefix)) {
				return DateUtil.offsetWeek(subResult, 1);
			} else if ("上".equals(subPrefix)) {
				return DateUtil.offsetWeek(subResult, -1);
			}
			return subResult;
		}

		DayOfWeek target = WEEK_MAP.get(weekdayKey);
		if (target == null) {
			return null;
		}

		LocalDate baseDate = base.toLocalDateTime().toLocalDate();
		LocalDate resultDate;

		switch (prefix) {
			case "本":
				// 本周的指定星期（若已过则取下周）
				resultDate = baseDate.with(TemporalAdjusters.nextOrSame(target));
				break;
			case "下":
				// 下周的指定星期
				resultDate = baseDate.with(TemporalAdjusters.next(target));
				break;
			case "上":
				// 上周的指定星期
				resultDate = baseDate.with(TemporalAdjusters.previous(target));
				break;
			default:
				return null;
		}

		return new DateTime(resultDate.atStartOfDay());
	}

	/**
	 * 解析星期几+时间：下周一上午9点
	 */
	private static DateTime parseWeekdayTime(String text, DateTime base) {
		String weekdayKey = null;
		String timePart = null;
		String prefix = null;

		// 提取前缀
		if (text.startsWith("下") || text.startsWith("本") || text.startsWith("上")) {
			prefix = text.substring(0, 1);
			String rest = text.substring(1);
			// 提取星期关键词
			for (String key : WEEK_MAP.keySet()) {
				if (rest.startsWith(key)) {
					weekdayKey = key;
					timePart = rest.substring(key.length());
					break;
				}
			}
		}

		if (weekdayKey == null || StrUtil.isBlank(timePart)) {
			return null;
		}

		// 先解析星期
		String weekdayText = (prefix != null ? prefix : "本") + weekdayKey;
		DateTime weekdayResult = parseWeekday(weekdayText, base);
		if (weekdayResult == null) {
			return null;
		}

		// 解析时间
		DateTime time = parseTimeExpression(timePart, base);
		if (time == null) {
			return weekdayResult;
		}

		// 合并日期和时间
		return new DateTime(LocalDateTime.of(
				weekdayResult.year(),
				weekdayResult.month() + 1,
				weekdayResult.dayOfMonth(),
				time.hour(true),
				time.minute(),
				time.second()
		));
	}

	/**
	 * 解析边界：本月第一天、下个月最后一天、年初、月末
	 */
	private static DateTime parseBoundary(String text, DateTime base) {
		// 提取月份偏移和边界类型
		int monthOffset = 0;
		String boundaryType = null;
		String remaining = text;

		// 处理"下个月"、"上个月"、"本月"
		if (text.contains("下个月") || text.contains("下月")) {
			monthOffset = 1;
			remaining = text.replace("下个月", "").replace("下月", "");
		} else if (text.contains("上个月") || text.contains("上月")) {
			monthOffset = -1;
			remaining = text.replace("上个月", "").replace("上月", "");
		} else if (text.contains("本月") || text.contains("这个月")) {
			monthOffset = 0;
			remaining = text.replace("本月", "").replace("这个月", "");
		}

		// 提取边界类型
		if (remaining.contains("第一天") || remaining.contains("月初") || remaining.contains("开始")) {
			boundaryType = "FIRST_DAY";
		} else if (remaining.contains("最后一天") || remaining.contains("月末") || remaining.contains("月底") || remaining.contains("结束")) {
			boundaryType = "LAST_DAY";
		}

		if (boundaryType == null) {
			return null;
		}

		DateTime targetMonth = DateUtil.offsetMonth(base, monthOffset);
		LocalDate targetDate = targetMonth.toLocalDateTime().toLocalDate();

		LocalDate resultDate;
		if ("FIRST_DAY".equals(boundaryType)) {
			resultDate = targetDate.with(TemporalAdjusters.firstDayOfMonth());
		} else {
			resultDate = targetDate.with(TemporalAdjusters.lastDayOfMonth());
		}

		return new DateTime(resultDate.atStartOfDay());
	}

	/**
	 * 解析时间段：上午、中午、下午、晚上（基于当前日期）
	 */
	private static DateTime parseDayPart(String text, DateTime base) {
		Integer hour = DAY_PART_MAP.get(text);
		if (hour == null) {
			return null;
		}

		return new DateTime(LocalDateTime.of(
				base.year(),
				base.month() + 1,
				base.dayOfMonth(),
				hour,
				0,
				0
		));
	}

	/**
	 * 解析季度：本季度、下季度、上季度
	 */
	private static DateTime parseQuarter(String text, DateTime base) {
		int offset = 0;
		if (text.contains("下") || text.contains("下个")) {
			offset = 1;
		} else if (text.contains("上") || text.contains("上个")) {
			offset = -1;
		} else if (!text.contains("本") && !text.contains("这个")) {
			return null;
		}

		DateTime target = DateUtil.offsetMonth(base, offset * 3);
		LocalDate targetDate = target.toLocalDateTime().toLocalDate();
		LocalDate quarterStart = targetDate.with(targetDate.getMonth().firstMonthOfQuarter())
				.with(TemporalAdjusters.firstDayOfMonth());
		return new DateTime(quarterStart.atStartOfDay());
	}

	/**
	 * 解析年份：今年、明年、去年
	 */
	private static DateTime parseYear(String text, DateTime base) {
		int offset = 0;
		if (text.equals("明年") || text.equals("下年")) {
			offset = 1;
		} else if (text.equals("去年") || text.equals("上年")) {
			offset = -1;
		} else if (!text.equals("今年") && !text.equals("本年")) {
			return null;
		}

		DateTime target = DateUtil.offsetYear(base, offset);
		return DateUtil.beginOfYear(target);
	}

	/**
	 * 解析年月：2026年8月
	 */
	private static DateTime parseYearMonth(String text, DateTime base) {
		Pattern pattern = Pattern.compile("^(\\d{4})\\s*年\\s*(\\d{1,2})\\s*月$");
		Matcher m = pattern.matcher(text);
		if (!m.find()) {
			return null;
		}
		int year = Integer.parseInt(m.group(1));
		int month = Integer.parseInt(m.group(2));
		return new DateTime(LocalDateTime.of(year, month, 1, 0, 0, 0));
	}

	/**
	 * 解析时间表达式：3点、14:30、下午3点半
	 */
	private static DateTime parseTimeExpression(String text, DateTime base) {
		if (StrUtil.isBlank(text)) {
			return null;
		}

		// 处理时间段前缀 + 时间
		String timeText = text;
		int hourOffset = 0;
		for (Map.Entry<String, Integer> entry : DAY_PART_MAP.entrySet()) {
			if (text.startsWith(entry.getKey())) {
				hourOffset = entry.getValue();
				timeText = text.substring(entry.getKey().length());
				break;
			}
		}

		// 解析时间数字
		// 支持：3点、3点半、3:30、3点30分、14:30:15
		Pattern timePattern = Pattern.compile(
				"(?i)" + // 忽略大小写
						"(?:" +
						// 分支1：标准数字时间（含毫秒）  例：12:30:15.123 或 12点30分15秒123
						"(?<hour1>\\d{1,2})\\s*[:：点时]\\s*(?<min1>\\d{1,2})(?:\\s*[:：分]\\s*(?<sec1>\\d{1,2})(?:[.。]?(?<milli>\\d+))?\\s*秒?)?|" +
						// 分支2：中文半/刻  例：3点半、2点一刻、4点三刻
						"(?<hour2>\\d{1,2})\\s*点\\s*(?<special>半|一[刻]?|二[刻]?|三[刻]?)|" +
						// 分支3：单独的“X点”  例：5点
						"(?<hour3>\\d{1,2})\\s*点\\s*" +
						")"
		);
		Matcher m = timePattern.matcher(timeText);

		int hour, minute = 0, second = 0, milli = 0;

		if (m.find()) {
			// 确定数字小时（从命中的分支提取）
			if (m.group("hour1") != null) {
				hour = Integer.parseInt(m.group("hour1"));
				minute = Integer.parseInt(m.group("min1"));
				if (m.group("sec1") != null) second = Integer.parseInt(m.group("sec1"));
				if (m.group("milli") != null) milli = Integer.parseInt(m.group("milli"));
			} else if (m.group("hour2") != null) {
				hour = Integer.parseInt(m.group("hour2"));
				String sp = m.group("special");
				if ("半".equals(sp) || "二刻".equals(sp)) minute = 30;
				else if ("一刻".equals(sp)) minute = 15;
				else if ("三刻".equals(sp)) minute = 45;
			} else if (m.group("hour3") != null) {
				hour = Integer.parseInt(m.group("hour3"));
				minute = 0;
			} else {
				return null; // 未匹配
			}
		} else {
			// 尝试纯数字解析：1430 -> 14:30
			Pattern numPattern = Pattern.compile("^(\\d{1,2})(\\d{2})$");
			Matcher numMatcher = numPattern.matcher(timeText.trim());
			if (numMatcher.find()) {
				hour = Integer.parseInt(numMatcher.group(1));
				minute = Integer.parseInt(numMatcher.group(2));
			} else if (timeText.trim().length() == 0 && hourOffset > 0){
				hour = hourOffset;
			} else {
				return null;
			}
		}

		// 应用时间段偏移（如"下午3点" -> 15点）
		if (hourOffset > 12 && hour < 12) {
			hour += 12;
		}

		if (hour >= 24) {
			hour = hour % 24;
		}

		return new DateTime(LocalDateTime.of(
				base.year(),
				base.month() + 1,
				base.dayOfMonth(),
				hour,
				minute,
				second
		));
	}

	/**
	 * 获取文本中的时间段关键词
	 */
	private static String getDayPartKey(String text) {
		for (String key : DAY_PART_MAP.keySet()) {
			if (text.contains(key)) {
				return key;
			}
		}
		return null;
	}

	/**
	 * 判断文本是否匹配任意关键词
	 */
	private static boolean matchesAny(String text, String... keywords) {
		for (String keyword : keywords) {
			if (text.equals(keyword)) {
				return true;
			}
		}
		return false;
	}
}
