package cn.hutool.core.date.natural;

/**
 * 自然语言日期模式枚举
 * 按匹配优先级排序（数值越小优先级越高）
 *
 * @author yoran.ye
 * @since 6.0.0
 */
public enum NaturalDatePattern {

	/**
	 * 纯数字日期时间：2026-08-04 14:30:00（交给DateUtil.parse兜底）
	 */
	STANDARD(0, "STANDARD"),

	/**
	 * 相对日期：今天、明天、后天、昨天、前天
	 */
	RELATIVE_DAY(10, "RELATIVE_DAY"),

	/**
	 * 相对日期+时间：明天下午3点、后天上午10点半
	 */
	RELATIVE_DAY_TIME(20, "RELATIVE_DAY_TIME"),

	/**
	 * 相对偏移：3天前、5天后、2周前、1个月后
	 */
	RELATIVE_OFFSET(30, "RELATIVE_OFFSET"),

	/**
	 * 星期几：下周一、本周五、上周日
	 */
	WEEKDAY(40, "WEEKDAY"),

	/**
	 * 星期几+时间：下周一上午9点
	 */
	WEEKDAY_TIME(50, "WEEKDAY_TIME"),

	/**
	 * 月初/月末/年初/年末：本月第一天、下个月最后一天
	 */
	BOUNDARY(60, "BOUNDARY"),

	/**
	 * 特殊：现在、此刻、马上
	 */
	NOW(70, "NOW"),

	/**
	 * 时间点：上午、中午、下午、晚上、凌晨（基于当前日期）
	 */
	DAY_PART(80, "DAY_PART"),

	/**
	 * 季度：本季度、下季度、上季度
	 */
	QUARTER(90, "QUARTER"),

	/**
	 * 年份：今年、明年、去年
	 */
	YEAR(100, "YEAR"),

	/**
	 * 年月：2026年8月
	 */
	YEAR_MONTH(110, "YEAR_MONTH"),

	/**
	 * 默认（兜底）
	 */
	DEFAULT(999, "DEFAULT");

	private final int priority;
	private final String name;

	NaturalDatePattern(int priority, String name) {
		this.priority = priority;
		this.name = name;
	}

	public int getPriority() {
		return priority;
	}

	public String getName() {
		return name;
	}
}
