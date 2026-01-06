/*
 * Copyright (c) 2013-2026 Hutool Team.
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

package cn.hutool.v7.core.date;

import java.time.temporal.ChronoUnit;

/**
 * 日期时间单位，每个单位都是以毫秒为基数
 *
 * @author Looly
 */
public enum DateUnit {
	/**
	 * 一毫秒
	 */
	MS(1),
	/**
	 * 一秒的毫秒数
	 */
	SECOND(1000),
	/**
	 * 一分钟的毫秒数
	 */
	MINUTE(SECOND.getMillis() * 60),
	/**
	 * 一小时的毫秒数
	 */
	HOUR(MINUTE.getMillis() * 60),
	/**
	 * 一天的毫秒数
	 */
	DAY(HOUR.getMillis() * 24),
	/**
	 * 一周的毫秒数
	 */
	WEEK(DAY.getMillis() * 7);

	private final long millis;

	DateUnit(final long millis) {
		this.millis = millis;
	}

	/**
	 * @return 单位对应的毫秒数
	 */
	public long getMillis() {
		return this.millis;
	}

	/**
	 * 单位兼容转换，将DateUnit转换为对应的{@link ChronoUnit}
	 *
	 * @return {@link ChronoUnit}
	 * @since 5.4.5
	 */
	public ChronoUnit toChronoUnit() {
		return DateUnit.toChronoUnit(this);
	}

	/**
	 * 单位兼容转换，将{@link ChronoUnit}转换为对应的DateUnit
	 *
	 * @param unit {@link ChronoUnit}
	 * @return DateUnit，null表示不支持此单位
	 * @since 5.4.5
	 */
	public static DateUnit of(final ChronoUnit unit) {
		return switch (unit) {
			case MILLIS -> DateUnit.MS;
			case SECONDS -> DateUnit.SECOND;
			case MINUTES -> DateUnit.MINUTE;
			case HOURS -> DateUnit.HOUR;
			case DAYS -> DateUnit.DAY;
			case WEEKS -> DateUnit.WEEK;
			default -> null;
		};
	}

	/**
	 * 单位兼容转换，将DateUnit转换为对应的{@link ChronoUnit}
	 *
	 * @param unit DateUnit
	 * @return {@link ChronoUnit}
	 * @since 5.4.5
	 */
	public static ChronoUnit toChronoUnit(final DateUnit unit) {
		return switch (unit) {
			case MS -> ChronoUnit.MILLIS;
			case SECOND -> ChronoUnit.SECONDS;
			case MINUTE -> ChronoUnit.MINUTES;
			case HOUR -> ChronoUnit.HOURS;
			case DAY -> ChronoUnit.DAYS;
			case WEEK -> ChronoUnit.WEEKS;
		};
	}
}
