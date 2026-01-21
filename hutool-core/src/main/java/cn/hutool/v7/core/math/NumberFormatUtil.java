/*
 * Copyright (c) 2026 Hutool Team.
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

package cn.hutool.v7.core.math;

import cn.hutool.v7.core.convert.ConvertUtil;
import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.core.util.ObjUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

/**
 * 基于NumberFormat封装的数字格式化/解析工具类
 * 提供常用的数字格式化、字符串解析为数字的方法，包含异常处理和参数校验
 *
 * @author Looly
 * @since 7.0.0
 */
public class NumberFormatUtil {

	/**
	 * 默认区域
	 */
	private static final Locale DEFAULT_LOCALE = Locale.getDefault(Locale.Category.FORMAT);

	// region ----- format

	/**
	 * 使用默认区域（Locale.getDefault(Locale.Category.FORMAT)）格式化数字
	 *
	 * @param number 要格式化的数字（支持Number子类：Integer/Long/Double/BigDecimal等）
	 * @return 格式化后的字符串，null返回空字符串
	 */
	public static String format(final Number number) {
		return format(number, DEFAULT_LOCALE);
	}

	/**
	 * 指定区域格式化数字
	 *
	 * @param number 要格式化的数字
	 * @param locale 区域（如Locale.US、Locale.CHINA）
	 * @return 格式化后的字符串，null返回空字符串
	 */
	public static String format(final Number number, final Locale locale) {
		if (number == null) {
			return "";
		}
		final NumberFormat nf = NumberFormat.getInstance(locale);
		return nf.format(number);
	}

	/**
	 * 格式化数字并指定小数位数
	 *
	 * @param number         要格式化的数字
	 * @param fractionDigits 小数位数（>=0）
	 * @return 格式化后的字符串
	 */
	public static String format(final Number number, final int fractionDigits) {
		return format(number, fractionDigits, Locale.getDefault());
	}

	/**
	 * 指定区域和小数位数格式化数字
	 *
	 * @param number         要格式化的数字
	 * @param fractionDigits 小数位数（>=0）
	 * @param locale         区域
	 * @return 格式化后的字符串
	 */
	public static String format(final Number number, final int fractionDigits, final Locale locale) {
		if (number == null) {
			return "";
		}
		if (fractionDigits < 0) {
			throw new IllegalArgumentException("fractionDigits must be >=0：" + fractionDigits);
		}
		final NumberFormat nf = NumberFormat.getInstance(locale);
		nf.setMaximumFractionDigits(fractionDigits);
		nf.setMinimumFractionDigits(fractionDigits);
		return nf.format(number);
	}
	// endregion

	// region ----- parse

	/**
	 * 使用默认区域解析字符串为数字（返回BigDecimal，避免精度丢失）
	 *
	 * @param source 要解析的字符串（如"1,234.56"、"1234.56"）
	 * @return 解析后的BigDecimal，解析失败返回null
	 */
	public static Number parse(final String source) {
		return parse(source, DEFAULT_LOCALE);
	}

	/**
	 * 指定区域解析字符串为数字
	 *
	 * @param source 要解析的字符串
	 * @param locale 区域（解析时匹配对应格式，如Locale.US解析"1,234.56"）
	 * @return 解析后的BigDecimal，解析失败返回null
	 */
	public static Number parse(String source, final Locale locale) {
		// 参数校验
		source = StrUtil.trim( source);
		if(StrUtil.isEmpty(source)){
			return null;
		}

		// issue#I79VS7 去除头部加号
		source = StrUtil.removeAllPrefix(source, "+");

		// issue@4197@Github 转为半角
		source = ConvertUtil.toDBC(source);

		// issue#IDJ1NS@Gitee 处理科学计数法E+格式
		// NumberFormat对E+格式支持不佳,使用BigDecimal直接解析
		if (StrUtil.containsIgnoreCase(source, "e")) {
			try {
				return new BigDecimal(source);
			} catch (final NumberFormatException e) {
				// BigDecimal解析失败,继续使用NumberFormat尝试
			}
		}

		final NumberFormat nf = NumberFormat.getInstance(ObjUtil.defaultIfNull(locale, DEFAULT_LOCALE));
		if (nf instanceof DecimalFormat) {
			// issue#1818@Github
			// 当字符串数字超出double的长度时，会导致截断，此处使用BigDecimal接收
			((DecimalFormat) nf).setParseBigDecimal(true);
		}

		final ParsePosition pos = new ParsePosition(0);
		final Number result = nf.parse(source, pos);

		// 校验解析是否完全成功（pos.getIndex()等于字符串长度才是完全解析）
		if (result == null || pos.getIndex() != source.length()) {
			throw new NumberFormatException("Unparseable number: [" + source + "] at: " + pos.getIndex());
		}
		return result;
	}
	// endregion
}
