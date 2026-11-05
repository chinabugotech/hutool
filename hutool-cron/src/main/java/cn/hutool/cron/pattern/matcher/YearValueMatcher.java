package cn.hutool.cron.pattern.matcher;

import java.util.Collection;
import java.util.TreeSet;

/**
 * 年匹配<br>
 * 考虑年数字太大，不适合boolean数组，单独使用{@link TreeSet}匹配<br>
 * 年份按升序保存，保证{@link #nextAfter(int)}返回的是不小于给定值的最小年份，而非表达式中第一个书写的年份
 *
 * @author Looly
 */
public class YearValueMatcher implements PartMatcher {

	private final TreeSet<Integer> valueList;

	public YearValueMatcher(Collection<Integer> intValueList) {
		this.valueList = new TreeSet<>(intValueList);
	}

	@Override
	public boolean match(Integer t) {
		return null != t && valueList.contains(t);
	}

	@Override
	public int nextAfter(int value) {
		final Integer year = valueList.ceiling(value);
		// null表示年无效，此表达式整体无效
		return null == year ? -1 : year;
	}
}
