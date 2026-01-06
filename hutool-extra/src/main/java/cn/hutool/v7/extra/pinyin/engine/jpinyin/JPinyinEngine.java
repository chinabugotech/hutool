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

package cn.hutool.v7.extra.pinyin.engine.jpinyin;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import cn.hutool.v7.core.array.ArrayUtil;
import cn.hutool.v7.core.lang.Assert;
import cn.hutool.v7.extra.pinyin.engine.PinyinEngine;

/**
 * 封装了Jpinyin的引擎。
 *
 * <p>
 * jpinyin（github库作者已删除）封装。
 * </p>
 *
 * <p>
 * 引入：
 * <pre>
 * &lt;dependency&gt;
 *     &lt;groupId&gt;com.github.stuxuhai&lt;/groupId&gt;
 *     &lt;artifactId&gt;jpinyin&lt;/artifactId&gt;
 *     &lt;version&gt;1.1.8&lt;/version&gt;
 * &lt;/dependency&gt;
 * </pre>
 *
 * @author Looly
 */
public class JPinyinEngine implements PinyinEngine {

	/**
	 * 构造
	 */
	public JPinyinEngine() {
		// SPI方式加载时检查库是否引入
		Assert.notNull(PinyinHelper.class);
	}

	@Override
	public String getPinyin(final char c, final boolean tone) {
		final String[] results = PinyinHelper.convertToPinyinArray(c, tone ? PinyinFormat.WITH_TONE_MARK : PinyinFormat.WITHOUT_TONE);
		return ArrayUtil.isEmpty(results) ? String.valueOf(c) : results[0];
	}

	@Override
	public String getPinyin(final String str, final String separator, final boolean tone) {
		try {
			return PinyinHelper.convertToPinyinString(str, separator, tone ? PinyinFormat.WITH_TONE_MARK : PinyinFormat.WITHOUT_TONE);
		} catch (final PinyinException e) {
			throw new cn.hutool.v7.extra.pinyin.PinyinException(e);
		}
	}
}
