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

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MathUtilTest {
	@Test
	public void factorialTest(){
		long factorial = MathUtil.factorial(0);
		assertEquals(1, factorial);

		assertEquals(1L, MathUtil.factorial(1));
		assertEquals(1307674368000L, MathUtil.factorial(15));
		assertEquals(2432902008176640000L, MathUtil.factorial(20));

		factorial = MathUtil.factorial(5, 0);
		assertEquals(120, factorial);
		factorial = MathUtil.factorial(5, 1);
		assertEquals(120, factorial);

		assertEquals(5, MathUtil.factorial(5, 4));
		assertEquals(2432902008176640000L, MathUtil.factorial(20, 0));
	}

	@Test
	public void factorialTest2(){
		long factorial = MathUtil.factorial(new BigInteger("0")).longValue();
		assertEquals(1, factorial);

		assertEquals(1L, MathUtil.factorial(new BigInteger("1")).longValue());
		assertEquals(1307674368000L, MathUtil.factorial(new BigInteger("15")).longValue());
		assertEquals(2432902008176640000L, MathUtil.factorial(20));

		factorial = MathUtil.factorial(new BigInteger("5"), new BigInteger("0")).longValue();
		assertEquals(120, factorial);
		factorial = MathUtil.factorial(new BigInteger("5"), BigInteger.ONE).longValue();
		assertEquals(120, factorial);

		assertEquals(5, MathUtil.factorial(new BigInteger("5"), new BigInteger("4")).longValue());
		assertEquals(2432902008176640000L, MathUtil.factorial(new BigInteger("20"), BigInteger.ZERO).longValue());
	}

	@Test
	public void testMultipleOverflow() {
		final int a = 500000;
		final int b = 600000;

		// 原方法使用 a * b / gcd(a, b) 计算，a * b 会先溢出，得到最小公倍数为负数
		// 使用修改后的multiple方法，测试它是否能正确处理这种情况
		final int result = MathUtil.multiple(a, b);
		// 验证结果必须是正数（两个正数的最小公倍数必须为正）
		assertTrue(result > 0);
	}
}
