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

package cn.hutool.v7.core.math;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTest {

	@Test
	public void yuanToCentTest() {
		final Money money = new Money("1234.56");
		assertEquals(123456, money.getCent());

		assertEquals(123456, MathUtil.yuanToCent(1234.56));
	}

	@Test
	public void centToYuanTest() {
		final Money money = new Money(1234, 56);
		assertEquals(1234.56D, money.getAmount().doubleValue(), 0);

		assertEquals(1234.56D, MathUtil.centToYuan(123456), 0);
	}

	@Test
	public void currencyScalingTest() {
		Money jpyMoney = new Money(0, Currency.getInstance("JPY"));
		jpyMoney.setAmount(BigDecimal.ONE);
		assertEquals(1, jpyMoney.getCent());
	}
}
