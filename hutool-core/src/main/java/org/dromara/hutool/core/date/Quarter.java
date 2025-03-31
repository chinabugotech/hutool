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

package org.dromara.hutool.core.date;

import org.dromara.hutool.core.lang.Assert;

import java.time.temporal.ChronoField;

/**
 * 季度枚举
 *
 * @author zhfish(https : / / github.com / zhfish)
 * @see #Q1
 * @see #Q2
 * @see #Q3
 * @see #Q4
 */
public enum Quarter {

	/**
	 * 第一季度
	 */
	Q1(1),
	/**
	 * 第二季度
	 */
	Q2(2),
	/**
	 * 第三季度
	 */
	Q3(3),
	/**
	 * 第四季度
	 */
	Q4(4);

	// ---------------------------------------------------------------
	private final int value;

	Quarter(final int value) {
		this.value = value;
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
	 * @param intValue 季度int表示
	 * @return {@code Quarter}
	 * @see #Q1
	 * @see #Q2
	 * @see #Q3
	 * @see #Q4
	 */
	public static Quarter of(final int intValue) {
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
	public static Quarter fromMonth(final int monthValue) {
		ChronoField.MONTH_OF_YEAR.checkValidValue(monthValue);
		return of(computeQuarterValueInternal(monthValue));
	}

	/**
	 * 根据给定的月份返回对应的季度
	 *
	 * @param month 月份
	 * @return 对应的季度
	 */
	public static Quarter fromMonth(final Month month) {
		Assert.notNull(month);
		final int monthValue = month.getValue();
		return of(computeQuarterValueInternal(monthValue));
	}

	/**
	 * 该季度的第一个月
	 *
	 * @return 结果
	 */
	public Month firstMonth() {
		return Month.of(value * 3 - 3);
	}

	/**
	 * 该季度最后一个月
	 *
	 * @return 结果
	 */
	public Month lastMonth() {
		return Month.of(value * 3 - 1);
	}

	/**
	 * 计算给定月份对应的季度值
	 *
	 * @param monthValue 月份值，取值范围为1到12
	 * @return 对应的季度值
	 */
	private static int computeQuarterValueInternal(final int monthValue) {
		return (monthValue - 1) / 3 + 1;
	}
}
