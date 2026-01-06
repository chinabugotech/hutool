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

package cn.hutool.v7.extra.pinyin.engine.houbb;

import cn.hutool.v7.core.lang.Assert;
import cn.hutool.v7.extra.pinyin.engine.PinyinEngine;
import com.github.houbb.pinyin.constant.enums.PinyinStyleEnum;
import com.github.houbb.pinyin.util.PinyinHelper;

/**
 * 封装了 houbb Pinyin 的引擎。
 *
 * <p>
 * houbb pinyin(https://github.com/houbb/pinyin)封装。
 * </p>
 *
 * <p>
 * 引入：
 * <pre>
 * &lt;dependency&gt;
 *     &lt;groupId&gt;com.github.houbb&lt;/groupId&gt;
 *     &lt;artifactId&gt;pinyin&lt;/artifactId&gt;
 *     &lt;version&gt;0.2.0&lt;/version&gt;
 * &lt;/dependency&gt;
 * </pre>
 *
 * @author Looly
 */
public class HoubbEngine implements PinyinEngine {

	/**
	 * 构造
	 */
	public HoubbEngine() {
		// SPI方式加载时检查库是否引入
		Assert.notNull(PinyinHelper.class);
	}

	@Override
	public String getPinyin(final char c, final boolean tone) {
		final String result;
		result = PinyinHelper.toPinyin(String.valueOf(c), tone ? PinyinStyleEnum.DEFAULT : PinyinStyleEnum.NORMAL);
		return result;
	}

	@Override
	public String getPinyin(final String str, final String separator, final boolean tone) {
		final String result;
		result = PinyinHelper.toPinyin(str, tone ? PinyinStyleEnum.DEFAULT : PinyinStyleEnum.NORMAL, separator);
		return result;
	}
}
