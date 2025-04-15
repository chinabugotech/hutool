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
