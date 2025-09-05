/*
 * Copyright (c) 2013-2025 Hutool Team and hutool.cn
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

package cn.hutool.v7.cron.pattern.matcher;

import cn.hutool.v7.core.date.Month;

import java.util.List;

/**
 * 每月第几天匹配<br>
 * 考虑每月的天数不同，且存在闰年情况，日匹配单独使用
 *
 * @author Looly
 */
public class DayOfMonthMatcher extends BoolArrayMatcher {

	/**
	 * 最后一天
	 */
	private static final int LAST_DAY = 32;

	/**
	 * 构造
	 *
	 * @param intValueList 匹配的日值
	 */
	public DayOfMonthMatcher(final List<Integer> intValueList) {
		super(intValueList);
	}

	/**
	 * 给定的日是否匹配当前匹配器，匹配分为两种情况：
	 * <ul>
	 *     <li>如果指定日在表达式中有定义，则直接返回。</li>
	 *     <li>如果指定日不在表达式中，但是表达式包含最后一天，匹配当月最后一天。</li>
	 * </ul>
	 *
	 * @param dayValue   被检查的值，此处为日，从1开始
	 * @param month      实际的月份，从1开始
	 * @param isLeapYear 是否闰年
	 * @return 是否匹配
	 */
	public boolean match(final int dayValue, final int month, final boolean isLeapYear) {
		return (super.test(dayValue) // 在约定日范围内的某一天
			//匹配器中用户定义了最后一天（32表示最后一天）
			|| matchLastDay(dayValue, month, isLeapYear));
	}

	/**
	 * 获取指定日之后的匹配值，也可以是其本身<br>
	 * 如果表达式中存在最后一天（如使用"L"），则：
	 * <ul>
	 *     <li>4月、6月、9月、11月最多匹配到30日</li>
	 *     <li>4月闰年匹配到29日，非闰年28日</li>
	 * </ul>
	 *
	 * @param dayValue   指定的天值
	 * @param month      月份，从1开始
	 * @param isLeapYear 是否为闰年
	 * @return 匹配到的值或之后的值
	 */
	public int nextAfter(int dayValue, final int month, final boolean isLeapYear) {
		final int maxValue = getMaxValue(month, isLeapYear);
		final int minValue = getMinValue(month, isLeapYear);
		if (dayValue > minValue) {
			final boolean[] bValues = this.bValues;
			// 最大值永远小于数组长度，只需判断最大值边界
			while (dayValue <= maxValue) {
				// 匹配到有效值
				if (bValues[dayValue] ||
					// 如果最大值不在有效值中，这个最大值表示最后一天，则在包含了最后一天的情况下返回最后一天
					(dayValue == maxValue && test(LAST_DAY))) {
					return dayValue;
				}
				dayValue++;
			}
		}

		// 两种情况返回最小值
		// 一是给定值小于最小值，那下一个匹配值就是最小值
		// 二是给定值大于最大值，那下一个匹配值也是下一轮的最小值
		return minValue;
	}

	/**
	 * 获取表达式定义中指定月的最小日的值
	 *
	 * @param month      月，base1
	 * @param isLeapYear 是否闰年
	 * @return 匹配的最小值
	 */
	public int getMinValue(final int month, final boolean isLeapYear) {
		final int minValue = super.getMinValue();
		if (LAST_DAY == minValue) {
			// 用户指定了 L 等表示最后一天
			return getLastDay(month, isLeapYear);
		}
		return minValue;
	}

	/**
	 * 获取表达式定义中指定月的最大日的值<br>
	 * 首先获取表达式定义的最大值，如果这个值大于本月最后一天，则返回最后一天，否则返回用户定义的最大值<br>
	 * 注意最后一天可能不是表达式中定义的有效值
	 *
	 * @param month      月，base1
	 * @param isLeapYear 是否闰年
	 * @return 匹配的最大值
	 */
	public int getMaxValue(final int month, final boolean isLeapYear) {
		return Math.min(super.getMaxValue(), getLastDay(month, isLeapYear));
	}

	/**
	 * 是否匹配本月最后一天，规则如下：
	 * <pre>
	 * 1、闰年2月匹配是否为29
	 * 2、其它月份是否匹配最后一天的日期（可能为30或者31）
	 * 3、表达式包含最后一天（使用31表示）
	 * </pre>
	 *
	 * @param value      被检查的值
	 * @param month      月，base1
	 * @param isLeapYear 是否闰年
	 * @return 是否为本月最后一天
	 */
	private boolean matchLastDay(final int value, final int month, final boolean isLeapYear) {
		return value > 27
			// 表达式中定义包含了最后一天
			&& test(LAST_DAY)
			// 用户指定的日正好是最后一天
			&& value == getLastDay(month, isLeapYear);
	}

	/**
	 * 获取最后一天
	 *
	 * @param month      月，base1
	 * @param isLeapYear 是否闰年
	 * @return 最后一天
	 */
	private static int getLastDay(final int month, final boolean isLeapYear) {
		return Month.getLastDay(month - 1, isLeapYear);
	}
}
