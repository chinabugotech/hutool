package cn.hutool.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.UtilException;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 版本对比工具
 * 对 {@link cn.hutool.core.comparator.VersionComparator} 的封装
 * 最主要功能包括：
 *
 *
 * <pre>
 * 1. 版本表达式匹配
 * 2. 单个版本匹配
 * </pre>
 *
 * @author winlans
 * @see cn.hutool.core.comparator.VersionComparator
 */
public class VersionUtil {

	private static final Pattern COMPARE_REG = Pattern.compile("^[<>≥≤]=?");
	// 默认多版本分隔符
	private static final String defaultVersionsDelimiter = ";";

	/**
	 * 是否匹配任意一个版本
	 *
	 * @param currentVersion  当前版本
	 * @param compareVersions 待匹配的版本列表
	 * @return true 包含待匹配的版本
	 */
	public static boolean anyMatch(String currentVersion, Collection<String> compareVersions) {
		return matchEl(currentVersion, CollUtil.join(compareVersions, defaultVersionsDelimiter));
	}

	/**
	 * 是否匹配任意一个版本
	 *
	 * @param currentVersion  当前版本
	 * @param compareVersions 待匹配的版本列表
	 * @return true 包含待匹配的版本
	 */
	public static boolean anyMatch(String currentVersion, String... compareVersions) {
		return matchEl(currentVersion, ArrayUtil.join(compareVersions, defaultVersionsDelimiter));
	}

	/**
	 * 当前版本大于待比较版本
	 *
	 * @param currentVersion 当前版本
	 * @param compareVersion 待比较版本
	 * @return true  当前版本大于待比较版本
	 */
	public static boolean isGreaterThan(String currentVersion, String compareVersion) {
		return matchEl(currentVersion, ">" + compareVersion);
	}

	/**
	 * 当前版本大于等于待比较版本
	 *
	 * @param currentVersion 当前版本
	 * @param compareVersion 待比较版本
	 * @return true  当前版本大于等于待比较版本
	 */
	public static boolean isGreaterThanOrEqual(String currentVersion, String compareVersion) {
		return matchEl(currentVersion, ">=" + compareVersion);
	}

	/**
	 * 当前版本小于待比较版本
	 *
	 * @param currentVersion 当前版本
	 * @param compareVersion 待比较版本
	 * @return true  当前版本小于待比较版本
	 */
	public static boolean isLessThan(String currentVersion, String compareVersion) {
		return matchEl(currentVersion, "<" + compareVersion);
	}

	/**
	 * 当前版本小于等于待比较版本
	 *
	 * @param currentVersion 当前版本
	 * @param compareVersion 待比较版本
	 * @return true  当前版本小于等于待比较版本
	 */
	public static boolean isLessThanOrEqual(String currentVersion, String compareVersion) {
		return matchEl(currentVersion, "<=" + compareVersion);
	}

	/**
	 * 当前版本是否满足版本表达式
	 * <pre>{@code
	 *     matchEl("1.0.2", ">=1.0.2") == true
	 *     matchEl("1.0.2", "<1.0.1;1.0.2") == true
	 *     matchEl("1.0.2", "<1.0.2") == false
	 *     matchEl("1.0.2", "1.0.0-1.1.1") == true
	 *     matchEl("1.0.2", "1.0.0-1.1.1") == true
	 * }</pre>
	 *
	 * @param currentVersion 当前版本
	 * @param versionEl      版本表达式
	 * @return true  当前版本是否满足版本表达式
	 */
	public static boolean matchEl(String currentVersion, String versionEl) {
		return matchEl(currentVersion, versionEl, defaultVersionsDelimiter);
	}

	/**
	 * 当前版本是否满足版本表达式
	 * <pre>{@code
	 *     matchEl("1.0.2", ">=1.0.2", ";") == true
	 *     matchEl("1.0.2", "<1.0.1,1.0.2", ",") == true
	 *     matchEl("1.0.2", "<1.0.2", ";") == false
	 *     matchEl("1.0.2", "1.0.0-1.1.1", ",") == true
	 *     matchEl("1.0.2", "1.0.1,1.0.2-1.1.1", ",") == true
	 * }</pre>
	 *
	 * @param currentVersion    当前版本
	 * @param versionEl         版本表达式（可以匹配多个条件，使用指定的分隔符（默认;）分隔）,
	 *                          {@code '-'}表示范围包含左右版本,如果 {@code '-'}的左边没有，表示小于等于某个版本号， 右边表示大于等于某个版本号。
	 *                          支持比较符号{@code '>'},{@code '<'}, {@code '>='},{@code '<='}，{@code '≤'}，{@code '≥'}
	 *
	 *                          <ul>
	 *                          <li>{@code 1.0.1-1.2.4, 1.9.8} 表示版本号 大于等于{@code 1.0.1}且小于等于{@code 1.2.4} 或 版本{@code 1.9.8}</li>
	 *                          <li>{@code >=2.0.0, 1.9.8} 表示版本号 大于等于{@code 2.0.0}或 版本{@code 1.9.8}</li>
	 *                          </ul>
	 * @param versionsDelimiter 多表达式分隔符
	 * @return true  当前版本是否满足版本表达式
	 */
	public static boolean matchEl(String currentVersion, String versionEl, String versionsDelimiter) {
		if (StrUtil.isBlank(versionsDelimiter)
			|| StrUtil.equals("-", versionsDelimiter)
			|| ReUtil.isMatch(COMPARE_REG, versionsDelimiter)) {
			throw new UtilException("非法的版本分隔符：" + versionsDelimiter);
		}

		if (StrUtil.isBlank(versionEl) || StrUtil.isBlank(currentVersion)) {
			return false;
		}
		String trimmedVersion = StrUtil.trim(currentVersion);

		List<String> els = StrUtil.split(versionEl, versionsDelimiter, true, true);
		if (CollUtil.isEmpty(els)) {
			return false;
		}

		for (String el : els) {
			el = el.trim();
			Matcher matcher = COMPARE_REG.matcher(el);
			if (matcher.find()) {
				String op = matcher.group();
				String ver = StrUtil.removePrefix(el, op);
				switch (op) {
					case ">=":
					case "≥":
						if (StrUtil.compareVersion(trimmedVersion, ver) >= 0) {
							return true;
						}
						break;
					case "<=":
					case "≤":
						if (StrUtil.compareVersion(trimmedVersion, ver) <= 0) {
							return true;
						}
						break;
					case "<":
						if (StrUtil.compareVersion(trimmedVersion, ver) < 0) {
							return true;
						}
						break;
					case ">":
						if (StrUtil.compareVersion(trimmedVersion, ver) > 0) {
							return true;
						}
						break;
					default:
						return false;
				}
			} else if (StrUtil.contains(el, "-")) {
				int index = el.indexOf('-');
				String left = StrUtil.blankToDefault(StrUtil.trim(el.substring(0, index)), "");
				String right = StrUtil.blankToDefault(StrUtil.trim(el.substring(index + 1)), "");

				boolean leftMatch = StrUtil.isBlank(left) || StrUtil.compareVersion(left, trimmedVersion) <= 0;
				boolean rightMatch = StrUtil.isBlank(right) || StrUtil.compareVersion(right, trimmedVersion) >= 0;
				if (leftMatch && rightMatch) {
					return true;
				}
			} else if (Objects.equals(trimmedVersion, el)) {
				return true;
			}
		}
		return false;
	}
}
