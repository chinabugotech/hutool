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

package cn.hutool.v7.core.math;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FractionTest {

	private Fraction fraction1;
	private Fraction fraction2;

	@BeforeEach
	public void setUp() {
		// 假设 Fraction 类有一个构造函数 Fraction(int numerator, int denominator)
		fraction1 = new Fraction(1, 2);
		fraction2 = new Fraction(3, 4);
	}

	@Test
	public void add_Fractions_ShouldReturnCorrectSum() {
		final Fraction result = fraction1.add(fraction2);
		assertEquals(new Fraction(5, 4), result);
	}

	@Test
	public void subtract_Fractions_ShouldReturnCorrectDifference() {
		final Fraction result = fraction1.subtract(fraction2);
		assertEquals(new Fraction(-1, 4), result);
	}

	@Test
	public void equals_Fractions_ShouldReturnTrueForEqualFractions() {
		final Fraction fraction3 = new Fraction(1, 2);
		assertEquals(fraction1, fraction3);
	}

	@Test
	public void equals_Fractions_ShouldReturnFalseForDifferentFractions() {
		assertNotEquals(fraction1, fraction2);
	}
}
