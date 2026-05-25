package cn.hutool.core.date;

import cn.hutool.core.lang.Assert;

import java.time.DateTimeException;
import java.time.MonthDay;
import java.time.temporal.ChronoField;

/**
 * 季度枚举
 *
 * @see #Q1
 * @see #Q2
 * @see #Q3
 * @see #Q4
 *
 * @author zhfish(https://github.com/zhfish)
 *
 */
public enum Quarter {

	/** 第一季度 */
	Q1(1),
	/** 第二季度 */
	Q2(2),
	/** 第三季度 */
	Q3(3),
	/** 第四季度 */
	Q4(4);

	// ---------------------------------------------------------------
	private final int value;

	private final int firstMonth;
	private final int lastMonth;

	Quarter(int value) {
		this.value = value;

		this.lastMonth = value * 3;
		this.firstMonth = lastMonth - 2;
	}

	/**
	 * 获取季度值
	 *
	 * @return 季度值
	 */
	public int getValue() {
		return this.value;
	}

	/**
	 * 将 季度int转换为Season枚举对象<br>
	 *
	 * @see #Q1
	 * @see #Q2
	 * @see #Q3
	 * @see #Q4
	 *
	 * @param intValue 季度int表示
	 * @return {@code Quarter}
	 */
	public static Quarter of(int intValue) {
		switch (intValue) {
			case 1:
				return Q1;
			case 2:
				return Q2;
			case 3:
				return Q3;
			case 4:
				return Q4;
			default:
				return null;
		}
	}

	/**
	 * 根据给定的月份值返回对应的季度
	 *
	 * @param monthValue 月份值，取值范围为1到12
	 * @return 对应的季度
	 * @throws IllegalArgumentException 如果月份值不在有效范围内（1到12），将抛出异常
	 */
	public static Quarter fromMonth(int monthValue) {
		ChronoField.MONTH_OF_YEAR.checkValidValue(monthValue);
		return of(computeQuarterValueInternal(monthValue));
	}

	/**
	 * 根据给定的月份返回对应的季度
	 *
	 * @param month 月份
	 * @return 对应的季度
	 */
	public static Quarter fromMonth(Month month) {
		Assert.notNull(month);
		final int monthValue = month.getValue();
		return of(computeQuarterValueInternal(monthValue));
	}

	/**
	 * 根据指定的年份，获取一个新的 YearQuarter 实例
	 * 此方法允许在保持当前季度信息不变的情况下，更改年份
	 *
	 * @param year 指定的年份
	 * @return 返回一个新的 YearQuarter 实例，年份更新为指定的年份
	 */
	public final YearQuarter atYear(int year) {
		return YearQuarter.of(year, this);
	}

	// StaticFactoryMethods end

	// computes

	/**
	 * 加上指定数量的季度
	 *
	 * @param quarters 所添加的季度数量
	 * @return 计算结果
	 */
	public Quarter plus(int quarters) {
		final int amount = (quarters % 4) + 4;
		return Quarter.values()[(ordinal() + amount) % 4];
	}

	// computes end

	// Getters

	/**
	 * 该季度的第一个月
	 *
	 * @return 结果
	 */
	public Month firstMonth() {
		return Month.of(firstMonthValue() - 1);
	}

	/**
	 * 该季度的第一个月
	 *
	 * @return 结果。月份值从 1 开始，1 表示 1月，以此类推。
	 */
	public int firstMonthValue() {
		return this.firstMonth;
	}

	/**
	 * 该季度最后一个月
	 *
	 * @return 结果
	 */
	public Month lastMonth() {
		return Month.of(lastMonthValue() - 1);
	}

	/**
	 * 该季度最后一个月
	 *
	 * @return 结果。1 表示 1月，以此类推。
	 */
	public int lastMonthValue() {
		return this.lastMonth;
	}

	/**
	 * 该季度的第一天
	 *
	 * @return 结果
	 */
	public MonthDay firstMonthDay() {
		return MonthDay.of(firstMonthValue(), 1);
	}

	/**
	 * 该季度的最后一天
	 *
	 * @return 结果
	 */
	public MonthDay lastMonthDay() {
		// 季度的最后一个月不可能是 2 月，不考虑闰年
		final Month month = lastMonth();
		return MonthDay.of(month.toJdkMonth(), month.getLastDay(false));
	}

	// Getters end

	/**
	 * 检查季度的值。（1~4）
	 * @param value 季度值
	 * @return 在取值范围内的值
	 */
	public static int checkValidIntValue(int value) {
		Assert.isTrue(value >= 1 && value <= 4,
				() -> new DateTimeException("Invalid value for Quarter: " + value));
		return value;
	}

	// Internal

	/**
	 * 计算给定月份对应的季度值
	 *
	 * @param monthValue 月份值，取值范围为1到12
	 * @return 对应的季度值
	 */
	private static int computeQuarterValueInternal(int monthValue) {
		return (monthValue - 1) / 3 + 1;
	}
}
