package cn.hutool.cron.pattern.matcher;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;

import java.util.Collections;
import java.util.List;

/**
 * 将表达式中的数字值列表转换为Boolean数组，匹配时匹配相应数组位
 *
 * @author Looly
 */
public class BoolArrayMatcher implements PartMatcher {

	/**
	 * 用户定义此字段的最小值
	 */
	private final int minValue;
	/**
	 * 用户定义此字段的最大值
	 * @since 5.8.41
	 */
	private final int maxValue;
	protected final boolean[] bValues;

	/**
	 * 构造
	 *
	 * @param intValueList 匹配值列表
	 */
	public BoolArrayMatcher(List<Integer> intValueList) {
		Assert.isTrue(CollUtil.isNotEmpty(intValueList), "Values must be not empty!");
		bValues = new boolean[Collections.max(intValueList) + 1];
		int min = Integer.MAX_VALUE;
		int max = 0;
		for (Integer value : intValueList) {
			min = Math.min(min, value);
			max = Math.max(max, value);
			bValues[value] = true;
		}
		this.minValue = min;
		this.maxValue = max;
	}

	@Override
	public boolean match(Integer value) {
		if(null != value && value >= minValue && value <= maxValue){
			return bValues[value];
		}
		return false;
	}

	@Override
	public int nextAfter(int value) {
		final int maxValue = this.maxValue;
		if(value == maxValue){
			return value;
		}
		final int minValue = this.minValue;
		if(value > minValue && value < maxValue){
			final boolean[] bValues = this.bValues;
			// 最大值永远小于数组长度，只需判断最大值边界
			while(value <= maxValue){
				if(value == maxValue || bValues[value]){
					// 达到最大值或达到第一个匹配值
					return value;
				}
				value++;
			}
		}

		// 两种情况返回最小值
		// 一是给定值小于最小值，那下一个匹配值就是最小值
		// 二是给定值大于最大值，那下一个匹配值也是下一轮的最小值
		return minValue;
	}

	/**
	 * 获取表达式定义的最小值
	 *
	 * @return 最小值
	 */
	public int getMinValue() {
		return this.minValue;
	}

	/**
	 * 获取表达式定义的最大值
	 *
	 * @return 最大值
	 * @since 5.8.41
	 */
	public int getMaxValue() {
		return this.maxValue;
	}

	@Override
	public String toString() {
		return StrUtil.format("Matcher:{}", new Object[]{this.bValues});
	}
}
