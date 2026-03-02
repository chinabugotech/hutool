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

import cn.hutool.v7.core.array.ArrayUtil;
import cn.hutool.v7.core.util.JdkUtil;
import cn.hutool.v7.core.util.ObjUtil;
import cn.hutool.v7.core.util.SystemUtil;

import java.time.ZoneId;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * {@link ZoneId}和{@link TimeZone}相关封装
 *
 * @author Looly
 * @since 5.7.15
 */
public class ZoneUtil {

	/**
	 * UTC 的 ZoneID
	 */
	public static final TimeZone ZONE_UTC = ZoneUtil.getTimeZone("UTC");
	/**
	 * UTC 的 TimeZone
	 */
	public static final ZoneId ZONE_ID_UTC = ZONE_UTC.toZoneId();

	/**
	 * A public version of {@link java.util.TimeZone}'s package private {@code GMT_ID} field.
	 */
	public static final String GMT_ID = "GMT";

	/**
	 * The GMT time zone.
	 */
	public static final TimeZone ZONE_GMT = getTimeZone(GMT_ID);

	/**
	 * {@link TimeZone}转换为{@link ZoneId}，{@code null}则返回系统默认值
	 *
	 * @param timeZone {@link TimeZone}，{@code null}则返回系统默认值
	 * @return {@link ZoneId}
	 */
	public static ZoneId toZoneId(final TimeZone timeZone) {
		if (null == timeZone) {
			return ZoneId.systemDefault();
		}

		return timeZone.toZoneId();
	}

	/**
	 * 获取指定偏移量的可用时区，如果有多个时区匹配，使用第一个
	 *
	 * @param rawOffset 偏移量
	 * @param timeUnit  偏移量单位
	 * @return 时区
	 */
	public static TimeZone getTimeZoneByOffset(final int rawOffset, final TimeUnit timeUnit) {
		final String id = getAvailableID(rawOffset, timeUnit);
		return null == id ? null : TimeZone.getTimeZone(id);
	}

	/**
	 * 在{@link ZoneId#SHORT_IDS}中映射ID后，委托给{@link TimeZone#getTimeZone(String)}。
	 * <p>
	 * 在Java 25中，使用{@link ZoneId#SHORT_IDS}中的ID调用{@link TimeZone#getTimeZone(String)}会在{@link System#err}中写入如下形式的消息：
	 * </p>
	 *
	 * <pre>
	 * WARNING: Use of the three-letter time zone ID "the-short-id" is deprecated and it will be removed in a future release
	 * </pre>
	 * <p>
	 * 您可以通过设置系统属性{@code "TimeZones.mapShortIDs=false"}来禁用从{@link ZoneId#SHORT_IDS}的映射。
	 * </p>
	 *
	 * @param id 与{@link TimeZone#getTimeZone(String)}相同。
	 * @return 与{@link TimeZone#getTimeZone(String)}相同。
	 */
	public static TimeZone getTimeZone(final String id) {
		return TimeZone.getTimeZone(JdkUtil.IS_AT_LEAST_JDK25 && mapShortIDs() ? ZoneId.SHORT_IDS.getOrDefault(id, id) : id);
	}

	/**
	 * {@link ZoneId}转换为{@link TimeZone}，{@code null}则返回系统默认值
	 *
	 * @param zoneId {@link ZoneId}，{@code null}则返回系统默认值
	 * @return {@link TimeZone}
	 */
	public static TimeZone getTimeZone(final ZoneId zoneId) {
		if (null == zoneId) {
			return TimeZone.getDefault();
		}

		return TimeZone.getTimeZone(zoneId);
	}

	/**
	 * 获取指定偏移量的可用时区ID，如果有多个时区匹配，使用第一个
	 *
	 * @param rawOffset 偏移量
	 * @param timeUnit  偏移量单位
	 * @return 时区ID，未找到返回{@code null}
	 */
	public static String getAvailableID(final int rawOffset, final TimeUnit timeUnit) {
		final String[] availableIDs = TimeZone.getAvailableIDs(
			(int) ObjUtil.defaultIfNull(timeUnit, TimeUnit.MILLISECONDS).toMillis(rawOffset));
		return ArrayUtil.isEmpty(availableIDs) ? null : availableIDs[0];
	}

	/**
	 * 是否映射{@link ZoneId#SHORT_IDS}中的ID<br>
	 * 默认为true，可以通过设置系统属性{@code "TimeZone.mapShortIDs=false"}来禁用。
	 *
	 * @return 是否映射{@link ZoneId#SHORT_IDS}中的ID
	 */
	private static boolean mapShortIDs() {
		return SystemUtil.getBoolean("TimeZone.mapShortIDs", true);
	}
}
