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

package cn.hutool.v7.core.text.finder;

import cn.hutool.v7.core.lang.Assert;
import cn.hutool.v7.core.text.CharSequenceUtil;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

import static cn.hutool.v7.core.text.CharSequenceUtil.isSubEquals;

/**
 * 字符串查找器
 *
 * @author Looly
 * @since 5.7.14
 */
public class StrFinder extends TextFinder {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 创建查找器，构造后须调用{@link #setText(CharSequence)} 设置被查找的文本
	 *
	 * @param strToFind       查找的字符串
	 * @param caseInsensitive 是否忽略大小写
	 * @return {@code StrFinder}
	 */
	public static StrFinder of(final CharSequence strToFind, final boolean caseInsensitive) {
		return new StrFinder(strToFind, caseInsensitive);
	}

	private final CharSequence strToFind;
	private final boolean caseInsensitive;
	private Map<Character, Integer> forwardOffsetMap;
	private Map<Character, Integer> reverseOffsetMap;

	/**
	 * 构造
	 *
	 * @param strToFind       查找的字符串
	 * @param caseInsensitive 是否忽略大小写
	 */
	public StrFinder(final CharSequence strToFind, final boolean caseInsensitive) {
		Assert.notEmpty(strToFind);
		this.strToFind = strToFind;
		this.caseInsensitive = caseInsensitive;
	}

	@Override
	public int start(int from) {
		Assert.notNull(this.text, "Text to find must be not null!");
		final int subLen = strToFind.length();
		final int textLen = text.length();

		// 基于Sunday算法实现高效子串查询
		if (negative) {
			if (this.reverseOffsetMap == null) {
				this.reverseOffsetMap = buildReverseOffsetMap(strToFind, caseInsensitive);
			}
			int maxIndex = textLen - subLen;
			if (from > maxIndex) {
				from = maxIndex;
			}
			int i = from;
			while (i >= 0) {
				if (isSubEquals(text, i, strToFind, 0, subLen, caseInsensitive)) {
					return i;
				}
				if (i - 1 < 0) {
					break;
				}
				char preChar = text.charAt(i - 1);
				int jump = reverseOffsetMap.getOrDefault(
					caseInsensitive ? Character.toLowerCase(preChar) : preChar,
					subLen + 1
				);
				i -= jump;
			}
		} else {
			if (this.forwardOffsetMap == null) {
				this.forwardOffsetMap = buildForwardOffsetMap(strToFind, caseInsensitive);
			}
			if (from < 0) {
				from = 0;
			}
			int endLimit = textLen - subLen;
			int i = from;
			while (i <= endLimit) {
				if (isSubEquals(text, i, strToFind, 0, subLen, caseInsensitive)) {
					return i;
				}
				if (i + subLen >= textLen) {
					break;
				}
				char nextChar = text.charAt(i + subLen);
				int jump = forwardOffsetMap.getOrDefault(
					caseInsensitive ? Character.toLowerCase(nextChar) : nextChar,
					subLen + 1
				);
				i += jump;
			}
		}

		return INDEX_NOT_FOUND;
	}

	@Override
	public int end(final int start) {
		if (start < 0) {
			return -1;
		}
		return start + strToFind.length();
	}

	/**
	 * 构建正向偏移表
	 */
	private static Map<Character, Integer> buildForwardOffsetMap(CharSequence pattern, boolean caseInsensitive) {
		int m = pattern.length();
		Map<Character, Integer> map = new HashMap<>(Math.min(m, 128));

		for (int i = 0; i < m; i++) {
			char c = pattern.charAt(i);
			int jump = m - i;

			if (caseInsensitive) {
				map.put(Character.toLowerCase(c), jump);
			} else {
				map.put(c, jump);
			}
		}
		return map;
	}

	/**
	 * 构建反向偏移表
	 */
	private static Map<Character, Integer> buildReverseOffsetMap(CharSequence pattern, boolean caseInsensitive) {
		int m = pattern.length();
		Map<Character, Integer> map = new HashMap<>(Math.min(m, 128));

		for (int i = m - 1; i >= 0; i--) {
			char c = pattern.charAt(i);
			int jump = i + 1;

			if (caseInsensitive) {
				map.put(Character.toLowerCase(c), jump);
			} else {
				map.put(c, jump);
			}
		}
		return map;
	}
}
