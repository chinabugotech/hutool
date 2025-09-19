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

package cn.hutool.v7.extra.pinyin;

import cn.hutool.v7.core.regex.PatternPool;
import cn.hutool.v7.core.regex.ReUtil;
import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.extra.pinyin.engine.PinyinEngine;
import cn.hutool.v7.extra.pinyin.engine.PinyinEngineFactory;

/**
 * 拼音工具类，用于快速获取拼音
 *
 * @author Looly
 */
public class PinyinUtil {

	/**
	 * 创建拼音引擎
	 *
	 * @param engineName 引擎名称
	 * @return {@link PinyinEngine}
	 */
	public static PinyinEngine createEngine(final String engineName) {
		return PinyinEngineFactory.createEngine(engineName);
	}

	/**
	 * 获得全局单例的拼音引擎
	 *
	 * @return 全局单例的拼音引擎
	 */
	public static PinyinEngine getEngine() {
		return PinyinEngineFactory.getEngine();
	}

	/**
	 * 如果c为汉字，则返回大写拼音；如果c不是汉字，则返回String.valueOf(c)
	 *
	 * @param c 任意字符，汉字返回拼音，非汉字原样返回
	 * @return 汉字返回拼音，非汉字原样返回
	 */
	public static String getPinyin(final char c) {
		return getEngine().getPinyin(c);
	}

	/**
	 * 如果c为汉字，则返回大写拼音；如果c不是汉字，则返回String.valueOf(c)
	 *
	 * @param c    任意字符，汉字返回拼音，非汉字原样返回
	 * @param tone 是否保留声调
	 * @return 汉字返回拼音，非汉字原样返回
	 */
	public static String getPinyin(final char c, final boolean tone) {
		return getEngine().getPinyin(c, tone);
	}

	/**
	 * 将输入字符串转为拼音，每个字之间的拼音使用空格分隔
	 *
	 * @param str 任意字符，汉字返回拼音，非汉字原样返回
	 * @return 汉字返回拼音，非汉字原样返回
	 */
	public static String getPinyin(final String str) {
		return getPinyin(str, StrUtil.SPACE);
	}

	/**
	 * 将输入字符串转为拼音，每个字之间的拼音使用空格分隔
	 *
	 * @param str  任意字符，汉字返回拼音，非汉字原样返回
	 * @param tone 是否保留声调
	 * @return 汉字返回拼音，非汉字原样返回
	 */
	public static String getPinyin(final String str, final boolean tone) {
		return getPinyin(str, StrUtil.SPACE, tone);
	}

	/**
	 * 将输入字符串转为拼音，以字符为单位插入分隔符
	 *
	 * @param str       任意字符，汉字返回拼音，非汉字原样返回
	 * @param separator 每个字拼音之间的分隔符
	 * @return 汉字返回拼音，非汉字原样返回
	 */
	public static String getPinyin(final String str, final String separator) {
		return getEngine().getPinyin(str, separator);
	}

	/**
	 * 将输入字符串转为拼音，以字符为单位插入分隔符
	 *
	 * @param str       任意字符，汉字返回拼音，非汉字原样返回
	 * @param separator 每个字拼音之间的分隔符
	 * @param tone      是否保留声调
	 * @return 汉字返回拼音，非汉字原样返回
	 */
	public static String getPinyin(final String str, final String separator, final boolean tone) {
		return getEngine().getPinyin(str, separator, tone);
	}

	/**
	 * 将输入字符串转为拼音首字母，其它字符原样返回
	 *
	 * @param c 任意字符，汉字返回拼音，非汉字原样返回
	 * @return 汉字返回拼音，非汉字原样返回
	 */
	public static char getFirstLetter(final char c) {
		return getEngine().getFirstLetter(c);
	}

	/**
	 * 将输入字符串转为拼音首字母，其它字符原样返回
	 *
	 * @param str       任意字符，汉字返回拼音，非汉字原样返回，{@code null}返回{@code null}
	 * @param separator 分隔符
	 * @return 汉字返回拼音，非汉字原样返回；str为{@code null}返回{@code null}
	 */
	public static String getFirstLetter(final String str, final String separator) {
		return (str == null) ? null :getEngine().getFirstLetter(str, separator);
	}

	/**
	 * 是否为中文字符
	 *
	 * @param c 字符
	 * @return 是否为中文字符
	 */
	public static boolean isChinese(final char c) {
		return '〇' == c || ReUtil.isMatch(PatternPool.CHINESE, String.valueOf(c));
	}
}
