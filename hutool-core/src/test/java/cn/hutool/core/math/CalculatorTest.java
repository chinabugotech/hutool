package cn.hutool.core.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {

	@Test
	public void conversationTest(){
		final double conversion = Calculator.conversion("(0*1--3)-5/-4-(3*(-2.13))");
		assertEquals(10.64, conversion, 0);
	}

	@Test
	public void conversationTest2(){
		final double conversion = Calculator.conversion("77 * 12");
		assertEquals(924.0, conversion, 0);
	}

	@Test
	public void conversationTest3(){
		final double conversion = Calculator.conversion("1");
		assertEquals(1, conversion, 0);
	}

	@Test
	public void conversationTest4(){
		final double conversion = Calculator.conversion("(88*66/23)%26+45%9");
		assertEquals((88D * 66 / 23) % 26, conversion, 0.0000000001);
	}

	@Test
	public void conversationTest5(){
		// https://github.com/chinabugotech/hutool/issues/1984
		final double conversion = Calculator.conversion("((1/1) / (1/1) -1) * 100");
		assertEquals(0, conversion, 0);
	}

	@Test
	public void conversationTest6() {
		final double conversion = Calculator.conversion("-((2.12-2) * 100)");
		assertEquals(-1D * (2.12 - 2) * 100, conversion, 0.01);
	}

	@Test
	public void conversationTest7() {
		//https://gitee.com/chinabugotech/hutool/issues/I4KONB
		final double conversion = Calculator.conversion("((-2395+0) * 0.3+140.24+35+90)/30");
		assertEquals(-15.11, conversion, 0.01);
	}

	@Test
	public void issue2964Test() {
		// https://github.com/chinabugotech/hutool/issues/2964
		final double calcValue = Calculator.conversion("(11+2)12");
		assertEquals(156D, calcValue, 0.001);
	}

	@Test
	void issue3787Test() {
		final Calculator calculator1 = new Calculator();
		double result = calculator1.calculate("0+50/100x(1/0.5)");
		assertEquals(1D, result);

		result = calculator1.calculate("0+50/100X(1/0.5)");
		assertEquals(1D, result);
	}

	@Test
	public void scientificNotationPlusTest() {
		// 测试科学记数法中的 + 号是否被正确处理
		final double conversion = Calculator.conversion("1e+3");
		assertEquals(1000.0, conversion, 0.001);

		// 更复杂的科学记数法表达式
		final double conversion2 = Calculator.conversion("2.5e+2 + 1.0e-1");
		assertEquals(250.1, conversion2, 0.001);
	}

	@Test
	public void unaryOperatorConsistencyTest() {
		// 测试连续的一元运算符：双重负号--3，等同于 -( -3 ) = 3
		final double conversion = Calculator.conversion("--3");
		assertEquals(3.0, conversion, 0.001);

		// 测试连续的一元运算符：正号后跟负号，等同于 +( -3 ) = -3
		final double conversion2 = Calculator.conversion("+-3");
		assertEquals(-3.0, conversion2, 0.001);

		// 测试表达式开始的一元+运算符
		final double conversion3 = Calculator.conversion("+3");
		assertEquals(3.0, conversion3, 0.001);

		// 测试表达式开始的一元-运算符
		final double conversion4 = Calculator.conversion("-3");
		assertEquals(-3.0, conversion4, 0.001);
	}

	@Test
	public void percentOperatorTest() {
		//基础 % 运算
		assertEquals(1.0, Calculator.conversion("10 % 3"), 0.001);

		// % 运算符后跟连续的一元运算符的情况
		assertEquals(1.0, Calculator.conversion("10 % +-3"), 0.001);
		assertEquals(1.0, Calculator.conversion("10 % -3"), 0.001);

		// 带括号的 % 后一元负号
		assertEquals(1.0, Calculator.conversion("10 % (-3)"), 0.001);

		// % 与 * / 的优先级测试
		assertEquals(2.0, Calculator.conversion("10 * 5 % 3"), 0.001);
		assertEquals(1.0, Calculator.conversion("20 / 5 % 3"), 0.001);

		//连续 % 运算
		assertEquals(2.0, Calculator.conversion("100 % 7 % 3"), 0.001);

		// % 与 + - 混合运算
		assertEquals(13.0, Calculator.conversion("10 + 15 % 4"), 0.001);

		//负数操作数的 % 运算
		assertEquals(-1.0, Calculator.conversion("-10 % 3"), 0.001);

		// 两个负数的 % 运算
		assertEquals(-1.0, Calculator.conversion("-10 % -3"), 0.001);

		// 小数的 % 运算
		assertEquals(0.9, Calculator.conversion("10.5 % 3.2"), 0.001);
	}

}
