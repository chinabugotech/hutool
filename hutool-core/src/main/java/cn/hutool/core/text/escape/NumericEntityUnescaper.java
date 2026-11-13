package cn.hutool.core.text.escape;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.text.replacer.StrReplacer;
import cn.hutool.core.util.CharUtil;

/**
 * 形如&#39;的反转义器
 *
 * @author looly
 *
 */
public class NumericEntityUnescaper extends StrReplacer {
	private static final long serialVersionUID = 1L;

	/**
	 * 替换单个数值字符引用（形如{@code &#39;}或{@code &#x1F600;}）为对应字符。<br>
	 * 码点大于0xFFFF的补充平面字符（如emoji）使用{@link Character#toChars(int)}转换为代理对后追加，
	 * 非法码点（超出Unicode范围或位于代理区）不替换，保留原文。
	 *
	 * @param str 原始字符串
	 * @param pos 当前处理位置
	 * @param out 替换结果输出
	 * @return 本次替换消耗的字符数，0表示不替换
	 */
	@Override
	protected int replace(CharSequence str, int pos, StrBuilder out) {
		final int len = str.length();
		// 检查以确保以&#开头
		if (str.charAt(pos) == '&' && pos < len - 2 && str.charAt(pos + 1) == '#') {
			int start = pos + 2;
			boolean isHex = false;
			final char firstChar = str.charAt(start);
			if (firstChar == 'x' || firstChar == 'X') {
				start++;
				isHex = true;
			}

			// 确保&#后还有数字
			if (start == len) {
				return 0;
			}

			int end = start;
			while (end < len && CharUtil.isHexChar(str.charAt(end))) {
				end++;
			}
			final boolean isSemiNext = (end != len) && (str.charAt(end) == ';');
			if (isSemiNext) {
				int entityValue;
				try {
					if (isHex) {
						entityValue = Integer.parseInt(str.subSequence(start, end).toString(), 16);
					} else {
						entityValue = Integer.parseInt(str.subSequence(start, end).toString(), 10);
					}
				} catch (final NumberFormatException nfe) {
					return 0;
				}
				if (entityValue > 0xFFFF) {
					// 补充平面字符需要两个char（代理对）表示，直接强转为char会丢失高位
					if (entityValue > Character.MAX_CODE_POINT
							|| (entityValue >= Character.MIN_SURROGATE && entityValue <= Character.MAX_SURROGATE)) {
						// 非法码点，保留原文
						return 0;
					}
					out.append(Character.toChars(entityValue));
				} else {
					out.append((char) entityValue);
				}
				return 2 + end - start + (isHex ? 1 : 0) + 1;
			}
		}
		return 0;
	}
}
