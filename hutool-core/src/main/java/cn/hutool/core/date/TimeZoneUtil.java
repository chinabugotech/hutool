package cn.hutool.core.date;

import cn.hutool.core.util.JdkUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.SystemPropsUtil;

import java.time.ZoneId;
import java.util.TimeZone;

/**
 * 时间时区工具类
 *
 * @author looly
 * @since 5.8.44
 */
public class TimeZoneUtil {
	/**
	 * A public version of {@link java.util.TimeZone}'s package private {@code GMT_ID} field.
	 */
	public static final String GMT_ID = "GMT";

	/**
	 * The GMT time zone.
	 */
	public static final TimeZone GMT = getTimeZone(GMT_ID);

	/**
	 * Delegates to {@link TimeZone#getTimeZone(String)} after mapping an ID if it's in {@link ZoneId#SHORT_IDS}.
	 * <p>
	 * On Java 25, calling {@link TimeZone#getTimeZone(String)} with an ID in {@link ZoneId#SHORT_IDS} writes a message to {@link System#err} in the form:
	 * </p>
	 *
	 * <pre>
	 * WARNING: Use of the three-letter time zone ID "the-short-id" is deprecated and it will be removed in a future release
	 * </pre>
	 * <p>
	 * You can disable mapping from {@link ZoneId#SHORT_IDS} by setting the system property {@code "TimeZones.mapShortIDs=false"}.
	 * </p>
	 *
	 * @param id Same as {@link TimeZone#getTimeZone(String)}.
	 * @return Same as {@link TimeZone#getTimeZone(String)}.
	 */
	public static TimeZone getTimeZone(final String id) {
		return TimeZone.getTimeZone(JdkUtil.IS_AT_LEAST_JDK25 && mapShortIDs() ? ZoneId.SHORT_IDS.getOrDefault(id, id) : id);
	}

	private static boolean mapShortIDs() {
		return SystemPropsUtil.getBoolean("TimeZones.mapShortIDs", true);
	}

	/**
	 * Returns the given TimeZone if non-{@code null}, otherwise {@link TimeZone#getDefault()}.
	 *
	 * @param timeZone a locale or {@code null}.
	 * @return the given locale if non-{@code null}, otherwise {@link TimeZone#getDefault()}.
	 */
	public static TimeZone toTimeZone(final TimeZone timeZone) {
		return ObjectUtil.defaultIfNull(timeZone, TimeZone::getDefault);
	}
}
