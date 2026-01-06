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

package cn.hutool.v7.swing.captcha.generator;

import cn.hutool.v7.core.math.Calculator;
import cn.hutool.v7.core.text.CharUtil;
import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.core.util.RandomUtil;

import java.io.Serial;

/**
 * 数字计算验证码生成器
 *
 * @author Looly
 * @since 4.1.2
 */
public class MathGenerator implements CodeGenerator {
	@Serial
	private static final long serialVersionUID = -5514819971774091076L;

	private static final String operators = "+-*";

	/**
	 * 参与计算数字最大长度
	 */
	private final int numberLength;
	/**
	 * 计算结果是否允许负数
	 */
	private final boolean resultHasNegativeNumber;

	/**
	 * 构造
	 */
	public MathGenerator() {
		this(2, false);
	}

	/**
	 * 构造
	 *
	 * @param numberLength            参与计算最大数字位数
	 * @param resultHasNegativeNumber 结果是否允许负数
	 */
	public MathGenerator(final int numberLength, final boolean resultHasNegativeNumber) {
		this.numberLength = numberLength;
		this.resultHasNegativeNumber = resultHasNegativeNumber;
	}

	@Override
	public String generate() {
		final int limit = getLimit();
		final char operator = RandomUtil.randomChar(operators);
		final int numberInt1;
		final int numberInt2;
		numberInt1 = RandomUtil.randomInt(limit);
		// 如果禁止了结果有负数，且计算方式正好计算为减法，需要第二个数小于第一个数
		if (!resultHasNegativeNumber && CharUtil.equals('-',operator,false)) {
			//如果第一个数为0，第二个数必须为0，随机[0,0)的数字会报错
			numberInt2 = numberInt1 == 0 ? 0 : RandomUtil.randomInt(0, numberInt1);
		} else {
			numberInt2 = RandomUtil.randomInt(limit);
		}
		String number1 = Integer.toString(numberInt1);
		String number2 = Integer.toString(numberInt2);

		number1 = StrUtil.padAfter(number1, this.numberLength, CharUtil.SPACE);
		number2 = StrUtil.padAfter(number2, this.numberLength, CharUtil.SPACE);

		return StrUtil.builder()//
			.append(number1)//
			.append(operator)//
			.append(number2)//
			.append('=').toString();
	}

	@Override
	public boolean verify(final String code, final String userInputCode) {
		final int result;
		try {
			result = Integer.parseInt(userInputCode);
		} catch (final NumberFormatException e) {
			// 用户输入非数字
			return false;
		}

		final int calculateResult = (int) Calculator.conversion(code);
		return result == calculateResult;
	}

	/**
	 * 获取验证码长度
	 *
	 * @return 验证码长度
	 */
	public int getLength() {
		return this.numberLength * 2 + 2;
	}

	/**
	 * 根据长度获取参与计算数字最大值
	 *
	 * @return 最大值
	 */
	private int getLimit() {
		return Integer.parseInt("1" + StrUtil.repeat('0', this.numberLength));
	}
}
