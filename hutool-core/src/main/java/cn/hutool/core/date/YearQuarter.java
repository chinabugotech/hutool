package cn.hutool.core.date;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static java.time.temporal.ChronoField.YEAR;

/**
 * 表示年份与季度
 *
 * @author ZhouXY
 * @since 5.8.37
 */
public final class YearQuarter implements Comparable<YearQuarter>, Serializable {
	private static final long serialVersionUID = 3804145964419489753L;

	/**
	 * 年份
	 */
	private final int year;
	/**
	 * 季度
	 */
	private final Quarter quarter;
	/**
	 * 季度开始日期
	 */
	private final LocalDate firstDate;
	/**
	 * 季度结束日期
	 */
	private final LocalDate lastDate;

	private YearQuarter(int year, Quarter quarter) {
		this.year = year;
		this.quarter = quarter;
		this.firstDate = quarter.firstMonthDay().atYear(year);
		this.lastDate = quarter.lastMonthDay().atYear(year);
	}

	// #region - StaticFactory

	/**
	 * 根据指定年份与季度，创建 {@code YearQuarter} 实例
	 *
	 * @param year    年份
	 * @param quarter 季度
	 * @return {@code YearQuarter} 实例
	 */
	public static YearQuarter of(int year, int quarter) {
		int yearValue = YEAR.checkValidIntValue(year);
		int quarterValue = Quarter.checkValidIntValue(quarter);
		return new YearQuarter(yearValue, Objects.requireNonNull(Quarter.of(quarterValue)));
	}

	/**
	 * 根据指定年份与季度，创建 {@code YearQuarter} 实例
	 *
	 * @param year    年份
	 * @param quarter 季度
	 * @return {@code YearQuarter} 实例
	 */
	public static YearQuarter of(int year, Quarter quarter) {
		return new YearQuarter(YEAR.checkValidIntValue(year), Objects.requireNonNull(quarter));
	}

	/**
	 * 根据指定日期，判断日期所在的年份与季度，创建 {@code YearQuarter} 实例
	 *
	 * @param date 日期
	 * @return {@code YearQuarter} 实例
	 */
	public static YearQuarter of(LocalDate date) {
		Objects.requireNonNull(date);
		return new YearQuarter(date.getYear(), Quarter.fromMonth(date.getMonthValue()));
	}

	/**
	 * 根据指定日期，判断日期所在的年份与季度，创建 {@code YearQuarter} 实例
	 *
	 * @param date 日期
	 * @return {@code YearQuarter} 实例
	 */
	public static YearQuarter of(Date date) {
		Objects.requireNonNull(date);
		@SuppressWarnings("deprecation") final int yearValue = YEAR.checkValidIntValue(date.getYear() + 1900L);
		@SuppressWarnings("deprecation") final int monthValue = date.getMonth() + 1;
		return new YearQuarter(yearValue, Quarter.fromMonth(monthValue));
	}

	/**
	 * 根据指定日期，判断日期所在的年份与季度，创建 {@code YearQuarter} 实例
	 *
	 * @param date 日期
	 * @return {@code YearQuarter} 实例
	 */
	public static YearQuarter of(Calendar date) {
		Objects.requireNonNull(date);
		final int yearValue = ChronoField.YEAR.checkValidIntValue(date.get(Calendar.YEAR));
		final int monthValue = date.get(Calendar.MONTH) + 1;
		return new YearQuarter(yearValue, Quarter.fromMonth(monthValue));
	}

	/**
	 * 根据指定年月，判断其所在的年份与季度，创建 {@code YearQuarter} 实例
	 *
	 * @param yearMonth 年月
	 * @return {@code YearQuarter} 实例
	 */
	public static YearQuarter of(YearMonth yearMonth) {
		Objects.requireNonNull(yearMonth);
		return of(yearMonth.getYear(), Quarter.fromMonth(yearMonth.getMonthValue()));
	}

	/**
	 * 当前年季
	 *
	 * @return 当前年季
	 */

	public static YearQuarter now() {
		return of(LocalDate.now());
	}

	// #endregion

	// #region - Getters

	/**
	 * 年份
	 *
	 * @return 年份
	 */
	public int getYear() {
		return this.year;
	}

	/**
	 * 季度
	 *
	 * @return 季度
	 */
	public Quarter getQuarter() {
		return this.quarter;
	}

	/**
	 * 季度值。从 1 开始。
	 *
	 * @return 季度值
	 */
	public int getQuarterValue() {
		return this.quarter.getValue();
	}

	/**
	 * 该季度第一个月
	 *
	 * @return {@link YearMonth} 对象
	 */
	public YearMonth firstYearMonth() {
		return YearMonth.of(this.year, this.quarter.firstMonthValue());
	}

	/**
	 * 该季度第一个月
	 *
	 * @return {@link Month} 对象
	 */
	public Month firstMonth() {
		return this.quarter.firstMonth();
	}

	/**
	 * 该季度的第一个月
	 *
	 * @return 结果。月份值从 1 开始，1 表示 1月，以此类推。
	 */
	public int firstMonthValue() {
		return this.quarter.firstMonthValue();
	}

	/**
	 * 该季度的最后一个月
	 *
	 * @return {@link YearMonth} 对象
	 */
	public YearMonth lastYearMonth() {
		return YearMonth.of(this.year, this.quarter.lastMonthValue());
	}

	/**
	 * 该季度的最后一个月
	 *
	 * @return {@link Month} 对象
	 */
	public Month lastMonth() {
		return this.quarter.lastMonth();
	}

	/**
	 * 该季度的最后一个月
	 *
	 * @return 结果。月份值从 1 开始，1 表示 1月，以此类推。
	 */
	public int lastMonthValue() {
		return this.quarter.lastMonthValue();
	}

	/**
	 * 该季度的第一天
	 *
	 * @return {@link LocalDate} 对象
	 */
	public LocalDate firstDate() {
		return firstDate;
	}

	/**
	 * 该季度的最后一天
	 *
	 * @return {@link LocalDate} 对象
	 */
	public LocalDate lastDate() {
		return lastDate;
	}

	// #endregion

	// #region - computes

	/**
	 * 添加季度
	 *
	 * @param quartersToAdd 要添加的季度数
	 * @return 计算结果
	 */
	public YearQuarter plusQuarters(long quartersToAdd) {
		if (quartersToAdd == 0L) {
			return this;
		}
		long quarterCount = this.year * 4L + (this.quarter.getValue() - 1);
		long calcQuarters = quarterCount + quartersToAdd; // safe overflow
		int newYear = YEAR.checkValidIntValue(Math.floorDiv(calcQuarters, 4));
		int newQuarter = (int) Math.floorMod(calcQuarters, 4) + 1;
		return new YearQuarter(newYear, Objects.requireNonNull(Quarter.of(newQuarter)));
	}

	/**
	 * 减去季度
	 *
	 * @param quartersToMinus 要减去的季度数
	 * @return 计算结果
	 */
	public YearQuarter minusQuarters(long quartersToMinus) {
		return plusQuarters(-quartersToMinus);
	}

	/**
	 * 下一个季度
	 *
	 * @return 结果
	 */
	public YearQuarter nextQuarter() {
		return plusQuarters(1L);
	}

	/**
	 * 上一个季度
	 *
	 * @return 结果
	 */
	public YearQuarter lastQuarter() {
		return minusQuarters(1L);
	}

	/**
	 * 添加年份
	 *
	 * @param yearsToAdd 要添加的年份数
	 * @return 计算结果
	 */
	public YearQuarter plusYears(long yearsToAdd) {
		if (yearsToAdd == 0L) {
			return this;
		}
		int newYear = YEAR.checkValidIntValue(this.year + yearsToAdd); // safe overflow
		return new YearQuarter(newYear, this.quarter);
	}

	/**
	 * 减去年份
	 *
	 * @param yearsToMinus 要减去的年份数
	 * @return 计算结果
	 */
	public YearQuarter minusYears(long yearsToMinus) {
		return plusYears(-yearsToMinus);
	}

	/**
	 * 下一年同季度
	 *
	 * @return 计算结果
	 */
	public YearQuarter nextYear() {
		return plusYears(1L);
	}

	/**
	 * 上一年同季度
	 *
	 * @return 计算结果
	 */
	public YearQuarter lastYear() {
		return minusYears(1L);
	}

	// #endregion

	// #region - hashCode & equals

	@Override
	public int hashCode() {
		return Objects.hash(year, quarter);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		YearQuarter other = (YearQuarter) obj;
		return year == other.year && quarter == other.quarter;
	}

	// #endregion

	// #region - compare

	@Override
	public int compareTo(YearQuarter other) {
		int cmp = (this.year - other.year);
		if (cmp == 0) {
			cmp = this.quarter.compareTo(other.quarter);
		}
		return cmp;
	}

	/**
	 * 判断是否在指定年份季度之前
	 *
	 * @param other 比较对象
	 * @return 结果
	 */
	public boolean isBefore(YearQuarter other) {
		return this.compareTo(other) < 0;
	}

	/**
	 * 判断是否在指定年份季度之后
	 *
	 * @param other 比较对象
	 * @return 结果
	 */
	public boolean isAfter(YearQuarter other) {
		return this.compareTo(other) > 0;
	}

	// #endregion

	// #region - toString

	/**
	 * 返回 {@code YearQuarter} 的字符串表示形式，如 "2024 Q3"
	 *
	 * @return {@code YearQuarter} 的字符串表示形式
	 */
	@Override
	public String toString() {
		return this.year + " " + this.quarter.name();
	}

	// #endregion
}
